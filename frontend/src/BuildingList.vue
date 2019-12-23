<template>
  <div id="buildings">
    <md-list class="md-double-line" :key="building.name" v-for="building in buildings">
      <building v-bind:building="building"></building>
      <md-list-item>
        <div class="md-list-item-text">
          <md-button class="md-raised md-primary" @click="upgrade(building)">Rozbuduj</md-button>
        </div>
      </md-list-item>
    </md-list>

    <md-dialog-alert
      :md-active.sync="alertMaxLevel"
      md-title="Dalsza rozbudowa niemożliwa!"
      md-content="Ten budynek został już rozbudowany do najwyższego poziomu!" />
  </div>
</template>

<script>
import Building from "./Building.vue";

export default {
  components: {Building},
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
    }
  }

}
</script>

<style>
  .md-list {
    width: 320px;
    min-width: 320px;
    max-width: 100%;
    display: inline-block;
    vertical-align: top;
    border: 1px solid rgba(#000, .12);
    float: left;
    margin-left: 20px !important;
    margin-bottom: 20px !important;
  }
</style>
