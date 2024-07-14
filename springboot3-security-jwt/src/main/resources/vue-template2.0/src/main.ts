
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css' 

import App from '@/App.vue'
import router from '@/router'
// 1、pinia的持久化插件
import { createPersistedState } from 'pinia-plugin-persistedstate'
// element-plus的图标
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App)
const pinia = createPinia()

//2、 接收createPersistedState函数
const piniaPersistedState = createPersistedState()

// 3、在pinia中引入持久化插件
pinia.use(piniaPersistedState)

// 全局引入图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
  }


app.use(pinia)
app.use(router)
app.use(ElementPlus)
app.mount('#app')
