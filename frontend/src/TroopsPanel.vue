<template>
  <div id="player">
    <md-list class="md-double-line md-dense">
      <md-subheader>Jednostki</md-subheader>
      <md-list-item>
        <div class="md-list-item-text">
          <span v-bind:style="setPastureStyle()" class="progressValue">{{allTroops}} / {{pastureCapacity}}</span>
          <span><md-progress-bar class="md-accent progressBar" md-mode="determinate" :md-value="progressCapacity"></md-progress-bar></span>
          <span class="progressLabel">Pojemność pastwiska</span>
        </div>
      </md-list-item>

      <md-list-item :key="troop.name" v-for="troop in nonzeroTroops">

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
  data() {
    return {
      pastureCapacity: 700,
    }
  },
  props: {
    troops: Array
  },
  methods: {
    setPastureStyle(){
      var style;
      if(this.pastureCapacity - this.allTroops <= 0.05*this.pastureCapacity
        || this.pastureCapacity - this.allTroops <= 150){
        style = {color: 'red'};
      }
      else {
        style = {color: 'black'};
      }
      return style;
    }
  },
  computed: {
    nonzeroTroops: function() {
      return this.troops.filter(function(u) {
        if(u.count > 0){
          return u;
        }
      })
    },
    allTroops: function () {
      var count = 0;
      var i = 0;
      this.troops.forEach(() => {
        count += this.troops[i].count;
        i++;
      })
      return count;
    },
    progressCapacity: function() {
      return this.allTroops/this.pastureCapacity*100;
    }
  }
}
</script>

<style scoped>
.md-list {
  margin-left: 0 !important;
}
.md-list-item-text span {
  text-align: center !important;
}
.nick {
  padding-right: 35%;
}
.progressBar {
  margin-bottom: 10px;
  background-color: #e8c1b0 !important;
  --md-theme-default-accent: #c75d30;
}
.progressLabel {
  color: grey;
}
.progressValue {
  color: black;
  font-size: 8pt !important;
  margin-bottom: 5px;
  font-family: Sui Generis;
}
</style>
