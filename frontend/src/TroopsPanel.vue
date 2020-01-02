<template>
  <div id="player">
    <md-list class="md-double-line md-dense">
      <md-subheader>Jednostki</md-subheader>
      <md-list-item style="padding-bottom: 20px">
        <div class="md-list-item-text">
          <span v-bind:style="setPastureStyle()" class="progressValue">{{allTroops}} / {{pastureCapacity}}</span>
          <span><md-progress-bar class="md-accent progressBar" md-mode="determinate" :md-value="progressCapacity"></md-progress-bar></span>
          <span class="progressLabel">Pojemność pastwiska</span>
        </div>
      </md-list-item>

      <md-list class="md-double-line md-dense" :key="troop.key" v-for="troop in troops">
        <md-subheader class="first">{{troop.unit.label}}</md-subheader>
        <md-list-item :key="troopKind.level" v-for="troopKind in troop.levelsData">

          <div class="md-list-item-text unitSymbol" style="flex-grow: 1">
            <md-icon>face</md-icon>
          </div>
          <div class="md-list-item-text level" style="flex-grow: 2" v-if="isNaN(troopKind.level) == false">
            <md-icon class="chevron" :md-src="require('./assets/chevron'+troopKind.level+'.svg')"></md-icon>

          </div>
          <div class="md-list-item-text" style="flex-grow: 2">
            <span>{{troopKind.amountInCity}}</span>
            <span>Ilość</span>
          </div>

        </md-list-item>
      </md-list>
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
    troops: Array,
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
  },
  computed: {
    allTroops: function () {
      var count = 0;
      var i = 0;
      this.troops.forEach(() => {
        var j = 0;
        var lvls = this.troops[i].levelsData;
        lvls.forEach(() => {
          count += lvls[j].amountInCity;
          j++;
        })
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
.first {
  border-top: 1px solid #d6d6d6;
}
.md-icon {
  width: 0px;
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
.chevron {
  height: 110%;
  text-align: center;
  padding-right: 0px;
  width: 40px;
}
.unitSymbol {
  text-align: center;
  padding-left: 30px;
}
</style>
