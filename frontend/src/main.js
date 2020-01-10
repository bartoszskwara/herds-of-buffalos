import Vue from 'vue'
import Home from './Home.vue'
import Scoreboard from './routerComponents/Scoreboard.vue'
import Reports from './routerComponents/Reports.vue'
import Village from './routerComponents/Village.vue'
import Map from './routerComponents/Map.vue'
import Profile from './routerComponents/Profile.vue'
import SendTroops from './routerComponents/SendTroops.vue'

import TownHall from './buildingComponents/TownHall.vue'
import Barracks from './buildingComponents/Barracks.vue'
import Pasture from './buildingComponents/Pasture.vue'
import Shipyard from './buildingComponents/Shipyard.vue'
import MachineFactory from './buildingComponents/MachineFactory.vue'
import Armory from './buildingComponents/Armory.vue'
import Market from './buildingComponents/Market.vue'

import VueMaterial from 'vue-material'
import 'vue-material/dist/vue-material.min.css'
import 'vue-material/dist/theme/default.css'
import VueRouter from 'vue-router'
import VueResource from 'vue-resource'

Vue.use(VueRouter)
Vue.use(VueResource)

Vue.use(VueMaterial)

Vue.config.productionTip = false

const router = new VueRouter({
  routes: [
    { path: '/', component: Village },
    { path: '/townhall', component: TownHall },
    { path: '/barracks', component: Barracks },
    { path: '/pasture', component: Pasture },
    { path: '/shipyard', component: Shipyard },
    { path: '/machinefactory', component: MachineFactory },
    { path: '/armory', component: Armory },
    { path: '/market', component: Market },
    { path: '/sendtroops', name: 'sendtroops', component: SendTroops, props: true },
    { path: '/map', component: Map },
    { path: '/scoreboard', component: Scoreboard },
    { path: '/reports', component: Reports },
    { path: '/profile', name: 'profile', component: Profile, props: true }
  ],
  scrollBehavior() {
    document.getElementById('app').scrollIntoView();
  }
})

new Vue({
  render: h => h(Home),
  router
}).$mount('#app')
