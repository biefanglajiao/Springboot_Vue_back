<template>
  <a-layout>
    <a-layout-content :style="{background:'#fff', padding:'24px',margin:0,}">
      <h1 v-if="docslevel.length===0">此电子书下还没有文档</h1>

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
          <div>
            <h2>{{ doc.name }}</h2>
            <div>
              <soan>阅读数：{{ doc.viewCount }}</soan>&nbsp;&nbsp;
              <span>点赞数：{{ doc.voteCount }}</span>
            </div>
            <a-divider style="height: 2px;background-color: #42b983"/>
          </div>

          <a-back-top/>
          <!--          回到顶部显示-->

          <div v-html="html"
               class="wangEditor-container"></div>

          <div class="up">
            <a-divider style="height: 2px;background-color: #42b983"/>
            <a-button type="primary" shape="round" :size="size" @click="IncreaseVoteView(doc.id)">
              <template #icon>
                <like-two-tone two-tone-color="#eb2f96"/>
                <DownloadOutlined/>
              </template>
              点赞:{{ doc.voteCount }}
            </a-button>
            <p/>


          </div>
          <a-form-item v-show="ifshow">
            <h1>
              <a-button type="primary" @click="add()">
                参与{{ modalVisible }}
              </a-button>
            </h1>

          </a-form-item>
        </a-col>
      </a-row>

    </a-layout-content>

  </a-layout>

  <a-modal title="参与表单" v-model:visible="modalVisible" :confirm-loading="modalLoading" @ok="handleModalOk" >
              <a-form :model="needhelp" :label-col="{ span: 4 }" :wrapper-col="{ span: 14 }">
                <a-form-item label="姓名" :rules="[{ required: true, message: 'Please input your username!' }]" >
                  <a-input v-model:value="needhelp.name"/>
                </a-form-item>
                <a-form-item label="地址" >
                  <a-input v-model:value="needhelp.location"/>
                </a-form-item>
                <a-form-item label="邮箱" :rules="[{ required: true, message: 'Please input your username!' }]" >
                  <a-input  v-model:value="needhelp.email"/>
                </a-form-item>
                <a-form-item label="验证码" >
                  <a-input v-model:value="needhelp.code"/>
                  <a-button type="primary" @click="getcheck(needhelp.email)">
                  获取验证码
                </a-button>
                </a-form-item>

                <a-form-item label="描述" >
                  <a-textarea v-model:value="needhelp.context" :rows="4" placeholder="请简要描述">

                  </a-textarea>
                </a-form-item>

              </a-form>
            </a-modal>
  <!--  关于反馈部分表单-->

</template>

<script lang="ts">
import {defineComponent, onMounted, ref,} from "vue";
import axios from "axios";
import {message} from "ant-design-vue";
import {Tool} from "@/utils/tool";
import {useRoute} from "vue-router";
import type {SizeType} from 'ant-design-vue/es/config-provider';
import {LikeTwoTone} from '@ant-design/icons-vue';

export default defineComponent({
  components: {
    LikeTwoTone,
  },
  name: "doc.vue",

  setup() {
    //关于反馈相关
    const modalLoading = ref<boolean>(false);
    const modalVisible = ref<boolean>(false);
    const needhelp = ref({
      id: ref<number>(0),
      name: ref<string>(""),
      location: ref<string>(""),
      email: ref<string>(""),
      code: ref<string>(""),
      context: ref<string>(""),
    });

    //保存帮助信息
    const handleModalOk = () => {//保存
      modalLoading.value = true;


      axios.post("/doc/reply", needhelp.value).then((response) => {
        modalLoading.value = false;//有返回就关闭加载
        const data = response.data;//data==common,resp
        if (data.success) {
          modalVisible.value = false;//关闭视图
          //重新加载列表
          handleQuery();
          message.success(data.message);
          //清空表单
          needhelp.value = {
            id: 0,
            name: "",
            location: "",
            email: "",
            code: "",
            context: "",
          };
        } else {
          message.error(data.message);
        }
      });

    };
    /***
     *     单击添加按钮
     */
    const add = () => {
      modalVisible.value = true;
    }
    //获取验证码
    const getcheck = (email: string) => {
      console.log("获取验证码", email);
      axios.get("/doc/getcheck/" + email).then((response) => {
        const data = response.data;
        if (data.success) {
          message.success("验证码已发送");
        } else {
          message.error(data.message);
        }
      });
    };

    //
    const doc = ref();
    doc.value = {};//当前选中的文档信息（题目 阅读数  点赞数等）

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

    const html = ref();
    /***
     * @方法描述: 内容数据获取方法
     */
        //是否展示
    const ifshow = ref<boolean>(false);
    const handleQueryContent = (id: number) => {
     // 给needhelp的id字段赋值
      needhelp.value.id = id;
      axios.get("/doc/find-content/" + id,).then((response) => {

        const data = response.data;
        if (data.success) {
          //根据查询结果给是否展示  显示赋值
          if (data.message == "有效") {

            ifshow.value = true;
          } else {
            ifshow.value = false;

          }
          html.value = data.content;
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
          if (Tool.isNotEmpty(docslevel)) {
            defaultSelectedKeys.value = [docslevel.value[0].id];
            handleQueryContent(docslevel.value[0].id);
            //初始显示文档信息
            doc.value = docslevel.value[0];
            // console.log("初始显示文档信息!!!!!!!!!!!!adadadada!!!!!!!!!",doc.value);
            // console.log("初始显示文档信息!!!!!!!!!!!!adadadada11111!!!!!!!!!",docslevel.value[0]);
          }
        } else {
          message.error(data.message);
        }
      });
    };
    /***
     * 点赞功能
     * @param selectedKeys
     * @param info
     */
    const IncreaseVoteView = (id: number) => {
      axios.get("/doc/increaseVoteView/" + id).then((response) => {

        const data = response.data;
        if (data.success) {
          // message.success("点赞成功");
          doc.value.voteCount++;
        } else {
          message.error("点赞失败,您已赞过，请在24小时以后重试", data.message);
        }
      });
    }
    const onSelect = (selectedKeys: any, info: any) => {
      // console.log('selectedKeys', selectedKeys);
      // console.log('selectedinfo',  info);

      if (Tool.isNotEmpty(selectedKeys)) {
        //加载内容
        handleQueryContent(selectedKeys[0]);
        //选中某一节点时，加载该节点的文档信息
        doc.value = info.selectedNodes[0];
        // console.log("选中的文档信息!!!!!!!!!!!!!!!!!!!!!",doc.value);
      }

    };


    onMounted(() => {
      handleQuery();
    });
    return {
      docslevel,
      html,
      onSelect,
      defaultSelectedKeys,
      doc,

      //点赞相关 ：
      size: ref<SizeType>('large'),
      IncreaseVoteView,

//关于反馈相关
      modalLoading,
      modalVisible,
      needhelp,
      handleModalOk,
      getcheck,
      ifshow,
      add,


    }
  }
})
</script>

<style>
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
  color: rgb(201, 209, 217);
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

/*//点赞按钮*/
.up {

  text-align: center;
}

</style>