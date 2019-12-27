<template>
  <div id="market">
    <div class="container">
    <md-list class="md-double-line">
      <md-subheader>Profil</md-subheader>

      <md-list-item>
        <md-avatar>
          <img src="https://placeimg.com/40/40/people/5" alt="People">
        </md-avatar>
        <div class="md-list-item-text nick">
          <span>{{player.name}}</span>
          <span>Nick</span>
        </div>
        <div class="md-list-item-text second">
          <span>{{player.points}}</span>
          <span>Punkty</span>
        </div>
        <div class="md-list-item-text">
          <span>{{player.numberOfCities}}</span>
          <span>Ilość wiosek</span>
        </div>
        <div class="md-list-item-text">
          <span>{{player.ranking}}</span>
          <span>Ranking</span>
        </div>
      </md-list-item>

      <md-list-item>
        <div class="table">
          <md-table v-model="cities" md-sort="points" md-card md-sort-order="asc">
            <md-table-toolbar>
              <h1 class="md-title">Wioski</h1>
            </md-table-toolbar>

            <md-table-row slot="md-table-row" slot-scope="{ item }">
              <md-table-cell md-label="Nazwa wioski">{{ item.name }}</md-table-cell>
              <md-table-cell md-label="Punkty" md-sort-by="points">{{ item.points }}</md-table-cell>
              <md-table-cell><md-button class="md-raised md-accent btn">Wyślij wojska</md-button></md-table-cell>
            </md-table-row>
          </md-table>
        </div>
      </md-list-item>
    </md-list>
  </div>
  </div>
</template>

<script>

export default {
  data() {
    return {
      player: Object,
      cities: [
        {
          user:
          {
            id: Number,
            name: String
          },
          name: String,
          coordX: Number,
          coordY: Number,
          points: Number
        }
      ],
    }
  },
  props: {
    userId: Number,
  },
  created: function(){
    const axios = require('axios').default;
    axios
      .get('http://localhost:8088/user/'+this.userId)
      .then(response => (
        this.player = response.data,
        axios
          .get('http://localhost:8088/user/'+this.userId+'/city')
          .then(response => (
            this.cities = response.data.content
          ))
      ))
  },
}
</script>

<style scoped>
.container {
  display: flex;
  justify-content: center;
}
.md-list {
  min-width: 500px;
  width: 80%;
  margin: auto !important;
  align-self: center;
}

.md-list-item-text {
  text-align: center !important;
}
.table {
  width: 100%;
}
.btn {
  float: right;
}
</style>
