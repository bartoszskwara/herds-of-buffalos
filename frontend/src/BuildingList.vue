<template>
  <div id="buildings">
    <md-list class="md-double-line" :key="building.building.key" v-for="building in buildingsArray">
      <building :building="building"></building>

      <md-list-item>
        <div class="md-list-item-text">
          <md-button class="md-raised md-primary" @click="upgrade(building)">Rozbuduj</md-button>
        </div>
      </md-list-item>
    </md-list>

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
        cost: Object,
        requiredBuildings: [],
        requirementsMet: false,
        }],
      userArray: null,
      player: {resources: {}},
      val: 10,
    }
  },
  created: function(){
    const axios = require('axios').default;
    axios
      .get('http://localhost:8088/user')
      .then(response => (
        this.userArray = response.data.content,
        axios
        .get("http://localhost:8088/user/"+this.userArray[0].id+"/building/upgrade")
        .then(response => (
          this.buildingsArray = response.data.content,
          axios
              .get("http://localhost:8088/user/"+this.userArray[0].id)
              .then(response => (this.player = response.data))
        ))
      ))
  },
  methods: {
    upgrade(building) {
      //tu by mogło być zawołanie http o ilość surków
      // this.$http.get("http://localhost:8088/user/"+this.userArray[0].id+"/materials").then(function(data) {
      //   this.materialsData = data.body.content;
      // })

      if(building.nextLevel <= building.building.maxLevel){
        if(this.player.resources.wood >= building.cost.wood
        && this.player.resources.clay >= building.cost.clay
        && this.player.resources.iron >= building.cost.iron){
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
