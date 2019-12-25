<template>
  <div id="market">

      <h1>Targowisko</h1>
      <market-offer></market-offer>
      <offers-table class="offers"></offers-table>

  </div>
</template>

<script>
import MarketOffer from "./MarketOffer.vue";
import OffersTable from "./OffersTable.vue";

export default {
  components: {MarketOffer, OffersTable},
  data() {
    return {
      //buildingsArray: buildingsArray,
    }
  }
}
</script>

<style scoped>
.offers {
  margin-top: 20px;
}
</style>
