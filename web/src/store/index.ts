import { createStore } from 'vuex'

const store= createStore({
  state: {
    user:{}
  },
  mutations: {//同步
    setUser(state,user){
      state.user = user;
    }
  },
  actions: {//异步
  },
  modules: {
  }
})


export default store;
