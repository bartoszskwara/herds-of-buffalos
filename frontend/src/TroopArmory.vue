<template>
  <div id="troop">

    <md-toolbar :md-elevation="1">
      <span class="md-title">{{troop.name}}</span>
    </md-toolbar>
    <md-list-item>
      <div class="md-list-item-text level" style="flex-grow: 1">
        <md-icon class="star" :md-src="require('./assets/star.svg')" />
        <md-icon class="star" :md-src="require('./assets/star.svg')" />

      </div>
      <div class="md-list-item-text" style="flex-grow: 0.7">
        <span><md-icon class="mat strength" :md-src="require('./assets/attack.svg')" /></span>
        <span class="textLabel">{{troop.attr.attack}}</span>
      </div>
      <div class="md-list-item-text" style="flex-grow: 0.7">
        <span><md-icon class="mat strength" :md-src="require('./assets/defense.svg')" /></span>
        <span class="textLabel">{{troop.attr.defense}}</span>
      </div>
      <div class="md-list-item-text" style="flex-grow: 0.7">
        <span><md-icon class="mat strength" :md-src="require('./assets/defense.svg')" /></span>
        <span class="textLabel">{{troop.attr.defense}}</span>
      </div>
      <div class="md-list-item-text" style="flex-grow: 0.7">
        <span><md-icon class="mat strength" :md-src="require('./assets/defense.svg')" /></span>
        <span class="textLabel">{{troop.attr.defense}}</span>
      </div>
      <div class="md-list-item-text surowce" style="flex-grow: 2">
        <span class="cell">
          <md-icon class="mat" :md-src="require('./assets/wood.svg')" /><div class="upgradeCost">{{troop.price.wood}}</div>
        </span>
        <span class="cell">
          <md-icon class="mat" :md-src="require('./assets/clay.svg')" /><div class="upgradeCost">{{troop.price.clay}}</div>
        </span>
        <span class="cell">
          <md-icon class="mat" :md-src="require('./assets/iron.svg')" /><div class="upgradeCost">{{troop.price.iron}}</div>
        </span>
      </div>
      <div class="md-list-item-text btn" style="flex-grow: 2">
          <md-button class="md-raised md-primary" @click="rankup()">Zbadaj</md-button>
      </div>
    </md-list-item>


    <md-snackbar md-position="center" :md-duration="snackbarDuration" :md-active.sync="showSnackbar" md-persistent>
      <span>{{snackbarText}}</span>
    </md-snackbar>
  </div>
</template>

<script>
export default {
  data() {
    return {
      showSnackbar: false,
      snackbarText: String,
      snackbarDuration: 3000,
    }
  },
  props: {
    troop: Object
  },
  methods: {
    rankup() {
      if(this.troop.level < this.troop.maxLevel){
        this.snackbarText = "Rozpoczęto szkolenie jednostek typu "+this.troop.name+".";
        this.troop.level++;
      }
      else {
        this.snackbarText = "Osiągnięto już maksymalne wyszkolenie jednostek typu "+this.troop.name+".";
      }
      this.showSnackbar = true;
    }
  }
}
</script>

<style scoped>
.md-list-item {
    border: 1px solid #d6d6d6;
    border-top: 0;
}
.disabled {
  opacity: 50% !important;
}
.md-list-item-text span {

  text-align: center;
  min-height: 30px;
  height: 40px;
}
.text {
  padding-top: 20px;
}
.textLabel {
  padding-top: 5px;
}
.md-icon {
  width: 0 !important;
  min-height: 110%;
}
.mat {
  margin: auto !important;
  margin-bottom: 5px !important;
}
.strength {
  padding-top: 10px;
}
.md-list-item-text.surowce {
  padding-left: 20px;
}
.md-list-item-text.btn {
  padding-left: 10px;
}
.upgradeCost {
  width: 50%;
  text-align: left;
  display: flex;
  flex-direction: column;
  align-self: center;
}
.cell {
  display: flex;
  text-align: center;
  height: 30px !important;
}
.level {
  display: flex;
  flex-direction: row;
  padding-right: 30px;
}
.star {
  height: 110%;
  text-align: left;
  padding-right: 0px;
}
</style>
