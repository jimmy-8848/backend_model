<script setup>

</script>

<template>
<div style="width:100vw;height:100vh;display:flex;overflow: hidden">
<div flex="1" >
  <el-image src="https://cube.elemecdn.com/6/94/4d3ea53c084bad6931a56d5158a48jpeg.jpeg" fit="cover" style="width: 100%;height: 100%"></el-image>
</div>
  <div class="welcome-card">
    <div style="font-size: 30px;font-weight: bold">
      欢迎来到我们的学习平台
    </div>
    <div style="margin-top: 10px">
      在这里你可以学习如何使用Java，如何搭建网站，并且与Java之父密切交流
    </div >
    <div style="margin-top: 5px">
      在这里你可以同性交友，因为都是男的，没有学Java的女生
    </div>
  </div>
  <div class="right-card">
    <router-view v-slot="{Component}">
<!--淡入淡出-->
      <transition name="el-fade-in-linear" mode="out-in">
        <component :is="Component"/>
      </transition>
    </router-view>
  </div>

  </div>
</template>

<style scoped>
.welcome-card{
  position: absolute;
  bottom: 30px;
  left: 30px;
  text-shadow: 0 0 10px black;
  color: white;
}
.right-card{
  width: 400px;
  z-index: 1;
  //
  background: var(--el-bg-color);
}
</style>