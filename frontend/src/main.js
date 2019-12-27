import Vue from 'vue'
import Home from './Home.vue'
import Scoreboard from './Scoreboard.vue'
import Reports from './Reports.vue'
import Village from './Village.vue'
import TownHall from './TownHall.vue'
import Barracks from './Barracks.vue'
import Armory from './Armory.vue'
import Map from './Map.vue'
import Market from './Market.vue'
import Profile from './Profile.vue'
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
    { path: '/armory', component: Armory },
    { path: '/market', component: Market },
    { path: '/map', component: Map },
    { path: '/scoreboard', component: Scoreboard },
    { path: '/reports', component: Reports },
    { path: '/profile', name: 'profile', component: Profile, props: true}
  ]
})

new Vue({
  render: h => h(Home),
  router
}).$mount('#app')
