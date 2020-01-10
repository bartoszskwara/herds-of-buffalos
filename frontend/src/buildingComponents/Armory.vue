<template>
  <div id="barracks">
    <h1>Zbrojownia</h1>
    <div class="armory">
      <md-list class="md-double-line troop" :key="troop.unit.key" v-for="troop in troopUpgrades" :class="opacityClass(troop)">
        <troop-armory :troop="troop"></troop-armory>
      </md-list>
    </div>
  </div>
</template>

<script>
import TroopArmory from "../components/TroopArmory.vue";

export default {
  components: {TroopArmory},
  data() {
    return {
      alertMaxLevel: false,
      player: {},
      activeCity: {},
      troopUpgrades: Array,
    }
  },
  methods: {
    opacityClass(troop){
      if(troop.levelsData[0].upgradeRequirementsMet == false && troop.levelsData[0].enabled == false){
        return "disabled";
      }
      return "";
    }
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
          this.activeCity = response.data
        )),
        axios
          .get("http://localhost:8088/user/"+this.player.id+"/city/"+this.player.currentCityId+"/unit/upgrade")
          .then(response => (this.troopUpgrades = response.data.content))
        )).catch((error) => {
          this.troopUpgrades = null;
          alert(error.response.data.message);
        })
  }

}
</script>

<style scoped>
h1 {
  font-family: Sui Generis;
  font-size: 30pt;
  text-align: center;
}
.md-list.troop {
  width: 560px !important;
  min-width: 420px;
  max-width: 100%;
  display: inline-block;
  vertical-align: top;
  float: left;
  margin-left: 20px !important;
  margin-bottom: 20px !important;
  padding-top: 0 !important;
  padding-bottom: 0 !important;
}
.armory {

}
.disabled {
  opacity: 30%;
}
</style>
