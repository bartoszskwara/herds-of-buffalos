<template>
  <div id="troop">
    <md-list class="md-double-line md-dense">
    <md-toolbar :md-elevation="1">
      <span class="md-title">Kolejka rekrutacji</span>
    </md-toolbar>
      <md-list-item :key="task.id" v-for="task in queue">
        <div class="md-list-item-text" style="flex-grow: 2">
          <span>{{task.label}}</span>
          <span>Jednostka</span>
        </div>
        <div class="md-list-item-text level" style="flex-grow: 0.5">
          <md-icon class="chevron" :md-src="require('../assets/chevron'+task.level+'.svg')"></md-icon>
        </div>

        <div class="md-list-item-text" style="flex-grow: 2">
          <span>{{task.amount}}</span>
          <span>Ilość</span>
        </div>
        <div class="md-list-item-text" style="flex-grow: 2">
          <span>{{task.remainingTime}}</span>
          <span>Pozostało</span>
        </div>
        <div class="md-list-item-text" style="flex-grow: 2">
          <span>{{task.readyAt}}</span>
          <span>Zakończenie</span>
        </div>
        <div class="md-list-item-text btn" style="flex-grow: 1">
          <md-button class="md-raised md-accent">Przerwij</md-button>
        </div>

      </md-list-item>
    </md-list>
    <md-snackbar md-position="center" :md-duration="snackbarDuration" :md-active.sync="showSnackbar" md-persistent>
      <span>{{snackbarText}}</span>
    </md-snackbar>
  </div>
</template>

<script>
import queue from "../assets/recruitmentQueue.js";

export default {
  data() {
    return {
      queue: queue,
      snackbarRecruitCount: 0,
      snackbarText: String,
      showSnackbar: false,
      snackbarDuration: 3000,
    }
  },
  props: {

  },
  methods: {

  },

}
</script>

<style scoped>
.md-list-item-text {
  text-align: center !important;
}
.md-list {
  width: 95%;
}
.md-list-item {
  border: 1px solid #d6d6d6;
  border-top: 0;
}
.md-list-item.spinner {
  border: 0;
  margin: 10px;
}
.md-list-item-text span {
  text-align: center;

}
.md-icon {
  min-height: 110%;
}

.md-helper-text {
  font-size: 8pt !important;
}

.level {
  padding-left: 20px;
}
.chevron {
  height: 110%;
  text-align: center;
  width: 40px;
}
.btn {
  padding: 0 50px;
}
</style>
