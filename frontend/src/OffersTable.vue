<template>
  <div id="scoreboard">
    <md-table v-model="offers" md-sort="date" md-card>
      <md-table-toolbar>
        <h1 class="md-title">Bieżące oferty wymiany</h1>
      </md-table-toolbar>

      <md-table-row slot="md-table-row" slot-scope="{ item }">
        <md-table-cell md-label="Nick">{{ item.nick }}</md-table-cell>
        <md-table-cell md-label="Oferuje">
          <span class="cell">
            <md-icon class="mat" :md-src="require('./assets/wood.svg')" /><div class="mat">{{item.supply.wood}}</div>
          </span>
          <span class="cell">
            <md-icon class="mat" :md-src="require('./assets/clay.svg')" /><div class="mat">{{item.supply.clay}}</div>
          </span>
          <span class="cell">
            <md-icon class="mat" :md-src="require('./assets/iron.svg')" /><div class="mat">{{item.supply.iron}}</div>
          </span>
        </md-table-cell>
        <md-table-cell md-label="W zamian za">
          <span class="cell">
            <md-icon class="mat" :md-src="require('./assets/wood.svg')" /><div class="mat">{{item.demand.wood}}</div>
          </span>
          <span class="cell">
            <md-icon class="mat" :md-src="require('./assets/clay.svg')" /><div class="mat">{{item.demand.clay}}</div>
          </span>
          <span class="cell">
            <md-icon class="mat" :md-src="require('./assets/iron.svg')" /><div class="mat">{{item.demand.iron}}</div>
          </span>
        </md-table-cell>
        <md-table-cell md-label="Data utworzenia">{{ item.date }}</md-table-cell>
        <md-table-cell>
          <md-button class="md-raised md-primary" @click="acceptOffer()">Przyjmij ofertę</md-button>
        </md-table-cell>
      </md-table-row>
    </md-table>
  </div>
</template>

<script>

export default {
  data() {
    return {
      offers: [
        {
          id: 1,
          nick: 'Shawna Dubbin',
          supply: {
            wood: 200,
            clay: 50,
            iron: 0
          },
          demand: {
            wood: 0,
            clay: 0,
            iron: 500
          },
          date: "24.12.2019",
        },
        {
          id: 2,
          nick: 'Bartek',
          supply: {
            wood: 2200,
            clay: 550,
            iron: 0
          },
          demand: {
            wood: 100,
            clay: 4000,
            iron: 100
          },
          date: "25.12.2019"
        },
      ]
    }
  },
  methods: {
    acceptOffer(){

    }
  }
}
</script>

<style scoped>
.md-table {
  min-width: 800px !important;
}
.mat {
  width: 70px;
  text-align: center;
  margin: 5px auto;
  display: flex;
  flex-direction: column;
  align-self: center;
}
.cell {
  display: flex;
  width: 70px;
  text-align: center;
}

</style>
