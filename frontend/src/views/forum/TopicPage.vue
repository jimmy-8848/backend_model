<script setup>

import LightCard from "@/views/components/LightCard.vue";
import {Calendar, CollectionTag} from "@element-plus/icons-vue";
</script>

<template>
<div style="display: flex;margin: 20px auto;gap: 20px;max-width: 900px">
  <div style="flex: 1">
    <LightCard style="height: 2000px">

    </LightCard>
  </div>
  <div style="width: 300px">
    <div style="position: sticky;top: 20px">
      <LightCard>
        <div style="font-weight: bold">
          <el-icon style="translate: 0 2px"><CollectionTag/></el-icon>
          论坛公告
        </div>
        <el-divider style="margin: 10px 0"/>
        <div style="font-size: 14px;color: grey">
          asdfasdfsadfasdfasdf
        </div>
      </LightCard>

      <LightCard style="margin-top: 20px">
        <div style="font-weight: bold">
          <el-icon style="translate: 0 2px"><Calendar/></el-icon>
          天气
        </div>
      </LightCard>
    </div>

  </div>
</div>
</template>

<style scoped>

</style>