import Vue from 'vue'
import Home from './Home.vue'
import Scoreboard from './Scoreboard.vue'
import Village from './Village.vue'
import TownHall from './TownHall.vue'
import Barracks from './Barracks.vue'
import Armory from './Armory.vue'
import VueMaterial from 'vue-material'
import 'vue-material/dist/vue-material.min.css'
import 'vue-material/dist/theme/default.css'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

Vue.use(VueMaterial)

Vue.config.productionTip = false

const router = new VueRouter({
  routes: [
    { path: '/', component: Village },
    { path: '/townhall', component: TownHall },
    { path: '/barracks', component: Barracks },
    { path: '/armory', component: Armory },
    { path: '/scoreboard', component: Scoreboard }
  ]
})

new Vue({
  render: h => h(Home),
  router
}).$mount('#app')
