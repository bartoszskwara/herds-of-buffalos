<template>
  <div id="village">
      <h1>{{player.activeVillage}}</h1>
      <div class="villageView">
        <router-link class="buildingImg" v-bind:style="setBuildingPosition(400,70)" to="townhall"><img src="./assets/townhall.png"/></router-link>
        <router-link class="buildingImg" v-bind:style="setBuildingPosition(100,200)" to="armory"><img src="./assets/armory.jpg"/></router-link>
        <router-link class="buildingImg" v-bind:style="setBuildingPosition(600,250)" to="barracks"><img src="./assets/barracks.png"/></router-link>
      </div>
      <md-button :key="building.name" v-for="building in buildingsArray" class="md-dense md-raised md-primary" @click.native='setRoute(building.building.key)' :disabled="building.level > 0 ? false : true">
        {{building.building.label}}
      </md-button>
  </div>
</template>

<script>

//import buildingsArray from "./assets/buildings.js";

export default {
  data() {
    return {
      //buildingsArray: buildingsArray,
      buildingsArray: null,
      userArray: null,
      materials: {
        wood: 3298,
        clay: 1290,
        iron: 333
      },
      player: {
        nick: "Bawół Świeczka",
        score: 12123,
        ranking: 3,
        villageCount: 1,
        activeVillage: "Miasto Bawol"
      },
    }
  },
  created: function(){
    const axios = require('axios').default;
    axios
      .get('http://localhost:8088/user')
      .then(response => (
        this.userArray = response.data.content,
        axios
        .get("http://localhost:8088/user/"+this.userArray[0].id+"/building")
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
.leftPanel {
  float: left;
  width: 76%;
}
.rightPanel {
  float: right;
  width: 24%;
  border-left: 1px solid grey;
  padding-right: 20px;
}
</style>
