<template>
  <div id="townhall">

      <h1>Ratusz</h1>
      <div v-if="availableTroops !=null">
        <md-list class="md-double-line troop" :key="troop.unit.key" v-for="troop in availableTroops">
          <troop-recruit v-if="troop.levelsData[0].enabled == true" :troop="troop" :resources="activeCity.resources"></troop-recruit>
        </md-list>
      </div>
      <building-list></building-list>

  </div>
</template>

<script>
import BuildingList from "../components/BuildingList.vue";
import TroopRecruit from "../components/TroopRecruit.vue";

export default {
  components: {BuildingList, TroopRecruit},
  data() {
    return {
      player: {},
      activeCity: {},
      availableTroops: [
                          {
                            unit: {
                              key: String,
                              label: String,
                              building: String
                            },
                            maxLevel: Number,
                            levelsData: [
                              {
                                level: Number,
                                amountInCity: Number,
                                skills: {
                                  attack: Number,
                                  defense: Number,
                                  health: Number
                                },
                                recruitmentCost: {
                                  wood: Number,
                                  clay: Number,
                                  iron: Number
                                },
                                enabled: Boolean
                              }
                            ]
                          }
                        ],
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
          .get("http://localhost:8088/user/"+this.player.id+"/city/"+this.player.currentCityId+"/building/townhall/unit")
          .then(response => (this.availableTroops = response.data.content))
        )).catch((error) => {
          this.availableTroops = null;
          alert(error.response.data.message);
        })
  }
}
</script>

<style scoped>
.md-list.troop {
  width: 95% !important;
  min-width: 720px;
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
</style>
