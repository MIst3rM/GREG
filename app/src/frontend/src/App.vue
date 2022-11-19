<template>
  <div>
    <el-container>
      <el-header height="100px">
        <Header />
      </el-header>
      <el-main class="main-container">
        <router-view />
      </el-main>
      <!-- <el-footer></el-footer> -->
    </el-container>
  </div>
</template> 

<script lang="ts">
import { Home } from './views'
import { Header } from './components'


export default {
  name: 'App',
  components: {
    Header,
    Home
  },
  data() {
    return {
    }
  },
}
</script>

<style scoped>
.main-container{
  height: 100%;
}
</style>
