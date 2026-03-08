<script setup>
defineProps({
  icon: Object,
  title: String,
  desc: String
})
</script>

<template>
<div class="card">
  <div class="card-header" v-if="title">
    <div>
      <el-icon style="translate: 0 4px;margin-right: 3px">
        <Component :is="icon"/>
      </el-icon>
      {{title}}
    </div>
    <div>
      {{desc}}
    </div>
  </div>
  <slot/>
</div>
</template>

<style lang="less" scoped>
.card {
  min-height: 20px;
  border:solid 1px var(--el-border-color);
  border-radius: 5px;
  padding: 20px;
  background-color: var(--el-bg-color);
  box-sizing: border-box;
}
.card-header {
  border-bottom: solid 1px var(--el-border-color);
  padding-bottom: 10px;
  margin-bottom: 20px;

  &>:first-child {
    font-size: 18px;
    font-weight: bold;
  }

  &>:last-child {
    font-size: 13px;
    color: grey;
  }
}
</style>