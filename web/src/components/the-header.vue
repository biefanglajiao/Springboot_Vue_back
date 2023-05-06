<!--将header写成自定义组件-->
<template>
<a-layout-header class="header">
<div class="logo"/>
<a-menu
    theme="dark"
    mode="horizontal"

    :style="{ lineHeight: '64px' }"
>
  <a-menu-item key="home">
    <router-link to="/">首页</router-link>
    </a-menu-item>
  <a-menu-item key="admin_book">
    <router-link to="/admin/ebook">电子书管理</router-link>
   </a-menu-item>
  <a-menu-item key="admin_user">
    <router-link to="/admin/user">用户管理</router-link>
   </a-menu-item>
  <a-menu-item key="admin_catepory">
    <router-link to="/admin/category">分类管理</router-link>
   </a-menu-item>
  <a-menu-item key="about">
    <router-link to="/about">关于我们</router-link>
    </a-menu-item>

  <a-menu-item class="login-menu" style="float:right" @click="showLoginModal">
    <span>登录</span>
  </a-menu-item>
<!--  //todo 登录框显示两个bug-->
</a-menu>

  <a-modal
    title="登录"
    v-model:visible="loginModalVisible"
    :confirm-loading="loginModalLoading"
    @ok="login"
    >
    <a-form :model="loginUser" :label-col="{ span: 4 }" :wrapper-col="{ span: 14 }" :layout="formLayout">
      <a-form-item label="用户名">
        <a-input v-model:value="loginUser.loginName"/>
      </a-form-item>
      <a-form-item label="密码">
        <a-input v-model:value="loginUser.password" type="password"/>
      </a-form-item>
    </a-form>
  </a-modal>

</a-layout-header>
</template>
<script lang="ts">
import { defineComponent ,ref} from 'vue';
import axios from "axios";
import {message} from "ant-design-vue";
declare let hexMd5:any;
declare let KEY:any;

export default defineComponent({
  name: 'the-header',

  setup() {
    const loginUser = ref({
      loginName: '',
      password: ''
    });
    const loginModalVisible = ref(false);
    const loginModalLoading = ref(false);
    const showLoginModal = () => {
      loginModalVisible.value = true;
    };

    //登录
    const login = () => {
      console.log("login");
      loginModalLoading.value = true;
      loginUser.value.password = hexMd5(loginUser.value.password + KEY);
      axios.post('/user/login', loginUser.value).then((response) => {
        loginModalLoading.value = false;
        const data = response.data;
        if (data.success) {
          loginModalVisible.value = false;
          message.success("登录成功");
        } else {
          message.error(data.message);

        }

      });
    }




    return {
        loginUser,
        loginModalVisible,
        loginModalLoading,
        showLoginModal,
        login,
      }


  }
});



</script>
<style>
.login-menu {

  color: white;
  padding-left: 10px;
}
</style>