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
        <pre>
  {{ ebooks }} {{books1}}
</pre>
        Content
      </a-layout-content>
    </a-layout>
  </a-layout-content>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref, reactive,toRef} from 'vue';
import axios from "axios";

export default defineComponent({
  name: 'Home',
  setup() {
    console.log("setup")
    const ebooks = ref();
    const ebooks1 = reactive({books: []});


    onMounted(() => {//生命周期函数
      console.log("onMounted")
      axios.get("http://localhost:8080/ebook/list?name=教程").then((response) => {//初始化方法
        const data = response.data;
        ebooks.value = data.content;
        ebooks1.books = data.content;
        console.log(response)
      });
    })


    return {
      ebooks,
      books1: toRef (ebooks1, "books")
    }
  }
});
</script>
