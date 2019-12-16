<template>
  <div id="buildings">
    <md-button :key="building" v-for="building in buildings" class="md-dense md-raised md-primary" @click.native='setRoute(building.link)'>{{building.name}}</md-button>
  </div>
</template>

<script>


export default {
  components: {},
  props: {
    buildings: Array
  },
  data() {
    return {
      alertMaxLevel: false
    }
  },
  methods: {
    upgrade(building) {
      if(building.level < building.maxLevel){
        building.level++;
      }
      else {
        this.alertMaxLevel = true;
      }
    },
    setRoute(link){
      this.$router.push(link);
    }
  }

}
</script>

<style>
  .md-list {
    width: 320px;
    max-width: 100%;
    display: inline-block;
    vertical-align: top;
    border: 1px solid rgba(#000, .12);
    float: left;
    margin-left: 20px !important;
    margin-bottom: 20px !important;
  }
</style>
