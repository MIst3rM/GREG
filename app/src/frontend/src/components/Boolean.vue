<template>
    <QuestionTitle ref="questionTitle" :saving="saving" :num="num" />
    <el-row class="boolean-btn-container">
        <el-col :span="2" class="button-column">
            <el-switch class="boolean" v-model="value" size="large"
                style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949" :active-text="right"
                :inactive-text="left" />
        </el-col>
    </el-row>
    <el-row class="boolean-inputs-container">
        <el-col :span="8">
            <el-input v-model="left" maxlength="10" show-word-limit placeholder="No" />
        </el-col>
        <el-col :span="8">
            <el-input v-model="right" maxlength="10" show-word-limit placeholder="Yes" />
        </el-col>
    </el-row>
    <DeleteQuestion ref="deleteQuestion" :num="num" @deleteQuestion="onDeleteQuestion" />
</template>

<script lang="ts">
import QuestionTitle from './QuestionTitle.vue'
import DeleteQuestion from './DeleteQuestion.vue'
import { useAppStore } from '../store/app';
export default {
    name: 'Boolean',
    emits: ['deleteQuestion', 'saveChanges'],
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
    setup() {
        const appStore = useAppStore();
        return {
            appStore
        }
    },
    data() {
        return {
            value: '',
            left: '',
            right: '',
            saving: false
        }
    },
    methods: {
        onDeleteQuestion(idx: number) {
            this.$emit('deleteQuestion', idx)
        },
        save() {
            let question = {
                "type": "boolean",
                "name": "question " + this.num,
                "isRequired": this.$refs.deleteQuestion.required,
                "title": this.$refs.questionTitle.question,
                "labelTrue": this.right,
                "labelFalse": this.left,
            }
            if (this.appStore.findElement(this.num - 1)) {
                this.appStore.updateElement(this.num - 1, question);
            } else {
                this.appStore.addQuestion("Page1", question);
            }
            this.saving = true;
            setTimeout(() => this.saving = false, 4000);
            this.$emit('saveChanges');
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