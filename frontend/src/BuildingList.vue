<template>
  <div id="buildings">
    <div :key="building.building.key" v-for="building in buildingsArray">
      <md-list class="md-double-line" v-if="building.building.maxLevel != building.currentLevel">
        <building :building="building"></building>
        <md-list-item>
          <div class="md-list-item-text">
            <md-button class="md-raised md-primary" @click="upgrade(building)" :disabled="!building.requirementsMet">Rozbuduj</md-button>
          </div>
        </md-list-item>
      </md-list>
    </div>
    <div :key="building.building.key+1" v-for="building in buildingsArray">
      <md-list class="md-double-line" v-if="building.building.maxLevel == building.currentLevel">
        <building :building="building"></building>
      </md-list>
    </div>

    <md-dialog-alert
      :md-active.sync="alertMaxLevel"
      md-title="Dalsza rozbudowa niemożliwa!"
      :md-content="alertText" />
  </div>
</template>

<script>
import Building from "./Building.vue";

export default {
  components: {Building},
  data() {
    return {
      alertMaxLevel: false,
      alertText: "",
      buildingsArray: [{
        building: Object,
        nextLevel: Number,
        cost: {
          wood: Number,
          clay: Number,
          iron: Number,
        },
        requiredBuildings: [],
        requirementsMet: false,
        }],
      player: Object,
      activeCity: Object,
      val: 10,
    }
  },
  created: function(){
    const axios = require('axios').default;
    axios
      .get('http://localhost:8088/user/current')
      .then(response => (
        this.player = response.data,
        axios
        .get("http://localhost:8088/user/"+this.player.id+"/city/"+this.player.currentCityId+"/building/upgrade")
        .then(response => (
          this.buildingsArray = response.data.content,
          axios
              .get("http://localhost:8088/user/"+this.player.id+"/city/"+this.player.currentCityId)
              .then(response => (this.activeCity = response.data))
        ))
      ))
  },
  methods: {
    upgrade(building) {
      if(building.nextLevel <= building.building.maxLevel){
        if(this.activeCity.resources.wood >= building.cost.wood
        && this.activeCity.resources.clay >= building.cost.clay
        && this.activeCity.resources.iron >= building.cost.iron){
          building.currentLevel++;
          building.nextLevel++;
        }
        else {
          this.alertText = "Brak zasobów na rozbudowę tego budynku!";
          this.alertMaxLevel = true;
        }
      }
      else {
        this.alertText = "Ten budynek został już rozbudowany do najwyższego poziomu!";
        this.alertMaxLevel = true;
      }
    }
  }

}
</script>

<style>
  .md-list {
    width: 320px;
    min-width: 320px;
    max-width: 100%;
    display: inline-block;
    vertical-align: top;
    border: 1px solid rgba(#000, .12);
    float: left;
    margin-left: 20px !important;
    margin-bottom: 20px !important;
  }
</style>
