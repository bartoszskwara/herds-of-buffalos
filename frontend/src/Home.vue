<template>
  <div id="app">
    <navigation-bar></navigation-bar>
    <div class="rightPanel">
      <player-info :player="player"></player-info>
      <material-panel :materials="materials"></material-panel>
      <troops-panel :troops="troopsArray"></troops-panel>
    </div>
    <div class="leftPanel">
      <router-view></router-view>
    </div>
  </div>
</template>

<script>
import NavigationBar from "./NavigationBar.vue";
import MaterialPanel from "./MaterialPanel.vue";
import PlayerInfo from "./PlayerInfo.vue";
import TroopsPanel from "./TroopsPanel.vue";
import troopsArray from "./assets/troops.js";

export default {
  components: {NavigationBar, MaterialPanel, PlayerInfo, TroopsPanel},
  data(){
    return {
      troopsArray: troopsArray,
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
  }
}
</script>

<style>
html {
  overflow-y: scroll;
}
h1 {
  font-family: Sui Generis;
  font-size: 30pt;
  text-align: center;
}
</style>
