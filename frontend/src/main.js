import Vue from 'vue'
import TribesHome from './TribesHome.vue'
import 'bootstrap/dist/css/bootstrap.min.css';

Vue.config.productionTip = false

new Vue({
  render: h => h(TribesHome),
}).$mount('#app')
