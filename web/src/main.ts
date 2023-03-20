import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Antd from  'ant-design-vue'
import 'ant-design-vue/dist/antd.css'
import axios from 'axios'

createApp(App).use(store).use(router).use(Antd).mount('#app');


console.log('环境',process.env.NODE_ENV);

//改变axios的默认配置 以后所有的axios请求都会带上这个配置baseURL  和xue的不一样
axios.defaults.baseURL = process.env.VUE_APP_SERVER;