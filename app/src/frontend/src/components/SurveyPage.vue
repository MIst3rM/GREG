<template>
    <el-row justify="end">
        <el-button-group>
            <el-button type="primary">Duplicate</el-button>
            <el-button type="info">Settings</el-button>
            <el-button type="danger">Delete</el-button>
        </el-button-group>
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
    <el-row justify="center">
        <el-col :span="18" class="page">
            <component class="question-component" v-for="(question, index) in questions" :is="question"
                :num="parseInt(index) + 1" @deleteQuestion="onDeleteQuestion" @saveChanges="onSaveChanges">
            </component>
        </el-col>
    </el-row>
</template>

<script>
export default {
    name: 'SurveyPage',
    emits: ['deleteQuestion', 'saveChanges'],
    props: {
        questions: {
            type: Array,
            default: []
        },
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
.pageName {
    width: 25%;
    height: 50px;
    font-size: 20px;
    border-style: none;
}

.pageDesc {
    width: 25%;
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