<template>

  <a-layout-content style="padding: 0 50px">

    <a-layout style="padding: 24px 0; background: #fff">

      <a-layout-content :style="{ padding: '0 24px', minHeight: '280px' }">
        <a-table :columns="columns"
                 :data-source="ebooks"
                 :row-key="record => record.id"
                 :pagination="pagination"
                 :loading="loading"
                 @change="handleTableChange"
        >
          <template #cover="{ text:cover }">
            <img class="img_xhz" v-if="cover" :src="cover" alt="avatar">
<!--            //todo 图片的处理-->
          </template>
     <template v-slot:action="{text,record}">
        <a-space size="small">
          <a-button type="primary">
            编辑
          </a-button>
          <a-button type="danger">
            删除
          </a-button>
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

export default defineComponent({
  name: 'AdminEbook',
  setup(){
    const ebooks=ref();
    const pagination=ref({
      current: 1,
      pageSize: 2,
      total:0
    });
    const loading =ref(false);
    const columns=[{
      title:'封面',
      dataIndex:'cover',
    //   渲染
      slots:{customRender:'cover'}
    },
      {
        title:'名称',
        dataIndex:'name',
      },
      { title:'分类一',
        key:'category1Id',
        dataIndex:'category1Id',

      },
      {
        title:'分类二',
        key:'category2Id',
        dataIndex:'category2Id',
      },
      {
        title:'文档数',
        dataIndex:'docCount',
      },
      {
        title:'阅读数',
        dataIndex:'docCount',
      },
      {
        title:'点赞数',
        dataIndex:'docCount',
      },
      {
        title:'Action',
        key:'action',
        slots:{customRender:'action'}
      }
    ];


    /***
     * @方法描述: 数据查询方法
     */
    const handleQuery=(params:any)=>{
      loading.value=true;
      axios.get("/ebook/list",{
        params: {
          page: params.page,
          size: params.size,
        }
      }).then((response)=>{
        loading.value=false;
        const  data=response.data;
        ebooks.value=data.content.list;

        //重置分页按钮
        pagination.value.current=params.page;
        pagination.value.total=data.content.total;
      });
    };


    /***
     * @方法描述: 表格点击页面触发
     */
    const handleTableChange = (pagination:any) => {
      handleQuery({
        page:pagination.current,
        size:pagination.pageSize
      });

    };
    /**
     * @方法描述: 初始进入页面就查一次数据
     */
    onMounted(()=>{
      handleQuery({
        page: 1,
        size: pagination.value.pageSize,
      });
    });


    return{
      ebooks,
      pagination,
      columns,
      loading,
      handleTableChange
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