<script setup>

import Card from "@/views/components/Card.vue";
import {Setting, Switch} from "@element-plus/icons-vue";
import {reactive, ref} from "vue";
import {post} from "@/net/index.js";
import {ElMessage} from "element-plus";
const validatePassword=(rule,value,callback)=>{
  if(value === '') callback(new Error('请输入重复密码'))
  else if(value !== form.new_password) callback(new Error('两次输入的密码不一致'))
  else callback()
}
const passwordValid = ref(false)
const onValidate = (prop,isValid) => {
  passwordValid.value = isValid
}
const formRef = ref()
const form = reactive({
  password: '',
  new_password: '',
  new_password_repeat: ''
})

function changePassword(){
  formRef.value.validate((isValid)=>{
    if(isValid){
      post("/api/user/change-password",form,()=>{
        ElMessage.success('密码修改成功')
        formRef.value.resetFields()
      },(url,code,message)=>{
        ElMessage.warning(message)
      })
    }
  })
}

const rule = {
  password:[{required:true,message:'请输入密码',trigger:['blur']},
    {min:6,max:20,message: '密码的长度必须在6-20之间',trigger:['blur','change']}],
  new_password:[{required:true,message:'请输入密码',trigger:['blur']},
    {min:6,max:20,message: '密码的长度必须在6-20之间',trigger:['blur','change']}],
  new_password_repeat:[{validator: validatePassword,trigger:['blur','change']},
    {required:true,message:'请输入密码',trigger:['blur']}
  ]
}


</script>

<template>
  <div style="max-width: 600px;margin: auto">
    <Card style="margin: 20px 0" title="隐私设置" desc="在这里设置哪些内容可以被其他人看到，请各位小伙伴注重自己的隐私" :icon="Setting">
      <div class="checkBox" >
        <el-checkbox>公开展示我的手机号</el-checkbox>
        <el-checkbox>公开展示我的电子邮箱地址</el-checkbox>
        <el-checkbox>公开展示我的微信号</el-checkbox>
        <el-checkbox>公开展示我的QQ号</el-checkbox>
        <el-checkbox>公开展示我的性别</el-checkbox>
      </div>
    </Card>
    <Card style="margin: 20px 0"  title="修改密码" desc="修改密码请在这里操作，请务必牢记您的密码" :icon="Setting">
      <el-form :rules="rule" ref="formRef" :model="form" label-width="100" style="margin-top: 20px">
        <el-form-item prop="password" label="当前密码">
          <el-input type="password" v-model="form.password" :prefix-icon="Lock" placeholder="当前密码" maxlength="20"/>
        </el-form-item>
        <el-form-item prop="new_password" label="新密码">
          <el-input type="password" v-model="form.new_password" :prefix-icon="Lock" placeholder="新密码" maxlength="20"/>
        </el-form-item>
        <el-form-item prop="new_password_repeat" label="重复新密码">
          <el-input type="password" v-model="form.new_password_repeat" :prefix-icon="Lock" placeholder="重复输入新密码" maxlength="20"/>
        </el-form-item>
        <div style="text-align: center">
          <el-button @click="changePassword" type="success" :icon="Switch">立即重置密码</el-button>
        </div>

      </el-form>
    </Card>
  </div>
</template>

<style scoped>
.checkBox {
  display: flex;
  flex-direction: column;
  margin: 10px;
}
</style>