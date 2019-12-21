<template>
  <div id="townhall">

      <h1>Ratusz</h1>
      <building-list :buildings="buildingsArray"></building-list>

  </div>
</template>

<script>
import BuildingList from "./BuildingList.vue";
import buildingsArray from "./assets/buildings.js";

export default {
  components: {BuildingList},
  data() {
    return {
      buildingsArray: buildingsArray,
    }
  }
}
</script>

<style scoped>
</style>
