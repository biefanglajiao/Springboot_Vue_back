<template>
  <a-layout>
    <a-layout-content :style="{background:'#fff', padding:'24px',margin:0,}">
      <h1 v-if="docslevel.length===0" >此电子书下还没有文档</h1>

      <a-row>
        <a-col :span="6">
          <a-tree
              v-if="docslevel.length>0"
              :tree-data="docslevel"
              @select="onSelect"
              :replaceFields="{title:'name',key:'id',value:'id'}"
              :defaultExpandAll="true"
              :defaultSelectedKeys="defaultSelectedKeys"
          >
<!--            //Q: @select的含义是什么-->
<!--            //A:select是一个事件，当树节点被选中时触发，返回值为选中节点的相关信息-->
          </a-tree>

        </a-col>
        <a-col :span="18">
          <div v-html="html"
               class="wangEditor-container" ></div>
        </a-col>
      </a-row>
    </a-layout-content>

  </a-layout>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref, createVNode} from "vue";
import axios from "axios";
import {message} from "ant-design-vue";
import {Tool} from "@/utils/tool";
import {useRoute} from "vue-router";

export default defineComponent({
  name: "doc.vue",

  setup() {
    const route = useRoute();//路由  带有很多路由的信息
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
    docslevel.value = [];//如果不初始化  v-if判断会报错


    const defaultSelectedKeys = ref();//存放默认选中的节点（初始选中打开的文档）
    defaultSelectedKeys.value = [];

    const  html=ref();
    /***
     * @方法描述: 内容数据获取方法
     */
    const handleQueryContent = (id:number) => {
      axios.get("/doc/find-content/" + id,).then((response) => {

        const data = response.data;
        if (data.success) {
          html.value=data.content;
          console.log("内容数据", html.value);
        } else {
          message.error(data.message);
        }
      });
    };
    /***
     * @方法描述: 数据获取方法
     */
    const handleQuery = () => {

      axios.get("/doc/allbyid/" + route.query.ebookId,).then((response) => {

        const data = response.data;
        if (data.success) {
          docs.value = data.content;
          console.log("初始数据", docs.value);
          docslevel.value = [];
          docs.value = Tool.array2Tree(docs.value, 0);
          docslevel.value = docs.value;
          console.log("处理后的数据", docslevel.value)
          if (Tool.isNotEmpty(docslevel)){
            defaultSelectedKeys.value = [docslevel.value[0].id];
            handleQueryContent(docslevel.value[0].id);
          }
        } else {
          message.error(data.message);
        }
      });
    };

    const onSelect = (selectedKeys: any, info: any) => {
      console.log('selected', selectedKeys, info);
      handleQueryContent(selectedKeys[0]);
    };


    onMounted(() => {
      handleQuery();
    });

    return {
      docslevel,
      html,
      onSelect,
      defaultSelectedKeys,

    }
  }
})
</script>

<style >
/* wangeditor默认样式, 参照: http://www.wangeditor.com/doc/pages/02-%E5%86%85%E5%AE%B9%E5%A4%84%E7%90%86/03-%E8%8E%B7%E5%8F%96html.html */
/* table 样式 */

/* blockquote 样式 */
.wangEditor-container blockquote {
  display: block;
  border-left: 8px solid #d0e5f2;
  padding: 5px 10px;
  margin: 10px 0;
  line-height: 1.4;
  font-size: 100%;
  background-color: #f1f1f1;
}
/* wangeditor默认样式, 参照: http://www.wangeditor.com/doc/pages/02-%E5%86%85%E5%AE%B9%E5%A4%84%E7%90%86/03-%E8%8E%B7%E5%8F%96html.html */
/* table 样式 */
.wangEditor-container table {
  border-top: 1px solid #ccc;
  border-left: 1px solid #ccc;
}
.wangEditor-container pre {

  background: #2d2d2d;
  color: rgb(201,209,217);
  font-family: Consolas;
  text-align: left;
  padding: 1em;
  padding-left: 0.8em;
  margin: 1em;
  border-radius: 5px;
  counter-reset: line;
  white-space: pre;
  word-spacing: normal;
  word-break: normal;
  word-wrap: normal;
  line-height: 1.5;
}
.wangEditor-container table td,
.wangEditor-container table th {
  border-bottom: 1px solid #ccc;
  border-right: 1px solid #ccc;
  padding: 3px 5px;
}
.wangEditor-container table th {
  border-bottom: 2px solid #ccc;
  text-align: center;
}

/* code 样式 */
.wangEditor-container code {
  display: inline-block;
  *display: inline;
  *zoom: 1;
  background-color: #f1f1f1;
  border-radius: 3px;
  padding: 3px 5px;
  margin: 0 3px;
}
.wangEditor-container pre code {
  display: block;
}

/* ul ol 样式 */
.wangEditor-container ul, ol {
  margin: 10px 0 10px 20px;
}


</style>