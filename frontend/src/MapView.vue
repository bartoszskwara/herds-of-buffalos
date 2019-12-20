<template>
  <div id="map">
      <div class="mapView">
        <div class="wioska" v-bind:key="village" v-for="village in mapVillages"
           v-bind:style="stylePositionVillage(village)">
           <img :src="setVillageImage(village)" />
           <md-tooltip md-direction="top"><b>Gracz:</b>  {{village.player}}
             <br><b>Wioska:</b>  {{village.name}}
             <br><b>Punkty:</b>  {{village.points}}</md-tooltip>
         </div>
      </div>
  </div>
</template>

<script>
import mapVillages from "./assets/mapVillages.js";

export default {
  data() {
    return {
      mapVillages: mapVillages,
      squareSizeX: 45,
      squareSizeY: 40,
      positionStyle: String,
      imagePath: String,
    }
  },
  methods: {
    stylePositionVillage(village) {
      this.positionStyle =  {top: this.squareSizeY*village.y+'px', left: this.squareSizeX*village.x+'px'};
      return this.positionStyle;
    },
    setVillageImage(village){
      var image = require.context('./assets/', false, /\.png$/)
      if(village.points < 300){
        return image('./wioska1.png')
      }
      else if(village.points >= 300 && village.points < 1000){
        return image('./wioska2.png')
      }
      else if(village.points >= 1000 && village.points < 3000){
        return image('./wioska3.png')
      }
      else if(village.points >= 3000){
        return image('./wioska4.png')
      }
    }
  }
}
</script>

<style>
.mapView {
  width: 1125px;
  margin: auto;
  height: 560px;
  outline: 1px solid black;
  background-color: #a5e8aa;
  position: relative;
}
.wioska {
  position: absolute;
}
.md-tooltip {
    height: auto !important;
}
</style>
