/**
 * 应用入口文件
 * 功能：初始化Vue应用，注册全局插件和组件
 */
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

import App from './App.vue'
import router from './router'
import './assets/styles/global.scss'

const app = createApp(App)

// 注册Element Plus（中文）
app.use(ElementPlus, { locale: zhCn })

// 注册所有Element Plus图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 注册Pinia状态管理
app.use(createPinia())

// 注册路由
app.use(router)

// 挂载应用
app.mount('#app')
