<template>
  <div id="offer">
    <div class="offer">
      <md-toolbar :md-elevation="1">
        <span class="md-title">Utwórz ofertę wymiany</span>
      </md-toolbar>
      <div class="supply-demand">
      <md-list class="md-double-line supply">

        <md-subheader>Oferuję</md-subheader>

        <md-list-item>
          <div class="md-list-item-text">
            <span>
            <md-field>
              <md-icon class="mat" :md-src="require('../assets/wood.svg')" />
              <label>Drewno</label>
              <md-input v-model="woodSupply" @keypress="isNumber($event)"></md-input>
            </md-field>
          </span>
          </div>
        </md-list-item>

        <md-list-item>
          <div class="md-list-item-text">
            <span>
            <md-field>
              <md-icon class="mat" :md-src="require('../assets/clay.svg')" />
              <label>Glina</label>
              <md-input v-model="claySupply" @keypress="isNumber($event)"></md-input>
            </md-field>
          </span>
          </div>
        </md-list-item>

        <md-list-item>
          <div class="md-list-item-text">
            <span>
            <md-field>
              <md-icon class="mat" :md-src="require('../assets/iron.svg')" />
              <label>Żelazo</label>
              <md-input v-model="ironSupply" @keypress="isNumber($event)"></md-input>
            </md-field>
          </span>
          </div>
        </md-list-item>
      </md-list>


      <md-list class="md-double-line demand">
        <md-subheader>W zamian za</md-subheader>

        <md-list-item>
          <div class="md-list-item-text">
            <span>
            <md-field>
              <md-icon class="mat" :md-src="require('../assets/wood.svg')" />
              <label>Drewno</label>
              <md-input v-model="woodDemand" @keypress="isNumber($event)"></md-input>
            </md-field>
          </span>
          </div>
        </md-list-item>

        <md-list-item>
          <div class="md-list-item-text">
            <span>
            <md-field>
              <md-icon class="mat" :md-src="require('../assets/clay.svg')" />
              <label>Glina</label>
              <md-input v-model="clayDemand" @keypress="isNumber($event)"></md-input>
            </md-field>
          </span>
          </div>
        </md-list-item>

        <md-list-item>
          <div class="md-list-item-text">
            <span>
            <md-field>
              <md-icon class="mat" :md-src="require('../assets/iron.svg')" />
              <label>Żelazo</label>
              <md-input v-model="ironDemand" @keypress="isNumber($event)"></md-input>
            </md-field>
          </span>
          </div>
        </md-list-item>
      </md-list>
    </div>
    <md-button class="md-raised md-primary" :disabled="offerDisabled()" @click="createOffer()">Utwórz ofertę</md-button>
  </div>
  </div>
</template>

<script>

export default {
  data() {
    return {
      woodSupply: null,
      claySupply: null,
      ironSupply: null,
      woodDemand: null,
      clayDemand: null,
      ironDemand: null,
    }
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
    offerDisabled(){
      if((this.woodSupply != 0 && this.woodSupply != null) || (this.claySupply != 0 && this.claySupply != null) || (this.ironSupply != 0 && this.ironSupply != null)
    || (this.woodDemand != 0 && this.woodDemand != null) || (this.clayDemand != 0 && this.clayDemand != null) || (this.ironDemand != 0 && this.ironDemand != null)){
        return false;
      }
      else return true;
    },
    createOffer(){
      this.woodSupply = null;
      this.claySupply = null;
      this.ironSupply = null;
      this.woodDemand = null;
      this.clayDemand = null;
      this.ironDemand = null;
    }
  }
}
</script>

<style scoped>
.md-toolbar {
  width: 700px;
  margin: auto;
}
.offer {
  max-width: 700px;
  text-align: center;
  margin: auto;
}
.md-list {
  width: 350px;
  margin: 0 !important;
}
.md-list.demand {
  margin-left: 0 !important;
}
.icon {
  width: 50px !important;
}
.md-icon {
  width: 30px;
  min-height: 110%;
}
.supply-demand {
  display: flex;
  flex-direction: row;
}
label {
  text-align: left;
}
</style>
