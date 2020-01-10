<template>
  <div id="sendTroops">

      <h1>Wyślij wojska</h1>
      <div class="upperPanel">
        <destination :village="village"></destination>
        <div class="btnPanel">
          <md-button class="md-raised md-accent button">Wyślij atak</md-button>
          <md-button class="md-raised md-primary button">Wyślij wsparcie</md-button>
        </div>
      </div>
      <div class="allUnits">
        <choose-troops :troops="infantry" header="Piechota" v-if="infantry != null"></choose-troops>
        <choose-troops :troops="cavalry" header="Kawaleria" v-if="cavalry != null"></choose-troops>
        <choose-troops :troops="vehicles" header="Pojazdy" v-if="vehicles != null"></choose-troops>
        <choose-troops :troops="ships" header="Statki" v-if="ships != null"></choose-troops>
        <choose-troops :troops="others" header="Inne" v-if="others != null"></choose-troops>
      </div>
  </div>
</template>

<script>
import Destination from "./Destination.vue";
import ChooseTroops from "./ChooseTroops.vue";

export default {
  components: {Destination, ChooseTroops},
  data() {
    return {
        player: Object,
        availableTroops: [
          {
            unit: {
                  key: String,
                  label: String,
                  building: String
                },
            levelsData: [
              {
                level: Number,
                amountInCity: Number
              },
            ]}],
      infantry: [],
      cavalry: [],
      vehicles: [],
      ships: [],
      others: [],
    }
  },
  props: {
    village: Object,
  },
  methods: {

    divideTroops(){
      var len = this.availableTroops.length;
      var tab = [0,0,0,0,0];
      for(var i = 0 ; i < len ; i++){
        var lvlSize = this.availableTroops[i].levelsData.length;
        for(var k = 0 ; k < lvlSize ; k++){
          this.availableTroops[i].levelsData[k]["chosen"] = null;
        }

        if(this.availableTroops[i].unit.building == "barracks" || this.availableTroops[i].unit.building == "townhall"){
          this.infantry.push(this.availableTroops[i]);
          tab[0]++;
        }
        else if(this.availableTroops[i].unit.building == "pasture"){
          this.cavalry.push(this.availableTroops[i]);
          tab[1]++;
        }
        else if(this.availableTroops[i].unit.building == "shipyard"){
          this.ships.push(this.availableTroops[i]);
          tab[2]++;
        }
        else if(this.availableTroops[i].unit.building == "machineFactory"){
          this.vehicles.push(this.availableTroops[i]);
          tab[3]++;
        }
        else {
          this.others.push(this.availableTroops[i]);
          tab[4]++;
        }
      }
      if(tab[0] == 0) this.infantry = null;
      if(tab[1] == 0) this.cavalry = null;
      if(tab[2] == 0) this.ships = null;
      if(tab[3] == 0) this.vehicles = null;
      if(tab[4] == 0) this.others = null;
    }
  },
  mounted: function(){
    const axios = require('axios').default;

    axios
      .get("http://localhost:8088/user/current")
      .then(response => (
        this.player = response.data,
        axios
          .get("http://localhost:8088/user/"+this.player.id+"/city/"+this.player.currentCityId+"/unit")
          .then(response => (
            this.availableTroops = response.data.content,
            this.divideTroops()
          ))
        )).catch((error) => {
          this.availableTroops = null;
          alert(error.response.data.message);
        })


  }
}
</script>

<style scoped>
.allUnits {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
}
.button {
  width: 250px;
  margin: 10px 20px !important;
}
.btnPanel {
  display: flex;
  flex-direction: column;
  width: 300px;
  justify-content: center;
}
.upperPanel {
  display: flex;
  flex-direction: row;
}
</style>
