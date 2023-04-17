import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import Home from '../views/home.vue'

//非惰性 全局加载、

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/about',
    name: 'About',
    /****
     *@解释: route level code-splitting 路由级代码分解  this generates a separate chunk (about.[hash].js) for this route 这将为该路由生成一个单独的块(约.[hash].js)  which is lazy-loaded when the route is visited. 它在访问路由时是惰性加载的。
     */
        component: () => import(/* webpackChunkName: "about" */ '../views/about.vue')
  },
  {
    path: '/admin/ebook',
    name: 'AdminEbook',
    component: () => import(/* webpackChunkName: "about" */ '../views/admin/admin_ebook.vue')
  } ,
  {
    path: '/admin/category',
    name: 'AdminCategory',
    component: () => import(/* webpackChunkName: "about" */ '../views/admin/admin_category.vue')
  },
// 非惰性 全局加载方式实现
// import AdminCategory from "@/views/admin/admin_category.vue";
//   {
//     path: '/admin/category',
//     name: 'AdminCategory',
//     component: AdminCategory,
//   }
  {
    path: '/admin/doc',
    name: 'AdminDoc',
    component: () => import(/* webpackChunkName: "about" */ '../views/admin/admin_doc.vue')
  },

]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
