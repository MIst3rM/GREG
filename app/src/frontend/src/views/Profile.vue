<template>
    <el-tabs :tab-position="tabPosition" class="tabs">
        <el-tab-pane label="User"></el-tab-pane>
        <el-tab-pane label="Configuration"></el-tab-pane>
        <el-tab-pane label="Surveys"></el-tab-pane>
        <el-tab-pane label="API"></el-tab-pane>
    </el-tabs>
</template> 

<script lang="ts">
import { Auth } from 'aws-amplify';
export default {
    name: 'Profile',
    data() {
        return {
            tabPosition: 'left',
        }
    },
    async beforeRouteEnter(to, from, next) {
        let isAuthenticated = false;
        try {
            if (await Auth.currentUserInfo()){
                isAuthenticated = true;
            }
        } catch (error) {
            isAuthenticated = false;
        }
        next(vm => {
            if (!isAuthenticated) {
                vm.$router.push('/');
            } else {
                vm.$router.push('/profile');
            }
        });
    },
}

</script>

<style scoped>
.tabs {
    height: 100%;
    position: absolute;
}
</style>