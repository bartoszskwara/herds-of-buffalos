<template>
  <div id="player">
    <md-list class="md-double-line">
      <md-subheader>Informacje</md-subheader>

      <md-list-item md-expand>
        <md-avatar>
          <img src="https://placeimg.com/40/40/people/5" alt="People">
        </md-avatar>
        <div class="md-list-item-text">
          <span>{{player.name}}</span>
          <span>Nick</span>
        </div>
        <md-list slot="md-expand">
      <md-list-item>
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
        <div class="md-list-item-text">
          <span>{{activeCity.name}}</span>
          <span>Aktywna wioska</span>
        </div>
      </md-list-item>
    </md-list>

      </md-list-item>
    </md-list>
  </div>
</template>

<script>

export default {
  props: {
    player: Object,
    activeCity: Object
  },
}
</script>

<style scoped>
.md-list {
  margin-left: 0 !important;
}
.md-list-item-text span {
  text-align: center !important;
}

</style>
