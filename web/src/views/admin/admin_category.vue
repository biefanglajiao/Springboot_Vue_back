<template>

  <a-layout-content style="padding: 0 50px">

    <a-layout style="padding: 24px 0; background: #fff">

      <a-layout-content :style="{ padding: '0 24px', minHeight: '280px' }">
        <a-form layout="inline " :model="param" >
        <a-space  direction="vertical">
        <a-input-search
            v-model:value="param.name"
            placeholder="名称"
            enter-button
            @search="handleQuery({page:1,size:pagination.pageSize})"
        />
        </a-space>
        <p>
        <a-button type="primary" @click="add()">
            新增
          </a-button>
        </p>
        </a-form>
        <a-table :columns="columns"
                 :data-source="categorys"
                 :row-key="record => record.id"
                 :pagination="pagination"
                 :loading="loading"
                 @change="handleTableChange"
        >
          <!--        //Q::row-key="record => record.id这个代码的含义是什么-->
          <!--        //A:row-key是一个属性，用来指定数据的主键，这里指定的是id，这样在表格中就可以通过id来唯一标识一行数据-->
          <template #cover="{ text:cover }">
            <img class="img_xhz" v-if="cover" :src="cover" alt="avatar">
            <!--            //todo 图片的处理-->
          </template>
          <template v-slot:action="{text,record}">
            <a-space size="small">
              <a-button type="primary" @click="edit(record)">
                编辑
              </a-button>
              <!--              原有的click方法到confirm里  cacel是放弃 这里不做操作  @cancel="cancel"-->
              <a-popconfirm
                  title="删除后不可回复，是否删除?"
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
  <a-modal title="分类" v-model:visible="modalVisible" :confirm-loading="modalLoading" @ok="handleModalOk">
    <a-form :model="category" :label-col="{ span: 4 }" :wrapper-col="{ span: 14 }" :layout="formLayout">
      <a-form-item label="名称">
        <a-input v-model:value="category.name"/>
      </a-form-item>
      <a-form-item label="顺序">
        <a-input v-model:value="category.sort"/>
      </a-form-item>
      <a-form-item label="父分类">
        <a-input v-model:value="category.parent"/>
      </a-form-item>



    </a-form>
  </a-modal>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from "axios";
import {message} from "ant-design-vue";
import { Tool } from '@/utils/tool';

export default defineComponent({
  name: 'AdminEategory',
  setup() {
    const param=ref();
    param.value={};
    const categorys = ref();
    const pagination = ref({
      current: 1,
      pageSize: 5,
      total: 0
    });
    const loading = ref(false);
    const columns = [
          {
        title: '名称',
        dataIndex: 'name',
      },
      {
        title: '父分类',
        key: 'parent',
        dataIndex: 'parent',

      },
      {
        title: '顺序',
        key: 'sort',
        dataIndex: 'sort',
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
    const category = ref();
    const handleModalOk = () => {//保存
      modalLoading.value = true;

      axios.post("/category/save", category.value).then((response) => {
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
      // category.value = record;
      category.value=Tool.copy(record);
      // Q:这句代码的含义
      // A:这句代码的含义是将record的值赋值给category.value，但是这样做会导致修改category.value的值的时候，record的值也会跟着改变，所以这里需要使用深拷贝的方式，将record的值赋值给category.value
    }
    /***
     *@方法描述: 单击新增按钮方法
     */
    const add = () => {
      modalVisible.value = true;
      category.value = {};
      category.value.cover = "url地址";

    }
    /***
     * @方法描述: 删除按钮方法
     */


    const delet = (id: number) => {
          axios.delete("/category/delete/" + id).then((response) => {

            const data = response.data;


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
      axios.get("/category/list", {
        params: {
          page: params.page,
          size: params.size,
          name: param.value.name,
        }
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          categorys.value = data.content.list;

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
      categorys,
      pagination,
      columns,
      loading,
      handleTableChange,
      handleQuery,

      //   编辑表格相关
      modalLoading,
      modalVisible,
      category,
      handleModalOk,
      edit,
      add,
      delet,
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