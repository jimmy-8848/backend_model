<script setup>
import Card from "@/views/components/Card.vue";
import {Message, Refresh, Select, User} from "@element-plus/icons-vue";
import {useStore} from "@/store/index.js";
import {computed, reactive, ref} from "vue";
import {get, getAccessHeader, post} from "@/net/index.js";
import {ElMessage} from "element-plus";
import axios from "axios";

const baseFormRef = ref()
const emailFormRef = ref()
const store = useStore()
const baseForm = reactive({
  username: '',
  gender: 0,
  phone: '',
  qq: '',
  wx: '',
  desc: ''
})

const desc = ref("")

const emailForm= reactive({
  email: '',
  code: ''
})

const coldTime = ref(0)
const emailValid = ref(true)

const onValidate = (prop,isValid) =>{
  if(prop === 'email') emailValid.value = isValid
}

function modifyEmail(){
  emailFormRef.value.validate(isValid => {
    if(isValid){
      post('/api/user/modify-email',emailForm,()=>{
        ElMessage.success('电子邮件修改成功！')
        store.user.email = emailForm.email
        emailForm.code = ''
      },(url,code,message)=>{
        ElMessage.warning(message)
        coldTime.value = 0
      })
    }
  })
}

function sendEmailCode(){
  emailFormRef.value.validate(isValid => {
    if(isValid){
      coldTime.value = 60
      get(`/api/auth/ask-code?email=${emailForm.email}&type=modify`,()=>{
        const interval = setInterval(()=>{
          coldTime.value--
          if(coldTime.value === 0)
            clearInterval(interval)
        },1000)
        ElMessage.success(`验证码已成功发送到邮箱:${emailForm.email},请注意查收`)
      },(url,code,message)=>{
        ElMessage.warning(message)
        coldTime.value = 0
        emailForm.code = ''
      })
    }
  })
}

function saveDetails(){
  baseFormRef.value.validate(isValid => {
    if(isValid){
      loading.base = true
      post("/api/user/save-details",baseForm,()=>{
        loading.base = false
        ElMessage.success("修改用户信息成功")
        store.user.username = baseForm.username
        desc.value = baseForm.desc
      },(__,_,message)=>{
        ElMessage.error(message)
        loading.base = false
      })
    }
  })
}

const loading = reactive({
  form: true,
  base: false
})

get("/api/user/details",(data)=>{
  baseForm.gender = data.gender
  baseForm.phone = data.phone
  baseForm.qq = data.qq
  baseForm.wx = data.wx
  emailForm.email = store.user.email
  baseForm.desc = desc.value = data.desc
  loading.form = false
})

const validateUsername=(rule,value,callback)=>{
  if(value === '') callback(new Error('请输入用户名'))
  else if(!/^[a-zA-Z0-9_]{3,16}$/.test(baseForm.username)) callback(new Error('用户名只允许字母（大小写）、数字、下划线'))
  else callback()
}

const rule = {
  username:[{validator:validateUsername,trigger:['blur','change']}],
  email:[{required:true,message:'请输入电子邮箱',trigger:['blur']},
    {type:'email',message: '请输入正确格式的电子邮箱',trigger:['blur','change']}],
  code:[{required:true,message:'请输入验证码',trigger:['blur']}]
}
//上传图像前检查类型和大小
function beforeAvatarUpload(rawFile){
  //检查类型 一般是PNG/JPG
  if(rawFile.type !== 'image/png' && rawFile.type!=='image/jpeg'){
    ElMessage.error('上传文件类型只能是PNF/JPG')
    return false;
  }else if(rawFile.size / 1024 > 100){
    ElMessage.error('大小不能超过100KB')
    return false;
  }
  ElMessage.success('上传头像成功')
  return true;
}
//上传图像成功后
function uploadAvatarSuccess(response){
  store.user.avatar = response.data
}
const registerTime = computed(()=>new Date(store.user.registerTime).toLocaleString())
</script>

