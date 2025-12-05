<script setup>
import {computed, reactive, ref} from "vue";
import {EditPen, Hide, Lock, Message, User, View} from "@element-plus/icons-vue";
import router from "@/router/index.js";
import {ElMessage} from "element-plus";
import {get, post} from "@/net/index.js";

const form = reactive({
  username:'',
  password:'',
  repeat_password:'',
  email:'',
  code:''
})

const formRef = ref()

const validateUsername=(rule,value,callback)=>{
  if(value === '') callback(new Error('请输入用户名'))
  else if(!/^[a-zA-Z0-9_]{3,16}$/.test(form.username)) callback(new Error('用户名只允许字母（大小写）、数字、下划线'))
  else callback()
}

const validatePassword=(rule,value,callback)=>{
  if(value === '') callback(new Error('请输入重复密码'))
  else if(value !== form.password) callback(new Error('两次输入的密码不一致'))
  else callback()
}

const coldTime = ref(0)

const rule = {
  username:[{validator:validateUsername,trigger:['blur','change']}],
  password:[{required:true,message:'请输入密码',trigger:['blur']},
    {min:6,max:20,message: '密码的长度必须在6-20之间',trigger:['blur','change']}],
  repeat_password:[{validator:validatePassword,trigger:['blur','change']}],
  email:[{required:true,message:'请输入电子邮箱',trigger:['blur']},
    {type:'email',message: '请输入正确格式的电子邮箱',trigger:['blur','change']}],
  code:[{required:true,message:'请输入验证码',trigger:['blur']}]
}

const isEmailValid= computed(()=> /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(form.email))



function askCode(){
  if(isEmailValid.value){
    get(`/auth/ask-code?email=${form.email}&type=register`,()=>{
      coldTime.value = 60
      ElMessage.success(`发送成功，${form.email},请注意查收`)
      let timer=setInterval(()=>{
        if(coldTime.value >= 0 ) coldTime.value--
        else {
          clearInterval(timer)
          coldTime.value = 0
          timer = null
        }
      },1000)
    },(url,code,message)=>{
      coldTime.value = 0
      ElMessage.warning(message)
    })
  }else{
    ElMessage.warning('请填写正确格式的电子邮箱')
  }
}

function handlerRegister(){
  formRef.value.validate((valid)=>{
    if(valid){
      post('/auth/register',{...form},()=>{
        ElMessage.success('注册成功')
        router.push('/')
      })
    }else{
      ElMessage.warning('请完整填写表单内容')
    }
  })
}

</script>

<template>
  <div style="margin: 0 20px;text-align: center">
    <div style="margin-top: 100px;">
      <div style="font-size: 30px;font-weight: bold">注册</div>
      <div style="font-size: 16px;color: grey">在注册前，请输入以下信息</div>
    </div>
    <div style="margin-top: 70px;">
      <el-form :model="form" ref="formRef" :rules="rule">
        <el-form-item prop="username">
          <el-input v-model="form.username" maxlength="10" placeholder="用户名/邮箱" type="text">
            <template #prefix>
              <el-icon><User/></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" maxlength="20" minlength="6"  type="password" placeholder="密码">
            <template #prefix>
              <el-icon><Lock/></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="repeat_password">
          <el-input v-model="form.repeat_password" maxlength="20" minlength="6"  type="password" placeholder="重复密码">
            <template #prefix>
              <el-icon><Lock/></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="email">
          <el-input v-model="form.email"  type="text" placeholder="电子邮件">
            <template #prefix>
              <el-icon><Message/></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="code">
        <el-row :gutter="100">
          <el-col :span="12">
            <el-input style="width: 200px" maxlength="6" v-model="form.code" placeholder="验证码">
              <template #prefix>
                <el-icon><EditPen/></el-icon>
              </template>
            </el-input>
          </el-col>
          <el-col :span="12" >
            <el-button style="text-align: left" type="success" :disabled="!isEmailValid || coldTime" @click="askCode" plain>
              {{ coldTime > 0 ? `请稍后 ${coldTime} `:'发送验证码' }}
            </el-button>
          </el-col>
        </el-row>
        </el-form-item>
        <div style="margin-top: 50px">
          <el-button style="width: 300px" type="success" @click="handlerRegister">立即注册</el-button>
          <el-divider>
            <span style="color: grey;font-size: 14px">已有账号？</span>
          </el-divider>
          <el-button style="width: 300px" type="warning" plain @click="router.push('/')">立即登录</el-button>
        </div>

      </el-form>
    </div>
  </div>
</template>

<style scoped>

</style>