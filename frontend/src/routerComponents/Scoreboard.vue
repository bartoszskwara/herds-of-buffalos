<template>
  <div id="scoreboard">
    <md-table class="table" v-model="users" md-sort="score" md-card>
      <md-table-toolbar>
        <h1 class="md-title">Ranking graczy</h1>
      </md-table-toolbar>

      <md-table-row slot="md-table-row" slot-scope="{ item }">
        <md-table-cell md-label="ID" md-numeric>{{ item.id }}</md-table-cell>
        <md-table-cell md-label="Nick" md-sort-by="nick">{{ item.nick }}</md-table-cell>
        <md-table-cell md-label="Punkty" md-sort-by="score">{{ item.score }}</md-table-cell>
      </md-table-row>
    </md-table>
  </div>
</template>

<script>

export default {
  data() {
    return {
      users: [
        {
          id: 1,
          nick: 'Shawna Dubbin',
          score: 325,
        },
        {
          id: 2,
          nick: 'Odette Demageard',
          score: 54435,
        },
        {
          id: 3,
          nick: 'Lonnie Izkovitz',
          score: 324,
        },
        {
          id: 4,
          nick: 'Thatcher Stave',
          score: 657,
        },
        {
          id: 5,
          nick: 'Clarinda Marieton',
          score: 50,
        }
      ]
    }
  }
}
</script>

<style scoped>
.md-table {
}

</style>
