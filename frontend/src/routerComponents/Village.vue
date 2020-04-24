<template>
  <div id="village">
      <h1>{{player.activeVillage}}</h1>
      <div class="villageView">
        <img src="../assets/townhall.png" class="buildingImg" v-bind:style="setBuildingPosition(400,50)" @click="setRoute('townhall', 'Ratusz')" />
        <img src="../assets/armory.jpg" class="buildingImg" v-bind:style="setBuildingPosition(100,200)" @click="setRoute('armory', 'Zbrojownia')" />
        <img src="../assets/barracks.png" class="buildingImg" v-bind:style="setBuildingPosition(600,200)" @click="setRoute('barracks', 'Koszary')" />
        <img src="../assets/pasture.png" class="buildingImg" v-bind:style="setBuildingPosition(400,300)" @click="setRoute('pasture', 'Pastwisko')" />
      </div>
      <md-button :key="building.building.key" v-for="building in buildingsArray" class="md-dense md-raised md-primary" @click.native='setRoute(building.building.key, building.building.label)' :disabled="building.level > 0 ? false : true">
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
    setRoute(key, label){
      if(key == "barracks" || key == "pasture" || key == "machineFactory" || key == "shipyard"){
        this.$router.push({ name: 'recruitment', params: {buildingId: key, buildingLabel: label }})
      }
      else {
        this.$router.push(key);
      }
    },
    setBuildingPosition(x, y){
      var positionStyle = {top: y+'px', left: x+'px'};
      return positionStyle;
    }
  }
}
</script>

<style scoped>
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
  cursor: pointer;
}
.md-button {
  margin: 5px !important;
}
</style>
