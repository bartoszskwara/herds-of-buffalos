<template>
  <div id="app">
    <navigation-bar></navigation-bar>
    <router-view></router-view>
  </div>
</template>

<script>
import NavigationBar from "./NavigationBar.vue";
export default {
  components: {NavigationBar},
}
</script>

<style>
 html {
   overflow-y: scroll;
 }
 h1 {
   font-family: Sui Generis;
   font-size: 30pt;
   text-align: center;
 }
</style>
