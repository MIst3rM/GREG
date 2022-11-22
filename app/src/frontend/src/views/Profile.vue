<template>
    <el-tabs :tab-position="tabPosition" class="tabs">
        <el-tab-pane label="User"></el-tab-pane>
        <el-tab-pane label="Configuration"></el-tab-pane>
        <el-tab-pane label="Surveys"></el-tab-pane>
        <el-tab-pane label="API">
            <el-container class="api-outer-container">
                <el-main class="api-inner-container">
                    <el-empty v-if="public_apis.length === 0" description="Share Your Survey Results Publicly">
                        <el-button @click="createApi" type="primary">Click here to start
                        </el-button>
                    </el-empty>
                    <ApiForm v-if="public_apis.length !== 0"/>
                </el-main>
            </el-container>

        </el-tab-pane>
    </el-tabs>
</template> 

<script lang="ts">
import { Auth } from 'aws-amplify';
import { ApiForm } from '../components';

export default {
    name: 'Profile',
    components: {
        ApiForm
    },
    data() {
        return {
            tabPosition: 'left',
            public_apis: []
        }
    },
    async beforeRouteEnter(to, from, next) {
        let isAuthenticated = false;
        try {
            if (await Auth.currentUserInfo()) {
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
    methods: {
        createApi() {
            this.public_apis.push({
                name: 'test',
                description: 'test',
                url: 'test'
            })
        }
    }
}

</script>

<style scoped>
.tabs {
    height: calc(100vh - 100px - 20px);
    position: absolute;
}

.el-tab-pane {
    width: calc(100vw - 178px);
}
</style>