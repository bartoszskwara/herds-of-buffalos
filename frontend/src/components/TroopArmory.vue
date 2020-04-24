<template>
  <div id="troopA">

    <md-toolbar :md-elevation="1">
      <span class="md-title">{{troop.unit.label}}</span>
    </md-toolbar>
    <md-list-item :key="troopKind.level" v-for="troopKind in troop.levelsData">
      <div class="md-list-item-text" style="flex-grow: 1">
        <md-icon class="chevron" :md-src="require('../assets/chevron'+troopKind.level+'.svg')"></md-icon>
      </div>
      <div class="md-list-item-text" style="flex-grow: 0.7">
        <span><md-icon class="mat strength" :md-src="require('../assets/attack.svg')" /></span>
        <span class="textLabel">{{troopKind.skills.attack}}</span>
      </div>
      <div class="md-list-item-text" style="flex-grow: 0.7">
        <span><md-icon class="mat strength" :md-src="require('../assets/defense.svg')" /></span>
        <span class="textLabel">{{troopKind.skills.defense}}</span>
      </div>
      <div class="md-list-item-text" style="flex-grow: 0.7">
        <span><md-icon class="mat strength" :md-src="require('../assets/health.svg')" /></span>
        <span class="textLabel">{{troopKind.skills.defense}}</span>
      </div>
      <div class="md-list-item-text surowce" style="flex-grow: 2" v-if="!troopKind.enabled">
        <span class="cell">
          <md-icon class="mat" :md-src="require('../assets/wood.svg')" /><div class="upgradeCost">{{troopKind.upgradingCost.wood}}</div>
        </span>
        <span class="cell">
          <md-icon class="mat" :md-src="require('../assets/clay.svg')" /><div class="upgradeCost">{{troopKind.upgradingCost.clay}}</div>
        </span>
        <span class="cell">
          <md-icon class="mat" :md-src="require('../assets/iron.svg')" /><div class="upgradeCost">{{troopKind.upgradingCost.iron}}</div>
        </span>
      </div>
      <div class="md-list-item-text btn" style="flex-grow: 2" v-if="!troopKind.enabled">
          <md-button class="md-raised md-primary" @click="rankup(troop.unit.key, troop.unit.label, troopKind.level)" :disabled="!troopKind.upgradeRequirementsMet">Zbadaj</md-button>
      </div>
      <div class="md-list-item-text" style="flex-grow: 4" v-if="troopKind.enabled">
        <span>
          <md-icon class="mat" :md-src="require('../assets/upgraded.svg')" />
        </span>
        <span>
          <p>Jednostka została już zbadana!</p>
        </span>
      </div>
    </md-list-item>


    <md-snackbar md-position="center" :md-duration="snackbarDuration" :md-active.sync="showSnackbar" md-persistent>
      <span>{{snackbarText}}</span>
    </md-snackbar>
  </div>
</template>

<script>
import { EventBus } from '../event-bus.js';

export default {
  data() {
    return {
      showSnackbar: false,
      snackbarText: String,
      snackbarDuration: 3000,
    }
  },
  props: {
    troop: Object,
    player: Object,
  },
  methods: {
    rankup(key, label, lvl) {
      const axios = require('axios').default;
      axios.post('http://localhost:8088/user/'+this.player.id+'/city/'+this.player.currentCityId+'/unit/upgrade', {
                  "level": parseInt(lvl),
                  "unit": String(key)
              }).then(() => {
                EventBus.$emit('unit-upgrade')
              }).catch(function(error) {
                alert(error);
              })

      this.snackbarText = "Rozpoczęto szkolenie jednostek typu "+label+" na poziomie "+lvl+".";
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
  width: 0;
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

}
.chevron {
  height: 110%;
  text-align: center;
  padding-right: 0px;
  width: 40px;
}
</style>
