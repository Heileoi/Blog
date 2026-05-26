/**
 * 应用入口文件
 * 功能：初始化Vue应用，注册全局组件和插件
 */
import App from './App'
import { createSSRApp } from 'vue'
import { createStore } from './store'

export function createApp() {
  const app = createSSRApp(App)
  const store = createStore()
  app.use(store)
  return { app }
}
