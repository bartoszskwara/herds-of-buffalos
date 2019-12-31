<template>
  <div id="barracks">
      <h1>Zbrojownia</h1>
    <md-list class="md-double-line troop" :key="troop.name" v-for="troop in troopsArray">
      <troop-armory :troop="troop"></troop-armory>
    </md-list>
  </div>
</template>

<script>
import TroopArmory from "./TroopArmory.vue";
import troopsArray from "./assets/troops.js";

export default {
  components: {TroopArmory},
  data() {
    return {
      alertMaxLevel: false,
      troopsArray: troopsArray,
    }
  },
  methods: {
  },
  // mounted: function(){
  //   const axios = require('axios').default;
  //
  //   axios
  //     .get("http://localhost:8088/user/current")
  //     .then(response => (
  //       this.player = response.data,
  //       axios
  //       .get("http://localhost:8088/user/"+this.player.id+"/city/"+this.player.currentCityId)
  //       .then(response => (
  //         this.activeCity = response.data
  //       )),
  //       axios
  //         .get("http://localhost:8088/user/"+this.player.id+"/city/"+this.player.currentCityId+"/building/barracks/unit")
  //         .then(response => (this.availableTroops = response.data.content))
  //       )).catch((error) => {
  //         this.availableTroops = null;
  //         alert(error.response.data.message);
  //       })
  // }

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
  border: 1px solid #f7f7f7;
  float: left;
  margin-left: 20px !important;
  margin-bottom: 20px !important;
  padding-top: 0 !important;
  padding-bottom: 0 !important;
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
