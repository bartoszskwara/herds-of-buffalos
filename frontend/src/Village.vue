<template>
  <div id="village">
      <h1>Tu powinna być wiocha</h1>


      <md-button :key="building" v-for="building in buildingsArray" class="md-dense md-raised md-primary" @click.native='setRoute(building.link)'>
        {{building.building.name}}
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
      playerID: 67,
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
      //console.log(data);
      this.buildingsArray = data.body.buildings;
    })
  },
  methods: {
    setRoute(link){
      this.$router.push(link);
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
