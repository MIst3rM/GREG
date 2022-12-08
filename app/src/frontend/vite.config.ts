import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue({
    template: {
      compilerOptions: {
        isCustomElement: (tag) => ['lord-icon'].includes(tag),
      }
    }
  })],
  server: {
    host: true,
    port: 8080,
    watch: {
      usePolling: true,
      interval: 1500,
    },
  },
  resolve: {
    alias: {
      './runtimeConfig': './runtimeConfig.browser',
    },
  }
})
