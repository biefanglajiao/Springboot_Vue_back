<template>
  <a-layout-content style="padding: 0 50px">

    <a-layout style="padding: 24px 0; background: #fff">
      <a-layout-sider width="200" style="background: #fff">
        <a-menu
            mode="inline"

            style="height: 100%"
        >
          <a-sub-menu key="sub1">
            <template #title>
                <span>
                  <user-outlined/>
                  subnav 11
                </span>
            </template>
            <a-menu-item key="1">option1</a-menu-item>
            <a-menu-item key="2">option2</a-menu-item>
            <a-menu-item key="3">option3</a-menu-item>
            <a-menu-item key="4">option4</a-menu-item>
          </a-sub-menu>
          <a-sub-menu key="sub2">
            <template #title>
                <span>
                  <laptop-outlined/>
                  subnav 2
                </span>
            </template>
            <a-menu-item key="5">option5</a-menu-item>
            <a-menu-item key="6">option6</a-menu-item>
            <a-menu-item key="7">option7</a-menu-item>
            <a-menu-item key="8">option8</a-menu-item>
          </a-sub-menu>
          <a-sub-menu key="sub3">
            <template #title>
                <span>
                  <notification-outlined/>
                  subnav 3
                </span>
            </template>
            <a-menu-item key="9">option9</a-menu-item>
            <a-menu-item key="10">option10</a-menu-item>
            <a-menu-item key="11">option11</a-menu-item>
            <a-menu-item key="12">option12</a-menu-item>
          </a-sub-menu>
        </a-menu>
      </a-layout-sider>
      <a-layout-content :style="{ padding: '0 24px', minHeight: '280px' }">
        <a-list item-layout="vertical" size="large" :pagination="pagination" :data-source="ebooks"
                :grid="{ gutter:20 , column : 3}">
          <template #footer>
            <div>
              <b>电子书籍</b>
              ---常兆海
            </div>
          </template>
          <template #renderItem="{ item }">
            <a-list-item key="item.name">
              <template #actions>
          <span v-for="{ type, text } in actions" :key="type">
            <component :is="type" style="margin-right: 8px"/>
            {{ text }}
          </span>
              </template>
              <template #extra>

              </template>
              <a-list-item-meta :description="item.description">
                <template #title>
                  <a :href="item.href">{{ item.name }}</a>
                </template>
                <template #avatar>
                  <a-avatar :src="item.cover"/>
                </template>
              </a-list-item-meta>

            </a-list-item>
          </template>
        </a-list>
        Content
      </a-layout-content>
    </a-layout>
  </a-layout-content>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref, reactive, toRef} from 'vue';
import axios from "axios";
import {StarOutlined, LikeOutlined, MessageOutlined} from '@ant-design/icons-vue';

const listData: Record<string, string>[] = [];

for (let i = 0; i < 23; i++) {
  listData.push({
    href: 'https://www.antdv.com/',
    title: `ant design vue part ${i}`,
    avatar: 'https://joeschmoe.io/api/v1/random',
    description:
        'Ant Design, a design language for background applications, is refined by Ant UED Team.',
    content:
        'We supply a series of design principles, practical patterns and high quality design resources (Sketch and Axure), to help people create their product prototypes beautifully and efficiently.',
  });
}
export default defineComponent({
  name: 'Home',

  components: {
    StarOutlined,
    LikeOutlined,
    MessageOutlined,
  },
  setup() {
    console.log("setup")
    const ebooks = ref();
    const ebooks1 = reactive({books: []});


    onMounted(() => {//生命周期函数
      console.log("onMounted")
      axios.get("/ebook/list").then((response) => {//初始化方法
        const data = response.data;
        ebooks.value = data.content;
        ebooks1.books = data.content;
        console.log(response)
      });
    })
    const pagination = {//分页
      onChange: (page: number) => {
        console.log(page);
      },
      pageSize: 6,
    };
    const actions: Record<string, string>[] = [
      {type: 'StarOutlined', text: '156'},
      {type: 'LikeOutlined', text: '156'},
      {type: 'MessageOutlined', text: '2'},
    ];

    return {
      ebooks,
      books1: toRef(ebooks1, "books"),
      listData,
      pagination,
      actions,
    }
  }
});
</script>
<style scoped>
/*scoped 作用  限制样式只在当前组件生效*/
.ant-avatar {
  width: 50px;
  height: 50px;
  line-height: 50px;
  border-radius: 8%;
  margin: 5px 0;


}</style>