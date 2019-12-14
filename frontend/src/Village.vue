<template>
  <div id="village">
    <material class="topcorner" :materials="materials"></material>
    <building-list :village="village"></building-list>
  </div>
</template>

<script>
import BuildingList from "./BuildingList.vue";
import Material from "./Material.vue";


export default {
  components: {BuildingList, Material},
  data() {
    return {
      village: [
        {
          name: "Ratusz",
          level: 4,
          maxLevel: 20,
          price: {
            wood: 20000,
            clay: 3000,
            iron: 500,
          }
        },
        {
          name: "Pastwisko bawołów",
          level: 1,
          maxLevel: 6,
          price: {
            wood: 4,
            clay: 20,
            iron: 60,
          }
        },
      ],
      materials: {
        wood: 3298,
        clay: 1290,
        iron: 333
      }
    }
  }
}
</script>

<style>
  .topcorner{
   float: right;
   margin-right: 20px;
  }
</style>
