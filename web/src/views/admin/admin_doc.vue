<template>

  <a-layout-content style="padding: 0 50px">

    <a-layout style="padding: 24px 0; background: #fff">

      <a-layout-content :style="{ padding: '0 24px', minHeight: '280px' }">
        <a-row :gutter="24">
          <a-col :span="8">
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


            </a-form>
            <!--            :后面 跟变量  前面跟字符串-->
            <a-table
                v-if="docslevel.length>0"
                :columns="columns"
                :data-source="docs"
                :row-key="record => record.id"
                :pagination="false"
                :loading="loading"
                :defaultExpandAllRows="true"

            >
              <!--              :defaultExpandAllRows="true"单独设置此属性没有用 因为数据是异步加载的 所以需要在数据加载完成后再设置  加v-if属性-->
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
                      @confirm="showConfirm(record)"
                  >
                    <a-button type="danger">
                      <!--                delete是关键字  @click="delet(record.id)-->
                      删除
                    </a-button>
                  </a-popconfirm>

                </a-space>
              </template>
            </a-table>
          </a-col>

          <a-col :span="16">
            <a-form-item>
              <p>
                <a-button type="primary" @click="add()">
                  新增
                </a-button>
              </p>

            </a-form-item>

            <!--  //Q::confirm-loading的含义-->
            <!--  //A:confirm-loading是一个属性，当点击确定按钮时，会调用handleModalOk---现改为handleSave方法，handleSave方法会调用axios的post方法，保存数据，然后重新加载列表-->
            <!--            <a-modal title="文档" v-model:visible="modalVisible" :confirm-loading="modalLoading" @ok="handleModalOk">-->

            <a-form :model="doc" :layout="vertical">
              <a-form-item label="名称">
                <a-input v-model:value="doc.name"/>
              </a-form-item>
              <a-form-item label="父文档">
                <a-tree-select
                    v-model:value="doc.parent"
                    show-search
                    style="width: 100%"
                    :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                    placeholder="请选择父文档"
                    allow-clear
                    tree-default-expand-all
                    :tree-data="treeSleectData"
                    :fieldNames="{label:'name',key:'id',value:'id' }"

                >
                  <!--不加冒号后面是字符串 加上是变量-->
                </a-tree-select>
              </a-form-item>
              <a-form-item label="顺序">
                <a-input v-model:value="doc.sort"/>
              </a-form-item>
              <a-form-item>
              <a-button type="primary" @click="handlePreviewContent">
                <EyeOutlined/>预览
              </a-button>
              </a-form-item>
              <a-form-item label="内容">
                <div style="border: 1px solid #ccc">
                  <Toolbar
                      style="border-bottom: 1px solid #ccc"
                      :editor="editorRef"
                      :defaultConfig="toolbarConfig"
                      :mode="mode"
                  />
                  <Editor
                      style="height: 500px; overflow-y: hidden;"
                      v-model="valueHtml"
                      :defaultConfig="editorConfig"
                      :mode="mode"
                      @onCreated="handleCreated"
                  />
                </div>
              </a-form-item>
              <p>
                <a-form layout="inlline" :model="param">
                  <a-form-item>
                    <h3>是否开启可参与:        <a-switch v-model:checked="checked" />

                    </h3>

                  </a-form-item>
                </a-form>
              </p>
              <p>
                <a-form layout="inlline" :model="param">
                  <a-form-item>
                    <a-button type="primary" @click="handleSave">保存</a-button>
                  </a-form-item>
                </a-form>
              </p>

            </a-form>

            <!--            </a-modal>-->
          </a-col>
        </a-row>

        <a-drawer width="80%" placement="right" :closable="false" :visible="drawerVisible" @close="onDrawerClose">
          <div class="wangEditor-container" v-html="previewHtml"></div>
        </a-drawer>
      </a-layout-content>
    </a-layout>
  </a-layout-content>

</template>

