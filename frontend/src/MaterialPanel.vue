<template>
  <div id="materials">
    <md-list class="md-double-line">
      <md-subheader>Surowce</md-subheader>
      <md-list-item layout="row">

        <div class="md-list-item-text">
          <span><md-icon class="mat" :md-src="require('./assets/wood.svg')" /></span>
          <span>{{materials.wood}}</span>
        </div>

        <div class="md-list-item-text">
          <span><md-icon class="mat" :md-src="require('./assets/clay.svg')" /></span>
          <span>{{materials.clay}}</span>
        </div>

        <div class="md-list-item-text">
          <span><md-icon class="mat" :md-src="require('./assets/iron.svg')" /></span>
          <span>{{materials.iron}}</span>
        </div>
      </md-list-item>
    </md-list>
  </div>
</template>

<script>

export default {
  props: {
    materials: Object
  },
}
</script>

<style scoped>
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
</style>
