<script setup>
import {reactive, ref} from "vue";
import {Hide, Lock, User, View} from "@element-plus/icons-vue";
import {ElMessage} from "element-plus";
import {login} from "@/net/index.js";
import router from "@/router/router.js";

const formRef = ref();

const showPassword = ref(false)

const form = reactive({
  username:'',
  password:'',
  remember:false
})
const rule = {
  username:[{required:true,message:'请输入用户名'}],
  password:[{required:true,message:'请输入密码',trigger:'blur'}]
}
const handleLogin = ()=>{
  formRef.value.validate((valid)=>{
    if(valid) login(form.username,form.password,form.remember,()=>router.push('/index'))
    else ElMessage.error("请输入用户名和密码")
  })
}
</script>

<template>
  <div style="text-align: center;margin: 0 20px">
    <div style="margin-top: 150px">
      <div style="font-weight: bold;font-size: 25px">登录</div>
      <div style="font-size: 14px;color: grey">在进入系统之前，请先输入用户名和密码进行登录</div>
    </div>
    <div style="margin-top: 50px">
      <el-form ref="formRef" :model="form" :rules="rule">
        <el-form-item prop="username">
          <el-input v-model="form.username" maxlength="20" type="text" placeholder="用户名/邮箱">
            <template #prefix>
              <el-icon><User/></el-icon>
            </template>
          </el-input>

        </el-form-item>
        <el-form-item prop="password">
          <el-input :type="showPassword?'text':'password'"  v-model="form.password" maxlength="20"  placeholder="密码">
            <template #prefix>
              <el-icon><Lock /></el-icon>
            </template>
            <template #suffix>
              <el-icon style="cursor: pointer" @click="showPassword = !showPassword">
                <component :is="showPassword?View:Hide"/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <!--      Basic基础布局 -> layout布局-->
        <el-row>
          <el-col :span="12" style="text-align: left">
            <el-form-item>
              <el-checkbox v-model="form.remember" label="记住我"></el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="12" style="text-align: right">
            <el-link @click="router.push('/reset')">忘记密码？</el-link>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <div style="margin-top: 40px">
      <el-button style="width: 270px" type="success" @click="handleLogin">立即登录</el-button>
    </div>
    <div>
      <el-divider>
        <span style="font-size: 13px;color: grey">没有账号？</span>
      </el-divider>
      <div>
        <!--plain半透明-->
<!--        -->
        <el-button @click="router.push('register')" style="width: 270px" type="warning" plain>立即注册</el-button>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>