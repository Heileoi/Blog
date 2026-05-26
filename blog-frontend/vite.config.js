import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'
import path from 'path'

/**
 * Vite配置文件
 * 功能：配置开发服务器、插件、路径别名、代理等
 */
export default defineConfig({
  plugins: [
    vue(),
    // 自动导入Element Plus的API
    AutoImport({
      resolvers: [ElementPlusResolver()],
      imports: ['vue', 'vue-router', 'pinia']
    }),
    // 自动注册Element Plus组件
    Components({
      resolvers: [ElementPlusResolver()]
    })
  ],
  resolve: {
    // 路径别名配置
    alias: {
      '@': path.resolve(__dirname, 'src')
    }
  },
  server: {
    port: 5173,
    // API代理配置（开发环境）
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})
