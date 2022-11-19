<template>
    <el-row>
        <el-col class="header-component" :span="24">
            <el-input v-debounce:1000ms="setTitle" v-model="surveyName" class="surveyName" size="large" placeholder="Survey Title" />
        </el-col>
        <el-col class="header-component" :span="24">
            <el-input v-debounce:1000ms="setDescription" v-model="surveyDesc" maxlength="15" show-word-limit type="text" class="surveyDesc" size="large"
                placeholder="description" />
        </el-col>
    </el-row>
    <el-divider class="divider" />

</template>

<script>
import { useAppStore } from '../store/app';
export default {
    name: 'DesignerHeader',
    setup() {
        const appStore = useAppStore();
        return {
            appStore
        }
    },
    data() {
        return {
            surveyName: "",
            surveyDesc: "",
        };
    },
    methods: {
        setTitle() {
            this.appStore.setTitle(this.surveyName);
        },
        setDescription() {
            this.appStore.setDescription(this.surveyDesc);
        }
    }
};
</script>

<style>
.header-component {
    margin-bottom: 10px;
}
</style>