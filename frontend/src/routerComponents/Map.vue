<template>
  <div id="map">
    <h1>Mapa</h1>
    <div class="mapView">
      <div class="wioska" v-bind:key="village.name" v-for="village in allCities"
        v-bind:style="stylePositionVillage(village)" @click="villageClicked(village)">
        <div class="yourCity" v-if="village.user.id == player.id" v-bind:style="village.id == player.currentCityId ? 'background-color: #fff700' : ''"></div>
        <img :src="setVillageImage(village)" />
        <md-tooltip md-direction="top"><b>Gracz:</b>  {{village.user.name}}
        <br><b>Wioska:</b>  {{village.name}}
        <br><b>Punkty:</b>  {{village.points}}</md-tooltip>
      </div>
    </div>
    <div class="legenda">
      <div class="legendaElement">
        <div class="yourCityLegenda" style="background-color: #fff700"></div>
        <div>Twoja aktywna wioska</div>
      </div>
      <div class="legendaElement">
        <div class="yourCityLegenda"></div>
        <div>Twoja wioska</div>
      </div>
    </div>


    <md-dialog :md-active.sync="villageDialog">
      <md-dialog-title class="dialogHeader">
        <h1 class="headerTitle">{{chosenVillage.name}}</h1>
        <span class="smallText">{{chosenVillage.points}} punktów</span>
        <span class="playerText">Gracz: {{chosenVillage.user.name}}</span>
      </md-dialog-title>

      <div class="dialogButtons">
        <md-button class="md-raised md-accent" @click="sendTroops(chosenVillage)">Wyślij wojska</md-button>
        <md-button class="md-raised md-primary" @click="showProfile(chosenVillage.user.id)">Profil gracza</md-button>
      </div>
    </md-dialog>

  </div>
</template>

<script>

export default {
  data() {
    return {
      player: {},
      squareSizeX: 45,
      squareSizeY: 40,
      imagePath: String,
      villageDialog: false,
      chosenVillage: {
        user:{
          id: Number,
        }
      },
      allCities: [],
    }
  },
  mounted: function(){
    const axios = require('axios').default;
    var i = 0;
    axios
    .get("http://localhost:8088/user/current")
    .then(response => (
      this.player = response.data,
    axios
    .get('http://localhost:8088/user')
    .then(response => (
      response.data.content.forEach(() => {
        var j = 0;
        axios
        .get("http://localhost:8088/user/"+response.data.content[i].id+"/city")
        .then(response => (
          response.data.content.forEach(() => {
            this.allCities.push(response.data.content[j]);
            j++;
          }
        )))
        i++;
      }) )) ));
    },
    methods: {
      stylePositionVillage(village) {
        var positionStyle = {top: this.squareSizeY*village.coordY+'px', left: this.squareSizeX*village.coordX+'px'};
        return positionStyle;
      },
      setVillageImage(village){
        var image = require.context('../assets/', false, /\.png$/)
        if(village.points < 300){
          return image('./wioska1.png')
        }
        else if(village.points >= 300 && village.points < 1000){
          return image('./wioska2.png')
        }
        else if(village.points >= 1000 && village.points < 3000){
          return image('./wioska3.png')
        }
        else if(village.points >= 3000){
          return image('./wioska4.png')
        }
      },
      villageClicked(village){
        this.chosenVillage = village;
        this.villageDialog = true;
      },
      showProfile(id){
        this.$router.push({ name: 'profile', params: {userId: id }})
      },
      sendTroops(village){
        this.$router.push({ name: 'sendtroops', params: {village: village }})
      }
    }
  }
  </script>

  <style>
  #map {
    margin: 0px 40px 0px 40px;
  }
  .mapView {
    width: 900px;
    min-width: 900px;
    margin: auto;
    height: 560px;
    outline: 1px solid black;
    position: relative;
    background:
    linear-gradient(-90deg, rgba(0,0,0,.05) 1px, transparent 1px),
    linear-gradient(rgba(0,0,0,.05) 1px, transparent 1px),
    linear-gradient(-90deg, rgba(0, 0, 0, .04) 1px, transparent 1px),
    linear-gradient(rgba(0,0,0,.04) 1px, transparent 1px),
    linear-gradient(transparent 3px, #a5e8aa 3px, #a5e8aa 38px, transparent 38px),
    linear-gradient(-90deg, #aaa 1px, transparent 1px),
    linear-gradient(-90deg, transparent 3px, #a5e8aa 3px, #a5e8aa 43px, transparent 43px),
    linear-gradient(#aaa 1px, transparent 1px),
    #a5e8aa;
    background-size:
    22.5px 20px,
    22.5px 20px,
    45px 40px,
    45px 40px,
    45px 40px,
    45px 40px,
    45px 40px,
    45px 40px;

  }
  .wioska {
    position: absolute;
    width: 45px;
    text-align: center;
    cursor: pointer;
  }
  .yourCity {
    background-color: #00ff1a;
    width: 10px;
    height: 10px;
    position: absolute;
    left: 5px;
    border-radius: 15px;
    border: 1px solid black;
  }
  .yourCityLegenda {
    background-color: #00ff1a;
    width: 10px;
    height: 10px;
    border-radius: 15px;
    border: 1px solid black;
    margin-top: 5px;
    margin-right: 10px;
  }
  .legenda {
    display: flex;
    flex-direction: column;
    flex-wrap: wrap;
    margin: auto;
    margin-top: 30px;

    width: 300px;
    height: auto;
    border: 1px solid grey;
    padding: 10px 20px;
  }
  .legendaElement {
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
  }
  .md-tooltip {
    height: auto !important;
  }
  .md-dialog {
    padding: 0px 20px 20px 20px;
  }
  .dialogHeader {
    border-bottom: 1px solid grey;
    text-align: center;
    padding-bottom: 20px !important;
  }
  .headerTitle {
    font-size: 17pt;
    margin: 0;
  }
  .smallText {
    font-size: 8pt;
    font-style: italic;
    margin: 0;
    padding: 0;
    display: block;
  }
  .playerText {
    font-size: 12pt;
    font-style: italic;
    margin: 0;
    padding: 0;
    display: block;
  }
  .dialogButtons {
    margin: auto;
  }
  </style>
