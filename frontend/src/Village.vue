<template>
  <div id="village">
      <h1>Tu powinna być wiocha</h1>

      <md-button :key="building" v-for="building in buildingsArray" class="md-dense md-raised md-primary" @click.native='setRoute(building.link)'>
        {{building.name}}
      </md-button>


  </div>
</template>

<script>

import buildingsArray from "./assets/buildings.js";

export default {
  data() {
    return {
      buildingsArray: buildingsArray,
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
      troops: [
        {
          name: "Pikinier",
          count: 400,
        },
        {
          name: "Miecznik",
          count: 200,
        },
        {
          name: "Konny Bawół",
          count: 50,
        },
        {
          name: "Bawół na smoku",
          count: 15,
        },
        {
          name: "Działko plazmowe",
          count: 0,
        },
      ],
    }
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
