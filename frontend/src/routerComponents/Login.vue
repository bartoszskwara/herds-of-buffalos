<template>
  <div id="login">
    <div class="loginPanel">
      <md-content class="md-elevation-3">
        <div class="title">
          <img src="https://vuematerial.io/assets/logo-color.png">
          <div class="md-title">Login</div>
          <div class="md-body-1">Build beautiful apps with Material Design and Vue.js</div>
        </div>
        <div class="form">
          <md-field>
            <label>Nick</label>
            <md-input v-model="nick"></md-input>
          </md-field>

          <md-field :md-toggle-password="false">
            <label>Password</label>
            <md-input v-model="password" type="password"></md-input>
          </md-field>
        </div>
        <div class="buttons">
          <a>Forgot password?</a>
          <md-button class="md-raised md-primary">Log in</md-button>
        </div>

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
.buttons {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  width: 80%;
}
.buttons a:hover {
  cursor: pointer;
}
.md-button {
  margin-right: 0 !important;
}
</style>
