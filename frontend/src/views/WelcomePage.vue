<script setup>
import {reactive, ref} from "vue";

</script>

<template>
  <div style="width: 100vw;height: 100vh;display: flex;overflow: hidden">
    <div flex="1">
      <el-image style="width: 100%;height:100%" src="src/assets/bg.jpg" fit="cover"/>
    </div>
    <div class="right-card">
     <router-view v-slot="{Component}">
       <transition name="el-fade-in-linear" mode="out-in">
         <Component :is="Component"/>
       </transition>
     </router-view>
    </div>
  </div>
</template>

<style scoped>
.right-card{
  width: 400px;
  z-index: 1;
}
</style>