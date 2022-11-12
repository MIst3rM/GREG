<template>
    <el-row class="question-title-container">
        <el-col :span="20">
            <el-form>
                <el-form-item :label="num + '.'" class="question">
                    <el-input v-model="question" size="large" placeholder="Question" />
                </el-form-item>
            </el-form>
        </el-col>
    </el-row>
</template>

<script lang="ts">
export default {
    name: 'QuestionTitle',
    props: {
        num: {
            type: Number,
            required: true,
            default: 0
        },
    },
    data() {
        return {
            question: ''
        }
    }
}
</script>

<style scoped>
.question-title-container {
    padding: 20px 20px 0px 20px;
}
</style>