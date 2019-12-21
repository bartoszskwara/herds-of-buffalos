<template>
  <div id="map">
      <h1>Mapa</h1>
      <map-view></map-view>
  </div>
</template>

<script>
import mapVillages from "./assets/mapVillages.js";
import MapView from "./MapView.vue";

export default {
  components: {MapView},
  data() {
    return {
      mapVillages: mapVillages,
      squareSize: 40,
      obj: String,
    }
  },
  methods: {
    stylePositionVillage(village) {
      this.obj =  {top: this.squareSize*village.y+'px', left: this.squareSize*village.x+'px'};
      return this.obj;
    }
  }
}
</script>

<style>

</style>
