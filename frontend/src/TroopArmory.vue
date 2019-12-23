<template>
  <div id="troop">

    <md-toolbar :md-elevation="1">
      <span class="md-title">{{troop.name}}</span>
    </md-toolbar>
    <md-list-item>
      <md-icon :md-src="require('./assets/helmet.svg')" />
      <div class="md-list-item-text">
        <span>
          <md-progress-spinner :md-diameter="25" md-mode="determinate"
            :md-value="getLevelPercentage()">
          </md-progress-spinner>
        </span>
        <span class="textLabel">Wyszkolenie</span>
      </div>
      <div class="md-list-item-text">
        <span class="text">{{troop.count}}</span>
        <span class="textLabel">Ilość</span>
      </div>
      <div class="md-list-item-text">
        <span><md-icon class="mat" :md-src="require('./assets/attack.svg')" /></span>
        <span class="textLabel">{{troop.attr.attack}}</span>
      </div>
      <div class="md-list-item-text">
        <span><md-icon class="mat" :md-src="require('./assets/defense.svg')" /></span>
        <span class="textLabel">{{troop.attr.defense}}</span>
      </div>
    </md-list-item>
    <md-divider></md-divider>
    <md-list-item layout="row" class="surowce">
      <md-icon :md-src="require('./assets/rank.svg')" />

      <div class="md-list-item-text">
        <span><md-icon class="mat" :md-src="require('./assets/wood.svg')" /></span>
        <span class="textLabel">{{troop.price.wood}}</span>
      </div>

      <div class="md-list-item-text">
        <span><md-icon class="mat" :md-src="require('./assets/clay.svg')" /></span>
        <span class="textLabel">{{troop.price.clay}}</span>
      </div>

      <div class="md-list-item-text">
        <span><md-icon class="mat" :md-src="require('./assets/iron.svg')" /></span>
        <span class="textLabel">{{troop.price.iron}}</span>
      </div>
    </md-list-item>
    <md-divider></md-divider>
    <md-list-item>
      <div class="md-list-item-text">
        <md-button class="md-raised md-primary" @click="rankup()">Szkolenie</md-button>
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
    getLevelPercentage(){
      return this.troop.level/this.troop.maxLevel*100;
    },
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
.md-list-item-text span {
  text-align: center;
  min-height: 30px;

  height: 40px;
}
.text {
  padding-top: 20px;
}
.textLabel {
  padding-top: 10px;
}
.md-icon {
  width: 0 !important;
  min-height: 110%;
}
.mat {
  margin: auto !important;
  margin-bottom: 5px !important;
}

.md-progress-spinner {
  margin-top: 15px;
  width: auto !important;
}
.md-list-item.surowce span {
  height: inherit;
}
</style>
