<template>
  <el-tree
    :data="menus"
    :props="categoryProps"
    ref="menus"
    node-key="catId"
    :expand-on-click-node="true"
    @node-click="nodeClick">
  </el-tree>
</template>

<script>
  export default {

    data () {
      return {
        menus: [],
        categoryProps: {
          label: 'name',
          children: 'children'
        }
      }
    },
    methods: {
      getCategory () {
        this.$http({
          url: this.$http.adornUrl('/product/category/list/tree'),
          method: 'get'
          //  此处用到了JS的解构，和python拆包类似
        }).then(({data}) => {
          // console.log(data.tree)
          this.menus = data.tree
        })
      },
      nodeClick (data, node, component) {
        // console.log('子组件被点击', data, node, component)
        this.$emit('tree-node-click', data, node, component)
      }
    },
    created () {
      this.getCategory()
    }
  }
</script>

<style scoped>

</style>
