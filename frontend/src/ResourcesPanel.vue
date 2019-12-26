<template>
  <div id="resources">
    <md-list class="md-double-line">
      <md-subheader>Surowce</md-subheader>
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
  props: {
    resources: Object
  },
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
</style>
