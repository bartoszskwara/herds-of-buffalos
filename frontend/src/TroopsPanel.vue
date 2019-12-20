<template>
  <div id="player">
    <md-list class="md-double-line md-dense">
      <md-subheader>Jednostki</md-subheader>

      <md-list-item :key="troop" v-for="troop in nonzeroTroops">

        <md-icon>face</md-icon>
        <div class="md-list-item-text">
          <span>{{troop.name}}</span>
          <span>Rodzaj</span>
        </div>
        <div class="md-list-item-text">
          <span>{{troop.count}}</span>
          <span>Ilość</span>
        </div>

      </md-list-item>

    </md-list>
  </div>
</template>

<script>

export default {
  props: {
    troops: Array
  },
  computed: {
    nonzeroTroops: function() {
      return this.troops.filter(function(u) {
        if(u.count > 0){
          return u;
        }
      })
    }
  }
}
</script>

<style scoped>
.md-list-item-text span {
  text-align: center !important;
}
.nick {
  padding-right: 35%;
}
</style>
