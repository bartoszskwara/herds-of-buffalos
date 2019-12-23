<template>
  <div id="village">
      <h1>{{player.activeVillage}}</h1>
      <div class="villageView">
        <router-link class="buildingImg" v-bind:style="setVillagePosition(400,70)" to="townhall"><img src="./assets/townhall.png"/></router-link>
        <router-link class="buildingImg" v-bind:style="setVillagePosition(600,250)" to="armory"><img src="./assets/armory.jpg"/></router-link>
      </div>

      <md-button :key="building.name" v-for="building in buildingsArray" class="md-dense md-raised md-primary" @click.native='setRoute(building.building.key)'>
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
      buildingsArray: [],
      playerID: 105,
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
    this.$http.get("http://localhost:8088/user/"+this.playerID+"/building").then(function(data) {
      this.buildingsArray = data.body.content;
    })
  },
  methods: {
    setRoute(link){
      this.$router.push(link);
    },
    setVillagePosition(x, y){
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
