<template>
    <el-tabs v-model="activeName" style="height: max-content" :tab-position="tabPosition" class="tabs" @tab-click="handleClick">
        <el-tab-pane name="user">
            <template #label>
                <span class="custom-tabs-label">
                    <lord-icon src="https://cdn.lordicon.com/bhfjfgqz.json" trigger="hover" colors="primary:#121331"
                        state="hover" style="width:32px;height:32px">
                    </lord-icon>
                </span>
            </template>
            <User />
        </el-tab-pane>
        <el-tab-pane class="surveys" name="surveys">
            <template #label>
                <span class="custom-tabs-label">
                    <lord-icon src="https://cdn.lordicon.com/kulwmpzs.json" trigger="hover" colors="primary:#121331"
                        state="hover" style="width:32px;height:32px">
                    </lord-icon>
                </span>
            </template>
            <SurveyGallery @shared="onShared" :surveys="surveys" />
        </el-tab-pane>
        <!-- <el-tab-pane name="api">
            <template #label>
                <span class="custom-tabs-label">
                    <lord-icon src="https://cdn.lordicon.com/ktybrenb.json" trigger="hover"
                        style="width:32px;height:32px">
                    </lord-icon>
                </span>
            </template>
            <el-container class="api-outer-container">
                <el-main class="api-inner-container">
                    <el-empty v-if="public_apis.length === 0" description="Share Your Survey Results Publicly">
                        <el-button @click="createApi" type="primary">Click here to start
                        </el-button>
                    </el-empty>
                    <ApiForm v-if="public_apis.length !== 0" />
                </el-main>
            </el-container>
        </el-tab-pane> -->
    </el-tabs>
</template> 

<script lang="ts">
import { Auth } from 'aws-amplify';
import axios from "axios";
import { ApiForm, SurveyGallery, User } from '../components';
import type { TabsPaneContext } from 'element-plus'

export default {
    name: 'Profile',
    components: {
        ApiForm,
        SurveyGallery,
        User,
    },
    data() {
        return {
            activeName: 'user',
            tabPosition: 'left',
            public_apis: [],
            surveys: [],
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
        },
        async handleClick(tab: TabsPaneContext, event: Event) {
            await Auth.currentSession().then((session) => {
                axios.defaults.headers.common['Authorization'] = `Bearer ${session.getAccessToken().getJwtToken()}`;
                axios.defaults.headers.common['Content-Type'] = 'application/json';
            });
            if (tab.paneName === 'surveys') {
                await axios.get("/getAllForms")
                    .then((response) => {
                        this.surveys = response.data;
                        console.log(response)
                    }, (error) => {
                        console.log(error);
                    });
            }
        },
        async onShared() {
            await Auth.currentSession().then((session) => {
                axios.defaults.headers.common['Authorization'] = `Bearer ${session.getAccessToken().getJwtToken()}`;
                axios.defaults.headers.common['Content-Type'] = 'application/json';
            });
            await axios.get("/getAllForms")
                .then((response) => {
                    this.surveys = response.data;
                    console.log(response)
                }, (error) => {
                    console.log(error);
                });
        }
    }
}

</script>

<style scoped>
.el-tabs:deep(.el-tabs__item) {
    display: flex;
    align-items: center;
}

.el-tabs:deep(.el-tabs__nav-scroll) {
    height: 180vh;
}

.custom-tabs-label {
    height: 30px;
}


/* .tabs {
    height: calc(100vh - 100px - 20px);
    position: absolute;
} */

/* .el-tab-pane {
    width: calc(100vw - 178px);
} */
</style>