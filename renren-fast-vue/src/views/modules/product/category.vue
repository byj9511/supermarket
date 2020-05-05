<template>
  <div>
    <el-row>
      <el-button type="danger" icon="el-icon-delete" circle @click="this.deleteBatchIds"/>
    </el-row>
    <el-tree
      :data="menus"
      :props="categoryProps"
      show-checkbox
      ref="menus"
      node-key="catId"
      :default-expanded-keys="expandkey"
      :expand-on-click-node="true">
      <!--      span标签中用了解构表达式，用变量tree中获取属性值-->
      <span class="custom-tree-node" slot-scope="{ node, data }">
        <span>{{ node.label }}</span>
        <span>
<!--          总共三级分类，所以只在1、2级菜单显示append-->
          <el-button
            v-if="node.childNodes.length>0"
            type="text"
            size="mini"
            @click="() => append(data)">
            Append
          </el-button>
          <el-button
            v-if="node.childNodes.length===0"
            type="text"
            size="mini"
            @click="() => remove(node, data)">
            Delete
          </el-button>
          <el-button
            type="text"
            size="mini"
            @click="() => edit(data)">
            Edit
          </el-button>
        </span>
      </span>
    </el-tree>
    <el-dialog :title="form.title" :visible.sync="form.dialogFormVisible">
      <el-form :model="form">
        <el-form-item label="分类名称" :label-width="form.formLabelWidth">
          <el-input label="name" v-model="category.name" autocomplete="off"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="form.dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>
  </div>

</template>

<script>

  export default {

    data () {
      return {
        menus: [],
        categoryProps: {
          label: 'name',
          children: 'children'
        },
        expandkey: [],
        form: {
          title: '',
          dialogType: '',
          dialogFormVisible: false,
          formLabelWidth: '70px'
        },
        category: {
          name: '',
          parentCid: 0,
          showStatus: 1,
          sort: 0,
          catLevel: ''
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
      append (data) {
        this.form.title = '添加分类'
        this.form.dialogType = 'add'
        this.category.name = ''
        this.category.catLevel = data.catLevel + 1
        this.category.parentCid = data.catId
        // 准备完成后显示表单
        this.form.dialogFormVisible = true
      },
      edit (data) {
        this.form.title = '修改分类'
        this.form.dialogType = 'edit'
        this.category = data
        console.log(data)
        // 准备完成后显示表单
        this.form.dialogFormVisible = true
      },
      submitForm () {
        if (this.form.dialogType === 'edit') {
          this.$http({
            url: this.$http.adornUrl('/product/category/update'),
            method: 'post',
            data: this.$http.adornData(this.category, false)
          }).then(() => {
            // 删除后保持展开状态
            this.expandkey = [this.category.catId]
            // 更新属性列表
            this.getCategory()
            this.$message({
              type: 'success',
              message: '修改成功!'
            })
          })
        } else {
          this.$http({
            url: this.$http.adornUrl('/product/category/save'),
            method: 'post',
            data: this.$http.adornData(this.category, false)
          }).then(() => {
            // 删除后保持展开状态
            this.expandkey = [this.category.parentCid]
            // 更新属性列表
            this.getCategory()
            this.$message({
              type: 'success',
              message: '添加成功!'
            })
          })
        }
        this.form.dialogFormVisible = false
      },
      deleteBatchIds () {
        console.log(this.$refs.menus.getCheckedKeys())
        let batchIds = this.$refs.menus.getCheckedKeys()
        this.$confirm(`此操作将永久删除[${batchIds}], 是否继续?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/product/category/delete'),
            method: 'post',
            data: this.$http.adornData(batchIds, false)
          }).then(() => {
            // 更新属性列表
            this.getCategory()
            this.$message({
              type: 'success',
              message: '删除成功!'
            })
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        })
      },
      remove (node, data) {
        console.log(node, data)
        this.$confirm(`此操作将永久删除[${data.name}], 是否继续?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          var ids = [node.data.catId]
          this.$http({
            url: this.$http.adornUrl('/product/category/delete'),
            method: 'post',
            data: this.$http.adornData(ids, false)
          }).then(() => {
            // 删除后保持展开状态
            this.expandkey = [node.parent.data.catId]
            // 更新属性列表
            this.getCategory()
            this.$message({
              type: 'success',
              message: '删除成功!'
            })
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        })
      }
    },
    created () {
      this.getCategory()
    }
  }
</script>

<style scoped>

</style>
