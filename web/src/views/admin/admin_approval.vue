<template>

  <a-layout-content style="padding: 0 50px">

    <a-layout style="padding: 24px 0; background: #fff">

      <a-layout-content :style="{ padding: '0 24px', minHeight: '280px' }">
        <a-form layout="inline" :model="param">
          <a-form-item>
            <a-space direction="vertical">
              <a-input-search
                  v-model:value="param.name"
                  placeholder="根据邮箱名称查询"
                  enter-button
                  @search="select(param.name)"
              />
            </a-space>
          </a-form-item>
          <a-form-item>
            <p>
              <a-button type="primary" @click="add()">
                根据参与种类查询 已审批在前 未审批在后
              </a-button>
            </p>
          </a-form-item>
          <a-form-item>
            <p>
              <a-button type="primary" @click="add()">
                根据参与种类查询 已审批在后 未审批在前
              </a-button>
            </p>
          </a-form-item>
          <a-form-item>
            <p>
              <a-button type="primary" @click="add()">
                根据审批结果查询 参与类在前 未参与类在后
              </a-button>
            </p>
          </a-form-item>
          <a-form-item>
            <p>
              <a-button type="primary" @click="add()">
                根据审批结果查询 参与类在后 未参与类在前
              </a-button>
            </p>
          </a-form-item>
        </a-form>
        <a-table :columns="columns"
                 :data-source="approvals"
                 :row-key="record => record.id"
                 :pagination="pagination"
                 :loading="loading"
                 @change="handleTableChange"
        >
          <template v-slot:option="{text,record}">
            <a-tag color="green" v-if="record.option==0">提供帮助</a-tag>
            <a-tag color="yellow" v-if="record.option==1">寻求帮助</a-tag>

          </template>
          <template v-slot:approvaled="{text,record}">
            <a-tag color="red" v-if="record.approval==0">未审批</a-tag>
            <a-tag color="blue" v-if="record.approval==1">已审批</a-tag>

          </template>
          <template v-slot:action="{text,record}">

            <a-space size="small">
              <a-button type="primary" @click="approvaled(record)" :disabled="record.approval">
                审核通过
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

</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from "axios";
import {message} from "ant-design-vue";
import {Tool} from '@/utils/tool';

export default defineComponent({
  name: 'AdminEbook',
  setup() {
    const param = ref();
    param.value = {};
    const approvals = ref();
    const pagination = ref({
      current: 1,
      pageSize: 5,
      total: 0
    });
    const loading = ref(false);
    const columns = [
      {
        title: 'id',
        dataIndex: 'id',
      },

      {
        title: '内容',
        dataIndex: 'context',
      }, {
        title: '邮箱',
        dataIndex: 'email',
      },
      {
        title: '姓名',
        dataIndex: 'name',
      },
      {
        title: '居住地',
        dataIndex: 'location',
      },
      {
        title: '种类',
        slots: {customRender: 'option'}
      },
      {
        title: '是否审批',
        slots: {customRender: 'approvaled'}
      },

      {
        title: '文档名称',
        dataIndex: 'docname',
      },
      {
        title: '文档id',
        dataIndex: 'docid',

      },


      {
        title: 'Action',
        slots: {customRender: 'action'}
      }
    ];
    /**
     * c查询按钮方法
     */
    const select = (email:string) => {
      if (Tool.isEmpty(email)) {
       handleQueryCategory();
        return;
      }
      axios.get("/approval/select/"+email).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {

          approvals.value = data.content;
        } else {
          message.error(data.message);

        }
      });
    }

    /***
     * @方法描述: 数据查询方法
     * @param params
     */
    const handleQueryCategory = () => {
      loading.value = true;
      axios.get("/approval/list",).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {

          handleQuery({
            page: 1,
            size: 5
          });
        } else {
          message.error(data.message);
        }
      });
    };

    /***
     * @方法描述: 删除按钮方法
     */
    const delet = (id: number) => {
      axios.delete("/approval/delete/" + id).then((response) => {
        if (response.data.success) {
          message.success(response.data.message);
        } else {
          message.error(response.data.message);
        }
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
      axios.get("/approval/list").then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          approvals.value = data.content;
          console.log("bbbbbbbbbbbbbb", data.content);
          console.log("aaaaaaaaaaaaaaaaaaaaaaaa", approvals.value);

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
      /***
       *
       *    handleQueryCategory();
       *       handleQuery({
       *         page: 1,
       *         size: pagination.value.pageSize,
       *       });
       * 这两个方法是异步执行的  如果获取分类在获取电子数后面 ，电子书中给分类渲染的方法会报错误（为空--因为没有取到 ）
       * 改进 把获取电子数的方法 放到获取分类的方法中
       */
      handleQueryCategory();
    });


    return {
      //列表
      param,
      approvals,
      pagination,
      columns,
      loading,
      handleTableChange,
      handleQuery,


      delet,
      select

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