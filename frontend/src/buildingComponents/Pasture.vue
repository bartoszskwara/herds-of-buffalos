<template>
  <div id="pasture">
    <div class="site">
    <h1>Pastwisko</h1>
      <recruitment-queue></recruitment-queue>
    <div v-if="availableTroops !=null && isTroopAvailable == true">
      <div :key="troop.unit.label" v-for="troop in availableTroops">
        <md-list class="md-double-line troop" v-if="troop.levelsData[0].enabled == true">
          <troop-recruit :troop="troop" :resources="activeCity.resources"></troop-recruit>
        </md-list>
      </div>
    </div>
    <div v-else>
      <p class="info">Aby rekrutować jednostki w tym budynku, zbadaj je najpierw w zbrojowni!</p>
    </div>
    </div>
  </div>
</template>

<script>
import TroopRecruit from "../components/TroopRecruit.vue";
import RecruitmentQueue from "../components/RecruitmentQueue.vue";

export default {
  components: {TroopRecruit, RecruitmentQueue},
  data() {
    return {
      alertMaxLevel: false,
      isTroopAvailable: true,
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
  methods: {
    isAnyTroopAvailable(){
      var len = this.availableTroops.length;
      for(var i = 0 ; i < len ; i++){
        if(this.availableTroops[i].levelsData[0].enabled == true){
          return true;
        }
      }
      return false;
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
          .get("http://localhost:8088/user/"+this.player.id+"/city/"+this.player.currentCityId+"/building/pasture/unit")
          .then(response => (
            this.availableTroops = response.data.content,
            this.isTroopAvailable = this.isAnyTroopAvailable()
          ))
        )).catch((error) => {
          this.availableTroops = null;
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
.info {
  text-align: center;
}
.site {
  display: flex;
  flex-direction: column;
}


</style>
