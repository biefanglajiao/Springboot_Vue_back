<template>

  <a-layout-content style="padding: 0 50px">

    <a-layout style="padding: 24px 0; background: #fff">

      <a-layout-content :style="{ padding: '0 24px', minHeight: '280px' }">
        <a-form layout="inline" :model="param">
          <a-form-item>
            <a-space direction="vertical">
              <a-input-search
                  v-model:value="param.loginName"
                  placeholder="登录名"
                  enter-button
                  @search="handleQuery({page:1,size:pagination.pageSize})"
              />
            </a-space>
          </a-form-item>
          <a-form-item>
            <p>
              <a-button type="primary" @click="add()">
                新增
              </a-button>
            </p>
          </a-form-item>
        </a-form>
        <a-table :columns="columns"
                 :data-source="users"
                 :row-key="record => record.id"
                 :pagination="pagination"
                 :loading="loading"
                 @change="handleTableChange"
        >

          <template v-slot:action="{text,record}">

            <a-space size="small">
              <a-button type="primary" @click="edit(record)">
                编辑
              </a-button>
              <a-button type="primary" @click="resetPassword(record)">
                重置密码
              </a-button>
              <!--              原有的click方法到confirm里  cacel是放弃 这里不做操作  @cancel="cancel"-->
              <a-popconfirm
                  title="删除后不可恢复，是否删除?"
                  ok-text="是"
                  cancel-text="否"
                  @confirm="delet(record.id)"
              >
                <a-button type="danger">
                  <!--                delete是关键字  @click="delet(record.id)-->
                  删除
                </a-button>
              </a-popconfirm>

            </a-space>
          </template>
        </a-table>


      </a-layout-content>
    </a-layout>
  </a-layout-content>
  <!--  //Q::confirm-loading的含义-->
  <!--  //A:confirm-loading是一个属性，当点击确定按钮时，会调用handleModalOk方法，handleModalOk方法会调用axios的post方法，保存数据，然后重新加载列表-->
  <a-modal title="用户表单" v-model:visible="modalVisible" :confirm-loading="modalLoading" @ok="handleModalOk">
    <a-form :model="user" :label-col="{ span: 4 }" :wrapper-col="{ span: 14 }" :layout="formLayout">
      <a-form-item label="登录名">
        <a-input v-model:value="user.loginName" :disabled="!!user.id"/>
<!--        :disabled="user.id" id是主键，新增时id为空，修改时id不为空，所以新增时可以输入，修改时不可以输入-->
<!--        !!可以绕过类型校验（前端f12的报错提示）-->
      </a-form-item>
      <a-form-item label="昵称">
        <a-input v-model:value="user.name"/>
      </a-form-item>
      <a-form-item label="密码"  v-show="!user.id">
        <a-input v-model:value="user.password"/>
<!--        v-show  是否展示、、此时新增显示，编辑不显示-->
      </a-form-item>
    </a-form>
  </a-modal>

  <a-modal title="重置密码" v-model:visible="resetModalVisible" :confirm-loading="resetModalLoading" @ok="handleresetModalOk">
    <a-form :model="user" :label-col="{ span: 4 }" :wrapper-col="{ span: 14 }" :layout="formLayout">
      <a-form-item label="新密码"  >
        <a-input v-model:value="user.password" />
<!--        v-show  是否展示、、此时新增显示，编辑不显示-->
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from "axios";
import {message} from "ant-design-vue";
import {Tool} from '@/utils/tool';
// 对外部引用爆红的解决方法
declare let hexMd5: any;
declare let KEY: any;

