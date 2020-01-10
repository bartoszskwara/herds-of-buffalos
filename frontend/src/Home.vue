<template>
  <div id="app">
    <navigation-bar></navigation-bar>
    <div class="container">
      <div class="leftPanel">
        <router-view></router-view>
      </div>
      <div class="rightPanel">
        <player-info :player="player" :activeCity="activeCity"></player-info>
        <resources-panel :resources="activeCity.resources"></resources-panel>
        <troops-panel :troops="troopsArray" :player="player" @updateTroops="updateTroops($event)"></troops-panel>
      </div>
    </div>
  </div>
</template>

<script>
import NavigationBar from "./NavigationBar.vue";
import ResourcesPanel from "./ResourcesPanel.vue";
import PlayerInfo from "./PlayerInfo.vue";
import TroopsPanel from "./TroopsPanel.vue";

export default {
  components: {NavigationBar, ResourcesPanel, PlayerInfo, TroopsPanel},
  data(){
    return {
      troopsArray: [
        {
          unit: {
                key: String,
                label: String,
                building: String
              },
          levelsData: [
            {
              level: Number,
              amountInCity: Number
            },
          ]}],
      player: {},
      activeCity: {resources: {}},
    }
  },
  updateTroops(list){
    this.troopsArray = list;
  },
  mounted: function(){
    const axios = require('axios').default;
      axios
      .get("http://localhost:8088/user/current")
      .then(response => (
        this.player = response.data,
        axios
        .get("http://localhost:8088/user/"+this.player.id+"/city/"+this.player.currentCityId)
        .then(response => (
          this.activeCity = response.data,
          axios
          .get("http://localhost:8088/user/"+this.player.id+"/city/"+this.player.currentCityId+"/unit")
          .then(response => (this.troopsArray = response.data.content))
        ))
      )).catch((error) => {
        alert(error.response.data.message);
      })
  },
}
</script>

<style>
html {
  overflow-y: scroll;
  overflow-x: hidden;
}
h1 {
  font-family: Sui Generis;
  font-size: 30pt;
  text-align: center;
}
.container {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}
.leftPanel {
  flex: 1;
}
.md-icon.chevron {
  width: 40px !important;
}

.rightPanel {

  padding-left: 20px;
  min-width: 340px;
  border-left: 1px solid grey;
}
</style>