<template>
<div style="display: flex;max-width: 950px;margin: auto">
  <div class="settings-card-left">
    <Card title="账号信息设置" desc="在这里编辑您的个人信息，您可以在隐私设置中选择是否展示这些信息" :icon="User" v-loading="loading.form">
      <el-form :rules="rule" ref="baseFormRef" :model="baseForm" label-position="top" style="margin:0 20px 20px 20px">
        <el-form-item prop="username" label="用户名">
          <el-input v-model="baseForm.username" maxlength="10"/>
        </el-form-item>
        <el-form-item prop="gender"  label="性别">
          <el-radio-group v-model="baseForm.gender" >
            <el-radio :label="0">男</el-radio>
            <el-radio :label="1">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item prop="phone" label="手机号">
          <el-input maxlength="11" v-model="baseForm.phone"/>
        </el-form-item>
        <el-form-item  prop="qq" label="QQ号">
          <el-input maxlength="13" v-model="baseForm.qq" />
        </el-form-item>
        <el-form-item prop="wx" label="微信号">
          <el-input maxlength="20" v-model="baseForm.wx"/>
        </el-form-item>
        <el-form-item prop="desc" label="个人简介">
          <el-input maxlength="200" v-model="baseForm.desc" type="textarea" :rows="6"/>
        </el-form-item>
        <div>
          <el-button :loading="loading.base" @click="saveDetails" type="success" plain :icon="Select">修改用户信息</el-button>
        </div>
      </el-form>
    </Card>

    <Card style="margin-top:10px" title="电子邮件设置" desc="您可以在这里修改默认绑定的电子邮件地址" :icon="Message">
      <el-form :rules="rule" @validate="onValidate" ref="emailFormRef" :model="emailForm" label-position="top" style="margin:0 10px 10px 10px">
        <el-form-item prop="email" label="电子邮件">
          <el-input v-model="emailForm.email"/>
        </el-form-item>
        <el-form-item>
          <el-row style="width: 100%" :gutter="10" >
            <el-col :span="18">
              <el-input maxlength="6" v-model="emailForm.code" placeholder="请先获取验证码..."></el-input>
            </el-col>
            <el-col :span="6">
              <el-button type="success" @click="sendEmailCode" :disabled="coldTime > 0 || !emailValid" style="width: 100%" plain>
                {{ coldTime > 0 ? `请稍后${coldTime}秒` : '发送验证码' }}
              </el-button>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item>
          <div>
            <el-button type="success" @click="modifyEmail" plain :icon="Refresh">更新电子邮件</el-button>
          </div>
        </el-form-item>
      </el-form>
    </Card>
  </div>
  <div class="settings-card-right">
    <div  style="top: 20px;position: sticky">
      <Card>
        <div style="text-align: center;padding: 5px 15px 0 15px">
          <el-avatar :size="70" :src="store.avatarUrl"/>
          <div style="margin:5px 0">
            <el-upload
                :before-upload="beforeAvatarUpload"
                :show-file-list="false"
                :on-success="uploadAvatarSuccess"
                :action="axios.defaults.baseURL + '/api/images/avatar'"
                :headers="getAccessHeader()"
            >
              <el-button size="small" round>修改图像</el-button>
            </el-upload>
          </div>
          <div style="font-weight: bold">你好,{{store.user.username}}</div>
        </div>
        <el-divider style="margin: 10px 0"></el-divider>
        <div style="font-size: 14px;color:grey;padding: 10px">
          {{desc || "这个用户很懒，什么都没写"}}
        </div>
      </Card>
      <Card style="margin-top: 10px;font-size: 14px">
        <div >账号注册时间：{{registerTime}}</div>
        <div style="color:grey">欢迎加入我们的学习论坛！</div>
      </Card>
    </div>

  </div>
</div>
</template>

<style scoped>
.settings-card-left {
  flex: 1;
  margin: 20px;
}

.settings-card-right {
  width: 400px;
  margin: 20px 30px;
}
</style>