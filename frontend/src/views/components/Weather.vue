<script setup>

</script>

<template>
  <div style="display: flex;justify-content: space-between;margin: 10px 20px">
    <div style="font-size: 45px;color: grey">
      <i class="qi-301-fill"></i>
    </div>
    <div style="font-weight: bold">
      <div style="font-size: 25px">20℃</div>
      <div style="font-size: 15px">多云</div>
    </div>
    <div style="margin-top: 13px">
      <div style="font-size: 15px">中国 广东省</div>
      <div style="color: grey;font-size: 13px">广州市 从化区</div>
    </div>
  </div>
  <el-divider style="margin: 10px 0"/>
  <div style="display: grid;justify-content: space-between;grid-template-columns: repeat(5,1fr)">
    <div v-for="item in 5" style="text-align: center">
      <div style="font-size: 13px;">{{item}}时</div>
      <div style="font-size: 23px;"><i class="qi-301-fill"></i></div>
      <div style="font-size: 12px;">20℃</div>
    </div>
  </div>
</template>

<style scoped>

</style>