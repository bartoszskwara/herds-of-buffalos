<template>
  <div id="register">
    <div class="loginPanel">
      <md-content class="md-elevation-3">
        <div class="title">
          <img src="https://vuematerial.io/assets/logo-color.png">
          <div class="md-title">Register</div>
          <div class="md-body-1">Build beautiful apps with Material Design and Vue.js</div>
        </div>
        <div class="form">
          <md-field>
            <label>Nick</label>
            <md-input v-model="nick" maxlength="20" minlength="5"></md-input>
            <span class="md-helper-text">Tak będą cię widzieć inni gracze</span>
          </md-field>
          <md-field>
            <label>Email</label>
            <md-input v-model="email" type="email"></md-input>
          </md-field>
          <md-field>
            <label>Password</label>
            <md-input v-model="password" type="password"></md-input>
          </md-field>
        </div>
        <md-button class="md-raised md-primary">Register</md-button>

      </md-content>
    </div>
  </div>
</template>

<script>

export default {
  data(){
    return {

    }
  },

}
</script>

<style scoped>
.md-content {
  width: 400px;
  height: 500px;
  background-color: white !important;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
.form {
  width: 80%;
}
.title {
  text-align: center;
  margin-bottom: 30px;
}
.title img {
  margin-bottom: 16px;
  max-width: 80px;
}

</style>
