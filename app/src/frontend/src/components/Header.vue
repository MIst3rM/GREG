<template>
    <el-menu class="home-menu" mode="horizontal" :ellipsis="false" :router="true">
        <el-menu-item index="0" route="/">
            <img :src="logo" alt="logo"/>
        </el-menu-item>
        <div class="flex-grow" />
        <el-menu-item v-if="logged_in" index="1" route="/profile">Hi, {{ username }}</el-menu-item>
        <el-menu-item v-if="logged_in" index="2" route="/survey-designer">Create New Survey</el-menu-item>
        <el-sub-menu v-if="logged_in" index="3">
            <template #title>Account</template>
            <el-menu-item index="2-1" route="/profile">Profile</el-menu-item>
            <el-menu-item index="2-2">Account Security</el-menu-item>
            <el-menu-item index="2-3">Logout</el-menu-item>
        </el-sub-menu>
        <el-menu-item v-else index="4">Login</el-menu-item>
    </el-menu>
</template>

<script>
import logo from "../assets/logo.png";

export default {
    name: 'Header',
    components: {
    },
    data() {
        return {
            logged_in: true,
            username: "MI5t3rM",
            btn_title: "Login",
            logo: logo,
        }
    },
    methods: {
    }
}
</script>

<style scoped>
.flex-grow {
    flex-grow: 1;
}

.home-menu{
    align-items: center;
}
</style>