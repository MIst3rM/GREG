<template>
    <el-menu class="home-menu" mode="horizontal" :ellipsis="false" :router="true">
        <el-menu-item index="0" route="/">
            <img :src="logo" alt="logo" />
        </el-menu-item>
        <div class="flex-grow" />
        <el-menu-item v-if="isAuthenticated" index="1" route="/profile">Hi, {{ user.username }}</el-menu-item>
        <el-menu-item v-if="isAuthenticated" index="2" route="/survey-designer">Create New Survey</el-menu-item>
        <el-sub-menu v-if="isAuthenticated" index="3">
            <template #title>Account</template>
            <el-menu-item index="2-1" route="/profile">Profile</el-menu-item>
            <el-menu-item index="2-2">Account Security</el-menu-item>
            <el-menu-item index="2-3" @click="logout">Logout</el-menu-item>
        </el-sub-menu>
        <template v-else>
            <el-menu-item index="/" @click="register">Register</el-menu-item>
            <el-menu-item index="/" @click="login">Login</el-menu-item>
        </template>
    </el-menu>
</template>

<script lang="ts">
import logo from "../assets/logo.png";
import { ref } from 'vue'
import { Amplify, Auth } from 'aws-amplify';

export default {
    name: 'Header',
    setup() {
        Amplify.configure({
            Auth: {
                region: import.meta.env.VITE_AWS_REGION,
                userPoolId: import.meta.env.VITE_COGNITO_USERPOOL_ID,
                userPoolWebClientId: import.meta.env.VITE_COGNITO_CLIENT_ID,
                mandatorySignIn: true,
                storage: window.sessionStorage,
                oauth: {
                    domain: import.meta.env.VITE_COGNITO_APP_DOMAIN,
                    scope: ['email', 'openid', 'phone'],
                    redirectSignIn: import.meta.env.VITE_COGNITO_REDIRECT_URI,
                    redirectSignOut: import.meta.env.VITE_COGNITO_REDIRECT_URI,
                    responseType: 'code',
                },
            },
        });
        return {
            login: () => {
                Auth.federatedSignIn();
            },
            logout: () => {
                Auth.signOut();
            },
            register: () => {
                Auth.federatedSignIn();
            },
            logo,
            isAuthenticated: ref(false),
            user: ref({}),
        }
    },
    async beforeMount() {
        const current_user = await Auth.currentAuthenticatedUser();
        if (current_user) {
            this.isAuthenticated = true;
            this.user = current_user;
        } else {
            this.isAuthenticated = false;
        }
    }
}
</script>

<style scoped>
.flex-grow {
    flex-grow: 1;
}
.home-menu {
    align-items: center;
}
</style>