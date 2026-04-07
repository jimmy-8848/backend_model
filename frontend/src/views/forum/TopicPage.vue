<script setup>

import LightCard from "@/views/components/LightCard.vue";
import {Calendar, CollectionTag, EditPen, Link} from "@element-plus/icons-vue";
import Weather from "@/views/components/Weather.vue";
import {computed} from "vue";

const today = computed(()=>{
  const date = new Date()
  return `${date.getFullYear()} 年 ${date.getMonth()} 月 ${date.getDay()} 日`
})
</script>

<template>
<div style="display: flex;margin: 20px auto;gap: 20px;max-width: 900px">
  <div style="flex: 1">
    <LightCard>
      <div class="create-topic">
        <el-icon style="translate: 0 13px"><EditPen/></el-icon>
        点击发表主题...
      </div>
    </LightCard>
    <LightCard style="height: 30px;margin-top: 10px"></LightCard>
    <div style="margin-top: 10px;display: flex;flex-direction: column;gap: 10px">
      <LightCard style="height: 100px" v-for="item in 10">
        <div></div>
      </LightCard>
    </div>
  </div>
  <div style="width: 300px">
    <div style="position: sticky;top: 20px">
<!--论坛公告模块-->
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
<!--天气模块-->
      <LightCard style="margin-top: 20px">
        <div style="font-weight: bold">
          <el-icon style="translate: 0 2px"><Calendar/></el-icon>
          天气
        </div>
        <Weather/>
      </LightCard>
<!--时间和IP地址模块-->
      <LightCard style="margin: 10px 0">
        <div class="info-text">
          <div>日期</div>
          <div>{{today}}</div>
        </div>
        <div class="info-text">
          <div>IP地址</div>
          <div>127.0.0.1</div>
        </div>
      </LightCard>
      <div style="font-size: 15px;color: grey">
      <el-icon style="translate: 0 2px"><Link/></el-icon>
      友情链接
    </div>
      <el-divider style="margin: 10px 0"/>
      <div style="display: grid;grid-template-columns: repeat(2,1fr);grid-gap: 10px;">
        <div class="friend-link"><el-image style="height: 100%" src="https://ts4.tc.mm.bing.net/th/id/OIP-C.GHPQTvkT3TqCadzvWvCZFgHaGs?rs=1&pid=ImgDetMain&o=7&rm=3"/></div>
        <div class="friend-link"><el-image style="height: 100%" src="https://www.itbaima.cn/image/welcome/logo/light.webp"/></div>
        <div class="friend-link"><el-image style="height: 100%" src="https://th.bing.com/th/id/ODF.WgLLc7GdrgvVHjprJsaodQ?w=32&h=32&qlt=90&pcl=fffffc&r=0&o=6&pid=1.2"/></div>
      </div>
    </div>

  </div>
</div>
</template>

<style scoped>
.info-text {
  display: flex;
  justify-content: space-between;
  color: grey;
  font-size: 14px;
}

.friend-link {
  border-radius: 5px;
  overflow: hidden;
}

.create-topic {
  display: flex;
  background-color: #efefef;
  color: grey;
  height: 40px;
  line-height: 40px;
  padding: 0 10px;
  border-radius: 5px;
  &:hover {
    cursor: pointer;
  }
}

</style>