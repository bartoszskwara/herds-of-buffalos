<template>
  <div id="troop">
    <md-list class="md-double-line">
      <md-subheader>Cel wyprawy</md-subheader>

      <md-list-item class="md-inset">
        <md-avatar>
          <img src="https://placeimg.com/40/40/people/5" alt="People">
        </md-avatar>
        <div class="md-list-item-text">
          <span>{{village.user.name}}</span>
          <span>Gracz</span>
        </div>
      </md-list-item>
      <md-list-item class="md-inset">
        <img :src="setVillageImage(village)" />
        <div class="md-list-item-text">
          <span>{{village.name}} <span class="punkty">({{village.points}}p.)</span></span>
          <span>Nazwa wioski</span>
        </div>
      </md-list-item>

    </md-list>

  </div>
</template>

<script>
export default {
  data() {
    return {
    }
  },
  props: {
    village: Object,
  },
  methods: {
    setVillageImage(village){
      var image = require.context('./assets/', false, /\.png$/)
      if(village.points < 300){
        return image('./wioska1.png')
      }
      else if(village.points >= 300 && village.points < 1000){
        return image('./wioska2.png')
      }
      else if(village.points >= 1000 && village.points < 3000){
        return image('./wioska3.png')
      }
      else if(village.points >= 3000){
        return image('./wioska4.png')
      }
    },
  },

}
</script>

<style scoped>
.md-list {
  width: 420px;
  max-width: 100%;
  display: inline-block;
  vertical-align: top;
  border: 1px solid #e6e6e6;
  margin: 20px;
  padding: 0;
}
.md-list-item-text img {
  width: 40px;
}
.md-icon.city {
  width: 40px !important;
  height: 110%;
}
.punkty {
  font-size: 8pt;
  font-style: italic;
}
</style>
