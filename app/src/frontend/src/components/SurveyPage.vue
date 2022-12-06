<template>
    <el-row justify="end">
        <el-icon class="page-settings" :size="25">
            <CopyDocument />
        </el-icon>
        <el-icon class="page-settings" :size="25">
            <Setting />
        </el-icon>
        <el-icon class="page-settings" :size="25">
            <Delete />
        </el-icon>
    </el-row>
    <el-row justify="center">
        <el-col class="header-component">
            <el-input v-model="pageName" class="pageName" size="large" placeholder="Page 1" />
        </el-col>
        <el-col class="header-component">
            <el-input v-model="pageDesc" maxlength="10" show-word-limit type="text" class="pageDesc" size="large"
                placeholder="description" />
        </el-col>
    </el-row>
    <el-row justify="center" :style="{ 'height': numOfQuestions === 0 ? '300px' : 'unset' }">
        <el-col :style="{ 'backgroundColor': numOfQuestions === 0 ? 'transparent' : 'white' }" :span="18" class="page">
            <component v-for="(question, index) in questions" :is="question" :ref="index"
                :num="parseInt(index) + 1" @deleteQuestion="onDeleteQuestion" @saveChanges="onSaveChanges">
            </component>
        </el-col>
    </el-row>
</template>

<script lang="ts">
import { CopyDocument } from '@element-plus/icons-vue';
import { useAppStore } from '../store/app';
export default {
    name: 'SurveyPage',
    emits: ['deleteQuestion', 'saveChanges'],
    props: {
        questions: {
            type: Array,
            default: []
        },
        numOfQuestions: {
            type: Number,
            default: 0
        },
    },
    setup() {
        const appStore = useAppStore();
        return {
            appStore
        }
    },
    data() {
        return {
            pageName: "",
            pageDesc: "",
        };
    },
    methods: {
        onDeleteQuestion(index) {
            this.$emit('deleteQuestion', index);
        },
        onSaveChanges() {
            this.$emit('saveChanges');
        }
    },
};
</script>

<style>

.page-settings{
    margin-right: 10px;
    cursor: pointer;
}
.pageName {
    width: 60%;
    height: 50px;
    font-size: 20px;
    border-style: none;
}

.pageDesc {
    width: 60%;
    height: 40px;
    border-style: none;
}

.page {
    border-width: 1px;
    border-color: black;
    border-radius: 5px;
    margin: 20px 0;
    background-color: white;
}

.page:hover {
    border-style: solid;
}

.question-component {
    margin: 20px 0;
}

.question-component:focus {
    border-radius: 5px;
    padding: 10px;
    box-shadow: 0 0 2px var(--secondary-light, rgba(255, 152, 20, 0.25))
}

.el-radio__inner {
    scale: 1.5;
}
</style>