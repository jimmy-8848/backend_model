<script setup>

import {Hide, Lock, User, View} from "@element-plus/icons-vue";
import {reactive, ref} from "vue";
import {login} from "@/net/index.js";
import router from "@/router/index.js";
import {ElMessage} from "element-plus";

const form = reactive({
      username:'',
      password:'',
      remember: false
    }
)

const showPassword = ref(false)

const formRef = ref()

const rule = {
  username:[{required:true,message:'请输入用户名'}],
  password:[{required:true,message:'请输入密码'}]
}

function handlerLogin(){
  formRef.value.validate((valid)=>{
    if(valid)   login(form.username,form.password,form.remember,()=>router.push('/index'))
    else ElMessage.warning('请完整填写表单内容')
  })

}
</script>

<template>

  <div style="margin: 0 20px;text-align: center">
    <div style="margin-top: 100px;">
      <div style="font-size: 30px;font-weight: bold">登录</div>
      <div style="font-size: 16px;color: grey">在登录前，请输入以下信息</div>
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
          <el-input v-model="form.password" maxlength="20" minlength="6"  :type="showPassword?'text':'password'" placeholder="密码">
            <template #prefix>
              <el-icon><Lock/></el-icon>
            </template>
            <template #suffix>
              <el-icon @click="showPassword = !showPassword" style="cursor: pointer">
                <Component :is="showPassword?View:Hide"/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-row :gutter="10">
          <el-col :span="12" style="text-align: left">
            <el-checkbox v-model="form.remember">记住我</el-checkbox>
          </el-col>
          <el-col :span="12" style="text-align: right">
            <el-link @click="router.push('/reset')">忘记密码？</el-link>
          </el-col>
        </el-row>
        <div style="margin-top: 50px">
          <el-button style="width: 300px" type="success" @click="handlerLogin">立即登录</el-button>
          <el-divider>
            <span style="color: grey;font-size: 14px">没有账号？</span>
          </el-divider>
          <el-button style="width: 300px" type="warning" plain @click="router.push('/register')">立即注册</el-button>
        </div>

      </el-form>
    </div>
  </div>
</template>

<style scoped>

</style>