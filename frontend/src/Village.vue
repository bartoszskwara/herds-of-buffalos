<template>
  <div id="village">
      <h1>{{player.activeVillage}}</h1>
      <div class="villageView">
        <router-link class="buildingImg" v-bind:style="setBuildingPosition(400,70)" to="townhall"><img src="./assets/townhall.png"/></router-link>
        <router-link class="buildingImg" v-bind:style="setBuildingPosition(100,200)" to="armory"><img src="./assets/armory.jpg"/></router-link>
        <router-link class="buildingImg" v-bind:style="setBuildingPosition(600,250)" to="barracks"><img src="./assets/barracks.png"/></router-link>
      </div>
      <md-button :key="building.building.key" v-for="building in buildingsArray" class="md-dense md-raised md-primary" @click.native='setRoute(building.building.key)' :disabled="building.level > 0 ? false : true">
        {{building.building.label}}
      </md-button>
  </div>
</template>

<script>

export default {
  data() {
    return {
      buildingsArray: null,
      player: Object,
    }
  },
  created: function(){
    const axios = require('axios').default;
    axios
      .get('http://localhost:8088/user/current')
      .then(response => (
        this.player = response.data,
        axios
        .get("http://localhost:8088/user/"+this.player.id+"/city/"+this.player.currentCityId+"/building")
        .then(response => (this.buildingsArray = response.data.content))
      ))
  },
  methods: {
    setRoute(link){
      this.$router.push(link);
    },
    setBuildingPosition(x, y){
      var positionStyle = {top: y+'px', left: x+'px'};
      return positionStyle;
    }
  }
}
</script>

<style>
.villageView {
  width: 900px;
  min-width: 900px;
  margin: auto;
  height: 600px;
  outline: 1px solid grey;
  position: relative;
}
.buildingImg {
  width: 200px;
  position: absolute;
}
</style>
