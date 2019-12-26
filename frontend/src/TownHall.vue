<template>
  <div id="townhall">

      <h1>Ratusz</h1>
      <building-list></building-list>

  </div>
</template>

<script>
import BuildingList from "./BuildingList.vue";

export default {
  components: {BuildingList},
  data() {
    return {
    }
  }
}
</script>

<style scoped>
</style>
