<template>
  <a-layout>
    <a-layout-content :style="{background:'#fff', padding:'24px',margin:0,}">
      <a-row>
        <a-col :span="6">
          <a-tree
             v-if="docslevel.length>0"
             :tree-data="docslevel"
             @select="onSelect"
             :replaceFields="{title:'name',key:'id',value:'id'}"
             :defaultExpandAll="true"
             >

          </a-tree>

        </a-col>
        <a-col :span="18">

        </a-col>
      </a-row>
    </a-layout-content>

  </a-layout>
</template>

<script lang="ts">
import {defineComponent,onMounted,ref,createVNode} from "vue";
import axios from "axios";
import {message} from "ant-design-vue";
import {Tool} from "@/utils/tool";
import {useRoute} from "vue-router";

export default defineComponent({
  name: "doc.vue",

  setup(){
    const route = useRoute();//路由  带有很多路由的信息
    const docs=ref();
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
    docslevel.value = [];//如果不初始化  v-if判断会报错
    /***
     * @方法描述: 数据获取方法
     */
    const handleQuery = () => {

      axios.get("/doc/allbyid/"+route.query.ebookId,).then((response) => {

        const data = response.data;
        if (data.success) {
          docs.value = data.content;
          console.log("初始数据", docs.value);
          docslevel.value = [];
          docs.value = Tool.array2Tree(docs.value, 0);
          docslevel.value = docs.value;
          console.log("处理后的数据", docslevel.value)
        } else {
          message.error(data.message);
        }
      });
    };

    onMounted(() => {
      handleQuery();
    });

    return {
      docslevel,

    }
  }
})
</script>

<style scoped>

</style>