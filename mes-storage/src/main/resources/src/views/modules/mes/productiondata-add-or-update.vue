<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="" prop="clientId">
      <el-input v-model="dataForm.clientId" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="cellId">
      <el-input v-model="dataForm.cellId" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="result">
      <el-input v-model="dataForm.result" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="data">
      <el-input v-model="dataForm.data" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="note">
      <el-input v-model="dataForm.note" placeholder=""></el-input>
    </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  export default {
    data () {
      return {
        visible: false,
        dataForm: {
          id: 0,
          clientId: '',
          cellId: '',
          result: '',
          data: '',
          note: ''
        },
        dataRule: {
          clientId: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          cellId: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          result: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          data: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          note: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.id = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.id) {
            this.$http({
              url: this.$http.adornUrl(`/mes/productiondata/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.clientId = data.productionData.clientId
                this.dataForm.cellId = data.productionData.cellId
                this.dataForm.result = data.productionData.result
                this.dataForm.data = data.productionData.data
                this.dataForm.note = data.productionData.note
              }
            })
          }
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/mes/productiondata/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'clientId': this.dataForm.clientId,
                'cellId': this.dataForm.cellId,
                'result': this.dataForm.result,
                'data': this.dataForm.data,
                'note': this.dataForm.note
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.visible = false
                    this.$emit('refreshDataList')
                  }
                })
              } else {
                this.$message.error(data.msg)
              }
            })
          }
        })
      }
    }
  }
</script>
