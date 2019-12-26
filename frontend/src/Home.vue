<template>
  <div id="app">

    <navigation-bar></navigation-bar>
    <div class="container">
      <div class="leftPanel">
        <router-view></router-view>
      </div>
      <div class="rightPanel">
        <player-info :player="player"></player-info>
        <resources-panel :resources="player.resources"></resources-panel>
        <troops-panel :troops="troopsArray"></troops-panel>
      </div>
    </div>
  </div>
</template>

<script>
import NavigationBar from "./NavigationBar.vue";
import ResourcesPanel from "./ResourcesPanel.vue";
import PlayerInfo from "./PlayerInfo.vue";
import TroopsPanel from "./TroopsPanel.vue";
import troopsArray from "./assets/troops.js";

export default {
  components: {NavigationBar, ResourcesPanel, PlayerInfo, TroopsPanel},
  data(){
    return {
      troopsArray: troopsArray,
      userArray: [],
      player: {resources: {}},
    }
  },
  mounted: function(){
    const axios = require('axios').default;
    axios
      .get('http://localhost:8088/user')
      .then(response => (
        this.userArray = response.data.content,
        axios
        .get("http://localhost:8088/user/"+this.userArray[0].id)
        .then(response => (this.player = response.data))
      ))
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


.rightPanel {

  padding-left: 20px;
  min-width: 340px;
  border-left: 1px solid grey;
}
</style>
