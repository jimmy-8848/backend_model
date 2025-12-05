<script setup>
import {logout} from "@/net/index.js";
import router from "@/router/index.js";

function handlerLogout(){
  logout(()=>router.push('/'))
}
</script>

<template>
<el-button type="success" style="width: 300px" @click="handlerLogout">退出登录</el-button>
</template>

<style scoped>

</style>