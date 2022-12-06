<template>
    <QuestionTitle :saving="saving" :num="num" ref="questionTitle" />
    <el-row>
        <el-input class="comment" type="textarea" resize="none" :autosize="{ minRows: 5, maxRows: 9 }" autofocus
            clearable v-model="input" :placeholder="placeholder" />
    </el-row>
    <DeleteQuestion ref="deleteQuestion" :num="num" @deleteQuestion="onDeleteQuestion" />
</template>

<script lang="ts">
import QuestionTitle from './QuestionTitle.vue'
import DeleteQuestion from './DeleteQuestion.vue'
import { useAppStore } from '../store/app';

export default {
    name: 'Comment',
    emits: ['deleteQuestion', 'saveChanges'],
    components: {
        QuestionTitle,
        DeleteQuestion
    },
    props: {
        num: {
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
            saving: false,
            input: '',
            placeholder: 'Write your comment here'
        }
    },
    methods: {
        onDeleteQuestion(idx: number) {
            this.$emit('deleteQuestion', idx);
        },
        save() {
            let question = {
                "type": "comment",
                "name": "question" + this.num,
                "title": this.$refs.questionTitle.question,
                "isRequired": this.$refs.deleteQuestion.required,
                "placeholder": this.placeholder
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
.comment {
    margin: 20px;
}
</style>