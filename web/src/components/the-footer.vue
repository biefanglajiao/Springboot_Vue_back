<!--将header写成自定义组件-->
<template>
<a-layout-footer style="text-align: center">
Ant Design ©2018 Created by CZH ，当前登录用户为：{{user.loginName}}
</a-layout-footer>
</template>
<script lang="ts">
import { defineComponent ,computed} from 'vue';
import store from "@/store";

export default defineComponent({
  name: 'the-footer',
setup(){
    const user=computed(()=>store.state.user);

    return{
        user
    }
}
});
</script>
