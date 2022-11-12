<template>
    <QuestionTitle :num="num" />
    <el-row class="boolean-btn-container">
        <el-col :span="2" class="button-column">
            <el-switch class="boolean" v-model="value" size="large"
                style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949" :active-text="right"
                :inactive-text="left" />
        </el-col>
    </el-row>
    <el-row class="boolean-inputs-container">
        <el-col :span="5">
            <el-input v-model="left" maxlength="10" show-word-limit placeholder="set left value" />
        </el-col>
        <el-col :span="5">
            <el-input v-model="right" maxlength="10" show-word-limit placeholder="set right value" />
        </el-col>
    </el-row>
    <DeleteQuestion :num="num" @deleteQuestion="onDeleteQuestion" />
</template>

<script lang="ts">
import QuestionTitle from './QuestionTitle.vue'
import DeleteQuestion from './DeleteQuestion.vue'
export default {
    name: 'Boolean',
    props: {
        num: {
            type: Number,
            required: true,
            default: 0
        },
    },
    components: {
        QuestionTitle,
        DeleteQuestion
    },
    data() {
        return {
            value: '',
            left: '',
            right: ''
        }
    },
    methods: {
        onDeleteQuestion(idx: number) {
            this.$emit('deleteQuestion', idx)
        }
    }
}
</script>

<style scoped>
.boolean {
    scale: 2
}

.button-column {
    max-width: unset;
}

.boolean-btn-container {
    justify-content: space-evenly;
    margin-bottom: 20px;
}

.boolean-inputs-container {
    justify-content: space-evenly;
}
</style>