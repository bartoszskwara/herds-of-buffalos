<template>
  <div id="app">
    <h1>Welcome to the tribal buffalos world!</h1>
    <building-list :village="village"></building-list>
  </div>
</template>

<script>
import BuildingList from "./BuildingList.vue";

export default {
  components: {BuildingList},
  data() {
    return {
      village: [
        {
          name: "Ratusz",
          level: 4,
          price: {
            wood: 200,
            clay: 300,
            iron: 500,
          }
        },
        {
          name: "Pastwisko bawołów",
          level: 1,
          price: {
            wood: 40,
            clay: 20,
            iron: 60,
          }
        },
      ]
    }
  }
}
</script>

<style>
  h1 {
    font-family: Algerian;
    text-align: center;
  }
</style>
