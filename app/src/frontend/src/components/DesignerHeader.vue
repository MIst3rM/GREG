<template>
    <el-row>
        <el-col class="header-component" :span="24">
            <el-input v-model="surveyName" class="surveyName" size="large" placeholder="Survey Title" />
        </el-col>
        <el-col class="header-component" :span="24">
            <el-input v-model="surveyDesc" maxlength="15" show-word-limit type="text" class="surveyDesc" size="large"
                placeholder="description" />
        </el-col>
    </el-row>
    <el-divider class="divider"/>

</template>

<script>
export default {
    name: 'DesignerHeader',
    data() {
        return {
            surveyName: "",
            surveyDesc: ""
        };
    },
};
</script>

<style>
.header-component {
    margin-bottom: 10px;
}
</style>