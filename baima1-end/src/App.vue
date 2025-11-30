<script setup>
import {useDark, useToggle} from "@vueuse/core";
//暗黑模式
useDark({
  selector:'html',
  attribute:'class',
  valueDark:'dark',
  valueLight:'light'
})
//
useDark({
  onChanged(dark){useToggle(dark)}
})
//
</script>

<template>
<router-view/>
</template>

<style scoped>
header {
  line-height: 1.5;
}

.logo {
  display: block;
  margin: 0 auto 2rem;
}

@media (min-width: 1024px) {
  header {
    display: flex;
    place-items: center;
  }

  .logo {
    margin: 0 2rem 0 0;
  }

  header .wrapper {
    display: flex;
    place-items: flex-start;
    flex-wrap: wrap;
  }
}
</style>
