<template>
  <div id="resources">
    <md-list class="md-double-line md-dense">
      <md-subheader>Surowce</md-subheader>
      <md-list-item>
        <div class="md-list-item-text">
          <span v-bind:style="setGranaryStyle()" class="progressValue">{{allResources}} / {{granaryCapacity}}</span>
          <span><md-progress-bar class="md-accent progressBar" md-mode="determinate" :md-value="progressCapacity"></md-progress-bar></span>
          <span class="progressLabel">Pojemność spichlerza</span>
        </div>
      </md-list-item>
      <md-list-item layout="row">
        <div class="md-list-item-text">
          <span><md-icon class="mat" :md-src="require('./assets/wood.svg')" /></span>
          <span>{{resources.wood}}</span>
        </div>

        <div class="md-list-item-text">
          <span><md-icon class="mat" :md-src="require('./assets/clay.svg')" /></span>
          <span>{{resources.clay}}</span>
        </div>

        <div class="md-list-item-text">
          <span><md-icon class="mat" :md-src="require('./assets/iron.svg')" /></span>
          <span>{{resources.iron}}</span>
        </div>
      </md-list-item>
    </md-list>
  </div>
</template>

<script>

export default {
  data() {
    return {
      granaryCapacity: 150000,
    }
  },
  props: {
    resources: Object
  },
  methods: {
    setGranaryStyle(){
      var style;
      if(this.granaryCapacity - this.allResources <= 0.1*this.granaryCapacity
        || this.granaryCapacity - this.allResources <= 500){
        style = {color: 'red'};
      }
      else {
        style = {color: 'black'};
      }
      return style;
    }
  },
  computed: {
  allResources: function () {
    return this.resources.wood + this.resources.clay + this.resources.iron;
  },
  progressCapacity: function() {
    return this.allResources/this.granaryCapacity*100;
  }
}
}
</script>

<style scoped>
.md-list {
  margin-left: 0 !important;
}
.md-icon {
  width: 0 !important;
  min-height: 110%;
}
.mat {
  margin: auto !important;
  margin-bottom: 5px !important;
}
.md-list-item-text span {
  text-align: center !important;
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
