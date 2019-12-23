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
      <div class="md-list-item-text divider surowce">

        <span><md-icon class="mat" :md-src="require('./assets/attack.svg')" /></span>
        <span class="textLabel">{{troop.attr.attack}}</span>
      </div>
      <div class="md-list-item-text surowce">

        <span><md-icon class="mat" :md-src="require('./assets/defense.svg')" /></span>
        <span class="textLabel">{{troop.attr.defense}}</span>
      </div>



      <div class="md-list-item-text divider surowce">
        <span><md-icon class="mat" :md-src="require('./assets/wood.svg')" /></span>
        <span class="textLabel">{{troop.price.wood}}</span>
      </div>

      <div class="md-list-item-text surowce">
        <span><md-icon class="mat" :md-src="require('./assets/clay.svg')" /></span>
        <span class="textLabel">{{troop.price.clay}}</span>
      </div>

      <div class="md-list-item-text surowce">
        <span><md-icon class="mat" :md-src="require('./assets/iron.svg')" /></span>
        <span class="textLabel">{{troop.price.iron}}</span>
      </div>

      <div class="md-list-item-text recruit">
        <md-field>
          <label>Rekrutacja</label>
          <md-input v-model="recruitCount" @keypress="isNumber($event)"></md-input>
        </md-field>
      </div>

      <div class="md-list-item-text">
        <md-button class="md-raised md-primary" @click="recruit()" :disabled="recruitCount > 0 ? false : true">Rekrutuj</md-button>
      </div>
    </md-list-item>


    <md-snackbar md-position="center" :md-duration="snackbarDuration" :md-active.sync="showSnackbar" md-persistent>
      <span>Rozpoczęto rekrutację {{snackbarRecruitCount}} jednostek typu {{troop.name}}.</span>
    </md-snackbar>
  </div>
</template>

<script>
export default {
  data() {
    return {
      recruitCount: null,
      snackbarRecruitCount: 0,
      showSnackbar: false,
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
    isNumber: function(evt) {
      evt = (evt) ? evt : window.event;
      var charCode = (evt.which) ? evt.which : evt.keyCode;
      if ((charCode > 31 && (charCode < 48 || charCode > 57))) {
        evt.preventDefault();
      } else {
        return true;
      }
    },
    recruit() {
      this.showSnackbar = true;
      this.snackbarRecruitCount = this.recruitCount;
      this.recruitCount = null;
    }
  }
}
</script>

<style scoped>
.md-list-item-text span {
  text-align: center;
  min-height: 30px;
  /* border: 1px solid red; */
  height: 40px;
}
.divider {
  border-left: 1px solid grey;
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
.md-list-item-text {
  flex-grow: 5 !important;
}
.md-list-item-text.surowce {
  flex-grow: 3 !important;
}
.md-list-item-text.recruit {
  flex-grow: 6 !important;
  padding-left: 30px;
  padding-right: 30px;
}
</style>
