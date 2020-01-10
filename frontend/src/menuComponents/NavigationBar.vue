<template>
  <md-app md-mode="fixed-last" :md-elevation="1">
    <md-app-toolbar class="md-dense md-primary">
      <div class="md-toolbar-row">
        <md-tabs class="md-primary" md-alignment="fixed" md-sync-route>
          <md-tab id="tab-home" md-label="Przegląd" to="/" exact></md-tab>
          <md-tab id="tab-map" md-label="Mapa" to="/map"></md-tab>
          <md-tab id="tab-reports" md-label="Raporty" to="/reports"></md-tab>
          <md-tab id="tab-scoreboard" md-label="Ranking" to="/scoreboard"></md-tab>
          <md-tab id="tab-logout" md-label="Wyloguj"></md-tab>
        </md-tabs>
      </div>
    </md-app-toolbar>
  </md-app>
</template>

<script>

</script>

<style scoped>
.md-app {
  max-height: 100px;
  height: 70px;
  margin-bottom: 20px;
}
.md-tabs {
  width: 100%;
}
</style>
