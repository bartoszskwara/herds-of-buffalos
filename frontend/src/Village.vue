<template>
  <div id="village">

    <div class="rightPanel">
      <player-info :player="player"></player-info>
      <material :materials="materials"></material>
      <troops :troops="troops"></troops>
    </div>
    <div class="leftPanel">
      <building-list :village="village"></building-list>
    </div>

  </div>
</template>

<script>
import BuildingList from "./BuildingList.vue";
import Material from "./Material.vue";
import PlayerInfo from "./PlayerInfo.vue";
import Troops from "./Troops.vue";

export default {
  components: {BuildingList, Material, PlayerInfo, Troops},
  data() {
    return {
      village: [
        {
          name: "Ratusz",
          level: 4,
          maxLevel: 20,
          price: {
            wood: 20000,
            clay: 3000,
            iron: 500,
          }
        },
        {
          name: "Pastwisko bawołów",
          level: 1,
          maxLevel: 6,
          price: {
            wood: 4,
            clay: 20,
            iron: 60,
          }
        },
        {
          name: "Koszary",
          level: 1,
          maxLevel: 15,
          price: {
            wood: 460,
            clay: 200,
            iron: 600,
          }
        },
        {
          name: "Mur obronny",
          level: 1,
          maxLevel: 10,
          price: {
            wood: 4,
            clay: 10,
            iron: 20,
          }
        },
      ],
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
