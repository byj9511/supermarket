<template>
  <div id="app" class="test">
    <img src="./assets/logo.png">
      <router-view/>
<!--    路径视图，其内部的内容会随着路径二被替换-->
<!--    视图功能-->
<!--    不能使用原生的a标签-->
<!--    <a href="/hello">去hello</a>-->
<!--    要用router进行路由-->
      <router-link to="/hello">去hello</router-link>
      <router-link to="/elementUI">去ElementUIdemo</router-link>

  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
