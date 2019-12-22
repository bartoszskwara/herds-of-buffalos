<template>
  <div id="barracks">
      <h1>Koszary</h1>
    <md-list class="md-double-line troop" :key="troop.name" v-for="troop in troopsArray">
      <troop-recruit v-bind:troop="troop"></troop-recruit>
    </md-list>
  </div>
</template>

<script>
import TroopRecruit from "./TroopRecruit.vue";
import troopsArray from "./assets/troops.js";

export default {
  components: {TroopRecruit},
  data() {
    return {
      alertMaxLevel: false,
      troopsArray: troopsArray,
    }
  },
  methods: {
  }

}
</script>

<style scoped>
h1 {
  font-family: Sui Generis;
  font-size: 30pt;
  text-align: center;
}
  .md-list.troop {
    width: 95% !important;
    min-width: 720px;
    max-width: 100%;
    display: inline-block;
    vertical-align: top;
    border: 1px solid rgba(#000, .12);
    float: left;
    margin-left: 20px !important;
    margin-bottom: 0 !important;
    padding-top: 0 !important;
    padding-bottom: 0 !important;
  }
  .leftPanel {
    float: left;
    width: 76%;
  }
  .rightPanel {
    float: right;
    width: 24%;
    border-left: 1px solid grey;
    padding-right: 20px;
  }
</style>
