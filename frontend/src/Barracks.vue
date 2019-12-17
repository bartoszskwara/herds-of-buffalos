<template>
  <div id="barracks">
    <div class="rightPanel">
      <player-info :player="player"></player-info>
      <material :materials="materials"></material>
      <troops-panel :troops="troopsArray"></troops-panel>
    </div>
    <div class="leftPanel">
      <h1>Koszary</h1>
    <md-list class="md-double-line troop" :key="troop" v-for="troop in troopsArray">
      <troop v-bind:troop="troop"></troop>

    </md-list>
  </div>
  </div>
</template>

<script>
import Troop from "./Troop.vue";
import troopsArray from "./assets/troops.js";
import Material from "./Material.vue";
import PlayerInfo from "./PlayerInfo.vue";
import TroopsPanel from "./TroopsPanel.vue";

export default {
  components: {Troop, Material, PlayerInfo, TroopsPanel},
  data() {
    return {
      alertMaxLevel: false,
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
  },
  methods: {
  }

}
</script>

<style>
h1 {
  font-family: Sui Generis;
  font-size: 30pt;
  text-align: center;
}
  .md-list.troop {
    width: 520px !important;
    max-width: 100%;
    display: inline-block;
    vertical-align: top;
    border: 1px solid rgba(#000, .12);
    float: left;
    margin-left: 20px !important;
    margin-bottom: 20px !important;
  }
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
