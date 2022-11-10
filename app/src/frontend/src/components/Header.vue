<template>
    <div id="overlay" :style="{ 'display': show }">
    </div>
    <el-menu class="home-menu" mode="horizontal" :ellipsis="false" :router="true">
        <el-menu-item index="0" route="/">
            <img :src="logo" alt="logo" />
        </el-menu-item>
        <div class="flex-grow" />
        <el-menu-item v-if="getIsAuthenticated" index="1" route="/profile">Hi, {{ getUser }}</el-menu-item>
        <el-menu-item v-if="getIsAuthenticated" index="2" route="/survey-designer">Create New Survey</el-menu-item>
        <el-sub-menu v-if="getIsAuthenticated" index="3">
            <template #title>Account</template>
            <el-menu-item index="2-1" route="/profile">Profile</el-menu-item>
            <el-menu-item index="2-2">Account Security</el-menu-item>
            <el-menu-item index="2-3" @click="logout">Logout</el-menu-item>
        </el-sub-menu>
        <el-menu-item v-else index="/" @click="login">Login</el-menu-item>
    </el-menu>
</template>

<script lang="ts">
import logo from "../assets/logo.png";
import { useAuthStore } from '../stores/auth'
import { useAuth0 } from '@auth0/auth0-vue';
import { useRouter } from 'vue-router'
import { ref } from 'vue'

export default {
    name: 'Header',
    setup() {
        const { loginWithPopup, user, isAuthenticated, logout } = useAuth0();
        const authStore = useAuthStore();
        const router = useRouter();
        let show = ref('none');

        return {
            login: () => {
                show.value = 'block';
                loginWithPopup()
                    .then(() => {
                        show.value = 'none';
                        let rawObject = JSON.parse(JSON.stringify(user));
                        authStore.login(rawObject._value);
                        router.push('/survey-designer');
                    });
            },
            logout: () => {
                authStore.logout();
                logout({ returnTo: window.location.origin });
            },
            isAuthenticated,
            user,
            logo,
            authStore,
            show,
        };
    },
    computed: {
        getUser() {
            return this.authStore.getUser.name;
        },
        getIsAuthenticated() {
            return this.authStore.getIsAuthenticated;
        },
    },
}
</script>

<style scoped>
.flex-grow {
    flex-grow: 1;
}

.home-menu {
    align-items: center;
}

#overlay {
    position: fixed;
    /* Sit on top of the page content */
    display: none;
    /* Hidden by default */
    width: 100%;
    /* Full width (cover the whole page) */
    height: 100%;
    /* Full height (cover the whole page) */
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.5);
    /* Black background with opacity */
    z-index: 2;
    /* Specify a stack order in case you're using a different order for other elements */
    cursor: pointer;
    /* Add a pointer on hover */
}
</style>