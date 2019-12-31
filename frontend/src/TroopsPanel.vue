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

      <md-list-item :class="checkFirstClass(troop)" :key="troop.key+troop.level" v-for="troop in troops">
        <div class="md-list-item-text" style="flex-grow: 0.5">
          <md-icon>face</md-icon>
        </div>
        <div class="md-list-item-text" style="flex-grow: 2">
          <span>{{troop.label}}</span>
          <span>Rodzaj</span>
        </div>
        <div class="md-list-item-text level" style="flex-grow: 2">
          <md-icon class="star" :md-src="require('./assets/star.svg')" v-bind:key="n" v-for="n in troop.level">{{n}}</md-icon>
        </div>
        <div class="md-list-item-text" style="flex-grow: 1">
          <span>{{troop.amount}}</span>
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
      pastureCapacity: 13000,
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
    },
    checkFirstClass(troop){
      if(troop.level == 1){
        return "first";
      }
      else {
        return "";
      }
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
        count += this.troops[i].amount;
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
.md-list-item span {
  text-align: center !important;
}
.md-list-item.first {
  border-top: 1px solid #d6d6d6;
}
.md-icon {
  width: 0;
}
.nick {
  padding-right: 35%;
}
.progressBar {
  margin-bottom: 10px;
  background-color: #c5edcf !important;
  --md-theme-default-accent: #1db847;
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
.level {
  display: flex;
  flex-direction: row;
  padding-right: 10px;
  padding-left: 10px;
}
.star {
  height: 110%;
  text-align: left;
  padding-right: 0px;
  width: 0;
}
</style>
