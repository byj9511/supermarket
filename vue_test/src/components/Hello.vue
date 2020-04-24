<template>
  <div id="hellodemo">
    hello,{{name1}}
  </div>
</template>

<script>
  export default {
    name: 'hello',
    data() {
      return {
        name1: 'byy'
      }
    }
  }
</script>

<style scoped>
  #app {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
    margin-top: 60px;
  }
</style>
