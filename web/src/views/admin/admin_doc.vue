<template>

  <a-layout-content style="padding: 0 50px">

    <a-layout style="padding: 24px 0; background: #fff">

      <a-layout-content :style="{ padding: '0 24px', minHeight: '280px' }">
        <a-form layout="inline" :model="param">
          <a-form-item>
            <a-space direction="vertical">
              <a-input-search
                  v-model:value="param.name"
                  placeholder="名称"
                  enter-button
                  @search="handleQueryByname(param.name)"
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
                 :data-source="docs"
                 :row-key="record => record.id"
                 :pagination="false"
                 :loading="loading"

        >
          <!--        //Q::row-key="record => record.id这个代码的含义是什么-->
          <!--        //A:row-key是一个属性，用来指定数据的主键，这里指定的是id，这样在表格中就可以通过id来唯一标识一行数据-->

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
  <a-modal title="文档" v-model:visible="modalVisible" :confirm-loading="modalLoading" @ok="handleModalOk">
    <a-form :model="doc" :label-col="{ span: 4 }" :wrapper-col="{ span: 14 }">
      <a-form-item label="名称">
        <a-input v-model:value="doc.name"/>
      </a-form-item>
      <a-form-item label="顺序">
        <a-input v-model:value="doc.sort"/>
      </a-form-item>
      <a-form-item label="父文档">
        <!--        <a-input v-model:value="doc.parent"/>-->
        <a-select
            ref="select"
            v-model:value="doc.parent"
        >
          <a-select-option value="0">无</a-select-option>
          <a-select-option v-for="c in docslevel" :key="c.id" :value="c.id" :disabled="doc.id==c.id">
            {{ c.name }}
          </a-select-option>

        </a-select>

      </a-form-item>


    </a-form>
  </a-modal>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from "axios";
import {message} from "ant-design-vue";
import {Tool} from '@/utils/tool';

export default defineComponent({
  name: 'AdminDoc',
  setup() {
    const param = ref();
    param.value = {};

    const loading = ref(false);
    const columns = [
      {
        title: '名称',
        dataIndex: 'name',
      },
      {
        title: '父文档',
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
    const doc = ref();
    const handleModalOk = () => {//保存
      modalLoading.value = true;

      axios.post("/doc/save", doc.value).then((response) => {
        modalLoading.value = false;//有返回就关闭加载
        const data = response.data;//data==common,resp
        if (data.success) {
          modalVisible.value = false;//关闭视图
          //重新加载列表
          handleQuery();
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
      // doc.value = record;
      doc.value = Tool.copy(record);
      // Q:这句代码的含义
      // A:这句代码的含义是将record的值赋值给doc.value，但是这样做会导致修改doc.value的值的时候，record的值也会跟着改变，所以这里需要使用深拷贝的方式，将record的值赋值给doc.value
    }
    /***
     *@方法描述: 单击新增按钮方法
     */
    const add = () => {
      modalVisible.value = true;
      doc.value = {};
      doc.value.cover = "url地址";

    }
    /***
     * @方法描述: 删除按钮方法
     */


    const delet = (id: number) => {
      axios.delete("/doc/delete/" + id).then((response) => {

        const data = response.data;


        //重新加载列表
        handleQuery();
      });
    }
    /**
     * @方法描述: 存放普通的文档数据
     */
    const docs = ref();
    /***
     * @方法描述: 存放经过树形结构处理的文档树
     * @变量docslevel解释：一级文档树，children属性时二级文档
     *                           [{
     *                             id:"",
     *                             name:"",
     *                             chileren:[{
     *                                id:"",
     *                             name:"",
     *                             }]
     *                           }
     * 以此类推
     * 对这个数据处理以后会带动docs发生改变  所以不用返回这个 直接返回docs就行  写出来时方便理解
     */
    const docslevel = ref();
    /***
     * @方法描述: 数据查询方法
     * @param params
     */
    const handleQueryByname = (params: any) => {
      loading.value = true;
      axios.get("/doc/list", {
        params: {
          name: params,
        }
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success && (params == null || params == "")) {
          docs.value = data.content.list;
          docslevel.value = [];
          docs.value = Tool.array2Tree(docs.value, 0);
        } else if (data.success && params != null) {
          docs.value = data.content.list;
        } else {
          message.error(data.message);
        }
      });
    };
    /***
     * @方法描述: 数据获取方法
     */
    const handleQuery = () => {
      loading.value = true;
      axios.get("/doc/all",).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          docs.value = data.content;
          console.log("初始数据" , docs.value);
          docslevel.value = [];
          docs.value = Tool.array2Tree(docs.value, 0);
          docslevel.value = docs.value;

        } else {
          message.error(data.message);
        }
      });
    };


    /**
     * @方法描述: 初始进入页面就查一次数据
     */
    onMounted(() => {
      handleQuery();
    });


    return {
      //列表
      param,
      docs,

      columns,
      loading,
      handleQuery,
      handleQueryByname,
      docslevel,


      //   编辑表格相关
      modalLoading,
      modalVisible,
      doc,
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