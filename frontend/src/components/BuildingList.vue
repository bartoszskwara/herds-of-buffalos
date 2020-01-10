<template>
  <div id="buildings">
    <md-toolbar :md-elevation="1">
        <span class="md-title">Rozbudowa</span>
      </md-toolbar>
      <md-list class="md-double-line md-dense">
        <div :key="building.building.key" v-for="building in buildingsArray">
        <md-list-item v-if="building.building.maxLevel != building.currentLevel">
          <div class="md-list-item-text" style="flex-grow:1">
            <span><a class="buildingLabel" @click='setRoute(building.building.key)'>{{building.building.label}}</a></span>
            <span>Poziom {{building.currentLevel}}</span>
          </div>
          <div class="md-list-item-text" style="flex-grow:1">
            <span><md-icon class="mat" :md-src="require('../assets/wood.svg')" /></span>
            <span>{{building.cost.wood}}</span>
          </div>
          <div class="md-list-item-text" style="flex-grow:1">
            <span><md-icon class="mat" :md-src="require('../assets/clay.svg')" /></span>
            <span>{{building.cost.clay}}</span>
          </div>
          <div class="md-list-item-text" style="flex-grow:1">
            <span><md-icon class="mat" :md-src="require('../assets/iron.svg')" /></span>
            <span>{{building.cost.iron}}</span>
          </div>
          <div class="md-list-item-text" style="flex-grow:1">
            <span><md-icon class="mat" :md-src="require('../assets/time.svg')" /></span>
            <span>1:29:26</span>
          </div>
          <div class="md-list-item-text" style="flex-grow:1">
            <md-button class="md-raised md-primary" @click="upgrade(building)" :disabled="!building.requirementsMet">Rozbuduj</md-button>
          </div>
        </md-list-item>
        <md-list-item v-else>
          <div class="md-list-item-text" style="flex-grow:1">
            <span><a class="buildingLabel" @click='setRoute(building.building.key)'>{{building.building.label}}</a></span>
            <span>Poziom {{building.currentLevel}}</span>
          </div>
          <div class="md-list-item-text" style="flex-grow:5">
            <span>Osiągnięto maksymalny poziom rozbudowy tego budynku.</span>
          </div>

        </md-list-item>
      </div>
      </md-list>

    <md-dialog-alert
      :md-active.sync="alertMaxLevel"
      md-title="Dalsza rozbudowa niemożliwa!"
      :md-content="alertText" />
  </div>
</template>

<script>

export default {
  components: {},
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
    },
    setRoute(link){
      this.$router.push(link);
    }
  }

}
</script>

<style>
  .md-list {
    width: auto;
    min-width: 320px;
    max-width: 100%;
    display: inline-block;
    vertical-align: top;
    border: 1px solid rgba(#000, .12);
  }
  .md-list-item-text {
    text-align: center;
  }
  .md-list-item {
    border-bottom: 1px solid #e8e8e8 !important;
  }
  .md-icon {
    width: 0 !important;
    min-height: 110%;
  }
  .mat {
    margin: auto !important;
    margin-bottom: 5px !important;
  }
  a.buildingLabel {
    color: rgba(0,0,0,0.87) !important;
  }
  a.buildingLabel:hover {
    cursor: pointer;
    color: #852800 !important;
    text-decoration: none !important;
  }
</style>
