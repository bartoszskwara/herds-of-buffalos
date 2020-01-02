<template>
  <div id="troop">
    <md-toolbar :md-elevation="1">
      <span class="md-title">{{troop.unit.label}}</span>
    </md-toolbar>
    <div v-bind:key="unitKind.level" v-for="unitKind in troop.levelsData">
      <md-list-item v-if="unitKind.enabled == true">
        <div class="md-list-item-text level" style="flex-grow: 1">
          <md-icon class="chevron" :md-src="require('./assets/chevron'+unitKind.level+'.svg')"></md-icon>
        </div>

        <div class="md-list-item-text" style="flex-grow: 2">
          <span class="text">{{unitKind.amountInCity}}</span>
          <span class="textLabel">Ilość</span>
        </div>
        <div class="md-list-item-text divider" style="flex-grow: 1">
          <span><md-icon class="mat" :md-src="require('./assets/attack.svg')" /></span>
          <span class="textLabel">{{unitKind.skills.attack}}</span>
        </div>
        <div class="md-list-item-text" style="flex-grow: 1">
          <span><md-icon class="mat" :md-src="require('./assets/defense.svg')" /></span>
          <span class="textLabel">{{unitKind.skills.defense}}</span>
        </div>
        <div class="md-list-item-text" style="flex-grow: 1">
          <span><md-icon class="mat" :md-src="require('./assets/health.svg')" /></span>
          <span class="textLabel">{{unitKind.skills.health}}</span>
        </div>

        <div class="md-list-item-text divider">
          <span><md-icon class="mat" :md-src="require('./assets/wood.svg')" /></span>
          <span class="textLabel">{{unitKind.recruitmentCost.wood}}</span>
        </div>

        <div class="md-list-item-text">
          <span><md-icon class="mat" :md-src="require('./assets/clay.svg')" /></span>
          <span class="textLabel">{{unitKind.recruitmentCost.clay}}</span>
        </div>

        <div class="md-list-item-text">
          <span><md-icon class="mat" :md-src="require('./assets/iron.svg')" /></span>
          <span class="textLabel">{{unitKind.recruitmentCost.iron}}</span>
        </div>

        <div class="md-list-item recruit" style="flex-grow: 2">
          <md-field>
            <label>Rekrutacja</label>
            <md-input v-model="recruitCount[unitKind.level-1]" @keypress="isNumber($event)"></md-input>
            <span class="md-helper-text">Max: {{getMaxRecruitCount(unitKind.recruitmentCost)}}</span>
          </md-field>
        </div>

        <div class="md-list-item-text" style="flex-grow: 2">
          <md-button class="md-raised md-primary" @click="recruit(troop.unit.label, unitKind.level-1, unitKind.recruitmentCost)" :disabled="recruitCount[unitKind.level-1] > 0 ? false : true">Rekrutuj</md-button>
        </div>
      </md-list-item>
    </div>

    <md-snackbar md-position="center" :md-duration="snackbarDuration" :md-active.sync="showSnackbar" md-persistent>
      <span>{{snackbarText}}</span>
    </md-snackbar>
  </div>
</template>

<script>
export default {
  data() {
    return {
      recruitCount: [null, null, null, null, null],
      snackbarRecruitCount: 0,
      snackbarText: String,
      showSnackbar: false,
      snackbarDuration: 3000,
    }
  },
  props: {
    troop: Object,
    resources: Object
  },
  methods: {
    isNumber: function(evt) {
      evt = (evt) ? evt : window.event;
      var charCode = (evt.which) ? evt.which : evt.keyCode;
      if ((charCode > 31 && (charCode < 48 || charCode > 57))) {
        evt.preventDefault();
      } else {
        return true;
      }
    },
    recruit(label, lvl, cost) {
      this.check = cost.clay;
      if(cost.wood*this.recruitCount[lvl] <= this.resources.wood
         && cost.clay*this.recruitCount[lvl] <= this.resources.clay
          && cost.iron*this.recruitCount[lvl] <= this.resources.iron){
        this.showSnackbar = true;
        this.snackbarRecruitCount = this.recruitCount[lvl];
        this.recruitCount[lvl] = null;
        this.snackbarText = "Rozpoczęto rekrutację "+this.snackbarRecruitCount+" jednostek typu "+label+" na poziomie "+(lvl+1)+".";
      }
      else {
        this.showSnackbar = true;
        this.snackbarText = "Nie masz wystarczającej ilości zasobów, by zrekrutować tyle jednostek.";
      }
    },
    getMaxRecruitCount(cost){
      var w, c, i;
      w = this.resources.wood/cost.wood;
      c = this.resources.clay/cost.clay;
      i = this.resources.iron/cost.iron;
      if(w <= c){
        if(w <= i){
          return Math.floor(w);
        }
        else if(i < w){
          return Math.floor(i);
        }
      }
      else if(c < w){
        if(c <= i){
          return Math.floor(c);
        }
        else if(i < c){
          return Math.floor(i);
        }
      }
    }
  },

}
</script>

<style scoped>
.md-list-item {
  border: 1px solid #d6d6d6;
  border-top: 0;
}
.md-list-item-text span {
  text-align: center;
  min-height: 30px;
  /* border: 1px solid red; */
  height: 40px;
}
.divider {
  border-left: 1px solid #d6d6d6;
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
  padding-top: 10px;
}
.md-list-item.recruit {
  padding-left: 30px;
  padding-right: 30px;
  border: 0;
}
.md-helper-text {
  font-size: 8pt !important;
}
.md-input {
  width: 0px;
}
.level {
  padding-left: 20px;
}
.chevron {
  height: 110%;
  text-align: center;
  width: 40px;
}
</style>
