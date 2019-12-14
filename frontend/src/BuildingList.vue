<template>
  <div id="buildings">
    <md-list class="md-double-line" :key="building" v-for="building in village">
      <building v-bind:name="building.name" v-bind:level="building.level" v-bind:price="building.price"></building>
    </md-list>
  </div>
</template>

<script>
import Building from "./Building.vue";

export default {
  components: {Building},
  props: {
    village: Array
  },
  data(){
    return {

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