export default defineComponent({
  name: 'AdminUser',
  setup() {
    const param = ref();
    param.value = {};
    const users = ref();
    const pagination = ref({
      current: 1,
      pageSize: 5,
      total: 0
    });
    const loading = ref(false);
    const columns = [
      {
        title: '登录名',
        dataIndex: 'loginName',
      },
      {
        title: '昵称',
        dataIndex: 'name',
      },
      {
        title: '密码',
        dataIndex: 'password',
      },
      {
        title: 'Action',
        key: 'action',
        slots: {customRender: 'action'}
      }
    ];
    /*****
     * @方法描述: 编辑表单的提交
     */
    const modalLoading = ref<boolean>(false);
    const modalVisible = ref<boolean>(false);
    const resetModalLoading = ref<boolean>(false);
    const resetModalVisible = ref<boolean>(false);
    const user = ref();
    const categoryIds = ref();
    const handleModalOk = () => {//保存
      modalLoading.value = true;
      user.value.password=hexMd5(user.value.password+KEY);
        axios.post("/user/save", user.value).then((response) => {
          modalLoading.value = false;//有返回就关闭加载
          const data = response.data;//data==common,resp
          if (data.success) {
            modalVisible.value = false;//关闭视图
            //重新加载列表
            handleQuery({
              page: pagination.value.current,
              size: pagination.value.pageSize
            });
          } else {
            message.error(data.message);
          }

        });

    };
    /***
     *@方法描述: 单击编辑按钮方法
     */
    const edit = (record: any) => {
      modalVisible.value = true;
      // user.value = record;
      user.value = Tool.copy(record);
    }

    const handleresetModalOk = () => {//保存
      resetModalLoading.value = true;
      user.value.password=hexMd5(user.value.password+KEY);
        axios.post("/user/reset-password", user.value).then((response) => {
          resetModalLoading.value = false;//有返回就关闭加载
          const data = response.data;//data==common,resp
          if (data.success) {
            resetModalVisible.value = false;//关闭视图
            //重新加载列表
            handleQuery({
              page: pagination.value.current,
              size: pagination.value.pageSize
            });
          } else {
            message.error(data.message);
          }

        });

    };
    /***
     *@方法描述: 单击重置密码按钮方法
     */
    const resetPassword= (record: any) => {
      resetModalVisible.value = true;
      // user.value = record;
      user.value = Tool.copy(record);
      user.value.password="";
      //todo 前端密码校验
    }



    /***
     *@方法描述: 单击新增按钮方法
     */
    const add = () => {
      modalVisible.value = true;
      user.value = {};


    }
    /***
     * @方法描述: 删除按钮方法
     */


    const delet = (id: number) => {
      axios.delete("/user/delete/" + id).then((response) => {




        //重新加载列表
        handleQuery({
          page: pagination.value.current,
          size: pagination.value.pageSize
        });
      });
    }

    /***
     * @方法描述: 数据查询方法
     */
    const handleQuery = (params: any) => {
      loading.value = true;

      axios.get("/user/list", {
        params: {
          page: params.page,
          size: params.size,
          loginName: param.value.loginName,
        }
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          users.value = data.content.list;

          //重置分页按钮
          pagination.value.current = params.page;
          pagination.value.total = data.content.total;
        } else {
          message.error(data.message);
        }
      });
    };


    /***
     * @方法描述: 表格点击页面触发
     */
    const handleTableChange = (pagination: any) => {
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize
      });

    };


    /**
     * @方法描述: 初始进入页面就查一次数据
     */
    onMounted(() => {
      handleQuery({
        page: 1,
        size: pagination.value.pageSize,
      });
    });


    return {
      //列表
      param,
      users,
      pagination,
      columns,
      loading,
      handleTableChange,
      handleQuery,

      //   编辑表格相关
      modalLoading,
      modalVisible,
      user,
      handleModalOk,
      edit,
      add,
      delet,


      //   重置密码相关
      resetModalLoading,
      resetModalVisible,
      handleresetModalOk,
      resetPassword,

      /**
       * 分类相关
       */
      categoryIds,

    }
  }
});
</script>
<style scoped>
/*scoped 作用  限制样式只在当前组件生效*/
.img_xhz {
  width: 50px;
  height: 50px;
  line-height: 50px;
  border-radius: 8%;
  margin: 5px 0;


}</style>