<script lang="ts">
import {onBeforeUnmount, shallowRef, defineComponent, onMounted, ref, h, createVNode} from 'vue';
import axios from "axios";
import {message, Modal} from "ant-design-vue";
import {Tool} from '@/utils/tool';
import {useRoute} from "vue-router";
import {ExclamationCircleOutlined} from '@ant-design/icons-vue';
import '@wangeditor/editor/dist/css/style.css' // 引入 css
import {Editor, Toolbar} from '@wangeditor/editor-for-vue'
import {EyeOutlined} from '@ant-design/icons-vue';
export default defineComponent({
  name: 'AdminDoc',
  components: {
    EyeOutlined,
    Editor,
    Toolbar
  },
  setup() {
    /**
     * 可参与模块数据
     */
    const checked = ref<boolean>(false);
    /****
     * @富文本编辑相关 start
     */
        // 编辑器实例，必须用 shallowRef
    const editorRef = shallowRef()

    // 内容 HTML
    const valueHtml = ref('<p>hello</p>')

    // 模拟 ajax 异步获取内容
    onMounted(() => {
      setTimeout(() => {
        valueHtml.value = '<p>模拟 Ajax 异步设置内容</p>'
      }, 1500)
    })

    const toolbarConfig = {}
    const editorConfig = {placeholder: '请输入内容...'}

    // 组件销毁时，也及时销毁编辑器
    onBeforeUnmount(() => {
      const editor = editorRef.value
      if (editor == null) return
      editor.destroy()
    })

    const handleCreated = (editor: any) => {
      editorRef.value = editor // 记录 editor 实例，重要！
    }

    /***
     * @富文本编辑器相关 end
     */


    //------------------------富文本预览---start------------------------
    const  previewHtml = ref();
    const drawerVisible = ref(false);
    const handlePreviewContent = () => {
       // message.info("预览");
    const html = editorRef.value.getHtml();
    console.log("html", html);
    console.log("valueHtml", valueHtml.value);
    previewHtml.value = html;
    drawerVisible.value = true;
    }
    const onDrawerClose = () => {
      drawerVisible.value = false;
    }
    //------------------------富文本预览---end---------------------
    const route = useRoute();//路由  带有很多路由的信息
    console.log("路由", route);
    console.log("route.path", route.path);//当前路由的路径(不含参数)
    console.log("route.params", route.params);//参数--对应 '/admin/doc/'+record.id  需要在路由中配置
    console.log("route.query", route.query);//参数--对应 '/admin/doc/ebookId='+record.id
    console.log("route.name", route.name);
    console.log("route.fullPath", route.fullPath);
    console.log("route.hash", route.hash);
    console.log("route.meta", route.meta);//路由元信息（在路由配置里可以添加的自定义信息）


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
        // doclevel将数据进行包装 方便在分类选择中造出0级分类
    const treeSleectData = ref();
    treeSleectData.value = [];
    const modalLoading = ref<boolean>(false);
    const modalVisible = ref<boolean>(false);
    const doc = ref();
    doc.value = {};
    const handleSave = () => {//保存
      modalLoading.value = true;
      const editor = editorRef.value;
      doc.value.content = editor.getHtml();
      doc.value.ebookId = route.query.ebookId;
      doc.value.involved= checked.value;
      console.log("doc.value", doc.value);
      axios.post("/doc/save", doc.value).then((response) => {
        modalLoading.value = false;//有返回就关闭加载
        const data = response.data;//data==common,resp
        if (data.success) {
          message.success("保存成功");
          doc.value = {};

          //重新加载列表
          handleQuery();
        } else {
          message.error(data.message);
        }

      });
    };

    /**
     * 将某节点及其子孙节点全部置为disabled  递归
     */
    const setDisable = (treeSelectData: any, id: any) => {
      // console.log(treeSelectData, id);
      // 遍历数组，即遍历某一层节点
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          // 如果当前节点就是目标节点
          console.log("disabled", node);
          // 将目标节点设置为disabled
          node.disabled = true;

          // 遍历所有子节点，将所有子节点全部都加上disabled
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              setDisable(children, children[j].id)
            }
          }
        } else {
          // 如果当前节点不是目标节点，则到其子节点再找找看。
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            setDisable(children, id);
          }
        }
      }
    };

    /***
     *@方法描述: 单击编辑按钮方法
     */
    const edit = (record: any) => {
      const editor = editorRef.value;
      editor.setHtml("");//单击编辑时清空富文本框
      modalVisible.value = true;
      // doc.value = record;
      doc.value = Tool.copy(record);
      // Q:这句代码的含义
      // A:这句代码的含义是将record的值赋值给doc.value，但是这样做会导致修改doc.value的值的时候，record的值也会跟着改变，所以这里需要使用深拷贝的方式，将record的值赋值给doc.value
      handleQueryContent();//doc有值以后 去查询相应的富文本框内容
      treeSleectData.value = Tool.copy(docslevel.value);
      setDisable(treeSleectData.value, record.id)

      treeSleectData.value.unshift({id: 0, name: "无"});//将无添加到treeSleectData中;;unshift是在数组的开头添加元素
    }
    /***
     *@方法描述: 单击新增按钮方法
     */
    const add = () => {
      const editor = editorRef.value;
      editor.setHtml("");//单击编辑时清空富文本框
      modalVisible.value = true;
      doc.value = {
        ebookId: route.query.ebookId
      };
      doc.value.cover = "url地址";
console.log("docslevel.value",docslevel.value);
console.log(docslevel.value.length);
if (docslevel.value.length > 0) {
  treeSleectData.value = Tool.copy(docslevel.value);
  treeSleectData.value.unshift({id: 0, name: "无"});
}else {
  treeSleectData.value=[];
  treeSleectData.value.unshift({id: 0, name: "无"});
}
    }

    /**
     * 将某节点及其子孙节点全部置于数组中传到后端去删除  递归
     */
    const ids: Array<string> = [];
    const setDeleIds = (treeSelectData: any, id: any) => {
      // console.log(treeSelectData, id);
      // 遍历数组，即遍历某一层节点
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          // 如果当前节点就是目标节点
          console.log("disabled", node);
          // 将目标节点id加入到数组中
          ids.push(node.id);

          // 遍历所有子节点，将所有子节点全部都加上disabled
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              setDeleIds(children, children[j].id)
            }
          }
        } else {
          // 如果当前节点不是目标节点，则到其子节点再找找看。
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            setDeleIds(children, id);
          }
        }
      }
      console.log("dadadaasdadadad", ids)
    };

    /***
     * @方法描述: 删除按钮方法
     */
    const delet = (id: number) => {
      setDeleIds(docslevel.value, id);
      axios.delete("/doc/delete/" + ids.join(",")).then((response) => {
        ids.splice(0, ids.length);//清空数组
        const data = response.data;
        console.log(data);

        //重新加载列表
        handleQuery();
      });
    }
    /***
     * 二次提示框
     */
    const showConfirm = (record: any) => {
      Modal.confirm({
        title: '你希望删除掉他们么?',
        icon: createVNode(ExclamationCircleOutlined),
        content: createVNode('div', {}, [
          h('p', '文档【' + record.name + '】，以及其子节点都将被删除且无法恢复，是否删除？'),
        ]),
        okText: '删除',
        okType: 'danger',
        cancelText: '取消',
        onOk() {
          delet(record.id);
        },
        onCancel() {
          console.log('Cancel');
        },
        class: 'test',
      });
    };

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
    docslevel.value = [];//如果不初始化  v-if判断会报错
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
      axios.get("/doc/allbyid/" + route.query.ebookId,).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          docs.value = data.content;
          console.log("初始数据长度", data.content.length);
          console.log("初始数据", docs.value);
          docslevel.value = [];
          docs.value = Tool.array2Tree(docs.value, 0);
          docslevel.value = docs.value;
          console.log("处理后的数据", docslevel.value);


          if (data.content.length > 0) {
            //父文档下拉框初始化 ，相当于默认点击新增
            treeSleectData.value = Tool.copy(docslevel.value);
            //为选择树新增一个无
            treeSleectData.value.unshift({id: 0, name: "无"});
          } else {
            treeSleectData.value = [];
            treeSleectData.value.unshift({id: 0, name: "无"});
          }

          //Q：如果treeSleectData为空为什么这里就无法添加了
          //A：因为treeSleectData是一个响应式对象，当它的值发生改变时，会触发视图的更新，但是在初始化时，它的值是空的，所以无法触发视图的更新，所以无法添加
          //Q：那么如何解决呢？
          //A：在初始化时，给它一个初始值，比如空数组，这样就可以触发视图的更新了
          //Q：具体语句为？
          //A：treeSleectData.value=[]
          console.log("treeSleectData", treeSleectData.value);
        } else {
          message.error(data.message);
        }
      });
    };
    /***
     * @方法描述: 内容数据获取方法
     */
    const handleQueryContent = () => {
      axios.get("/doc/find-content/" + doc.value.id,).then((response) => {

        const data = response.data;
        if (data.success) {
          const editor = editorRef.value;
          editor.setHtml(data.content);


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
      handleSave,
      edit,
      add,
      delet,
      treeSleectData,//树结构分类
      showConfirm,//二次提示框

      //富文本编辑器相关
      editorRef,
      valueHtml,
      mode: 'default', // 或 'simple'
      toolbarConfig,
      editorConfig,
      handleCreated,

      //富文本预览相关
      drawerVisible,
      previewHtml,
      handlePreviewContent,
      onDrawerClose,
    //可参与模块数据
      checked
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