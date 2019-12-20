<template>
  <div id="building">

    <md-toolbar :md-elevation="1" @click.native='setRoute(building.link)'>
      <span class="md-title">{{building.name}}</span>
    </md-toolbar>
      <md-list-item>
        <md-icon :md-src="require('./assets/bricks.svg')" />
        <div class="md-list-item-text level">
          <span>{{building.level}}</span>
          <span>Poziom</span>
        </div>
      </md-list-item>

      <md-list-item layout="row">
        <md-icon :md-src="require('./assets/coins.svg')" />

        <div class="md-list-item-text">
          <span><md-icon class="mat" :md-src="require('./assets/wood.svg')" /></span>
          <span>{{building.price.wood}}</span>
        </div>

        <div class="md-list-item-text">
          <span><md-icon class="mat" :md-src="require('./assets/clay.svg')" /></span>
          <span>{{building.price.clay}}</span>
        </div>

        <div class="md-list-item-text">
          <span><md-icon class="mat" :md-src="require('./assets/iron.svg')" /></span>
          <span>{{building.price.iron}}</span>
        </div>
      </md-list-item>

  </div>
</template>

<script>
export default {
  props: {
    building: Object,
  },
  methods: {
    setRoute(link){
      this.$router.push(link);
    }
  }
}
</script>

<style scoped>
.md-toolbar:hover {
  background-color: #ebebeb;
  cursor: pointer;
}
  .md-list {
    width: 320px;
    max-width: 100%;
    display: inline-block;
    vertical-align: top;
    border: 1px solid #e6e6e6;
    padding-top: 0 !important;
  }
  .md-list-item-text {
    flex: auto;
  }
  .md-list-item-text span {
    text-align: center;
  }
  .md-icon {
    width: 0 !important;
    min-height: 110%;
  }
  .mat {
    margin: auto !important;
    margin-bottom: 5px !important;
  }
  .md-list-item-text.level {
    margin-right: 60%;
  }
</style>
