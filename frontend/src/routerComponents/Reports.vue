<template>
  <div id="reports">
    <md-table v-model="reports" md-sort="id" md-card>
      <md-table-toolbar>
        <h1 class="md-title">Twoje raporty</h1>
      </md-table-toolbar>

      <md-table-row slot="md-table-row" slot-scope="{ item }">
        <md-table-cell md-label="ID" md-numeric>{{ item.id }}</md-table-cell>
        <md-table-cell md-label="Tytuł">{{ item.title }}</md-table-cell>
        <md-table-cell md-label="Data">{{ item.date }}</md-table-cell>
      </md-table-row>
    </md-table>
  </div>
</template>

<script>

export default {
  data() {
    return {
      reports: [
        {
          id: 1,
          title: 'Bawół Świeczka (Bawol) atakuje gracza Bartek (Ukraina)',
          date: "20.12.2019 19:36",
        },
        {
          id: 2,
          title: 'Bawół Świeczka (Bawol) atakuje gracza Mateusz (Sarajewo)',
          date: "11.12.2019 13:21",
        },
        {
          id: 3,
          title: 'Kuba (Kraków) atakuje gracza Bawół Świeczka (Bawol)',
          date: "15.11.2019 21:22",
        },
      ]
    }
  }
}
</script>

<style scoped>
.md-table {
  min-width: 800px !important;
}
</style>
