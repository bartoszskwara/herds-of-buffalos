<template>
  <div id="troop">
    <div class="units">
      <h1>{{header}}</h1>
    <md-list class="md-double-line md-dense" :key="troop.key" v-for="troop in troops">
      <md-subheader>{{troop.unit.label}}</md-subheader>
      <md-list-item :key="troopKind.level" v-for="troopKind in troop.levelsData">

          <div class="md-list-item-text">
            <md-field>
              <label class="inputLabel">Ilość</label>
              <md-icon class="chevron" :md-src="require('../assets/chevron'+troopKind.level+'.svg')"></md-icon>
              <md-input v-model="troopKind.chosen" @keypress="isNumber($event)"></md-input>

            </md-field>
            <span class="md-helper-text">Posiadasz: {{troopKind.amountInCity}}</span>
          </div>
      </md-list-item>
    </md-list>
  </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      chosenCount: [null, null, null, null, null],
    }
  },
  props: {
    troops: Array,
    header: String,
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
    setAllTroops(index, count) {
      this.chosenCount[index] = count;
    }
  },

}
</script>

<style scoped>
.md-list {
  width: 150px !important;
  min-width: 150px !important;
  max-width: 100%;
  height: 413px;
  display: inline-block;
  vertical-align: top;
  border: 1px solid #e6e6e6;
  margin: 0px;
  padding: 0 !important;
}
.md-list-item {
  border-bottom: 0 !important;
}
.md-icon {
  width: 10px !important;
}
.md-input {
  width: 70px;
}
.md-subheader {
  height: 50px;
}
.md-helper-text {
  font-size: 8pt !important;
  text-align: left;
}
.units {
  margin: 10px;
}
h1 {
  font-size: 10pt;
  text-align: left;
}
.inputLabel {
  margin-left: 30px;
}
</style>
