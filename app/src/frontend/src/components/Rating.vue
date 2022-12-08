<template>
    <QuestionTitle :saving="saving" :num="num" ref="questionTitle" />
    <el-row class="rating-container">
        <el-col :span="4">
            <el-row>
                <span class="minus">
                    <span class="material-icons-outlined minus-inner" v-on:click="removeItem">
                        remove_circle
                    </span>
                </span>
                <span class="plus">
                    <span class="material-icons-outlined plus-inner" v-on:click="addItem">
                        add_circle
                    </span>
                </span>
            </el-row>
        </el-col>
        <el-col class="rating-circles" :span="20">
            <span class="circle" v-for="rating in ratings">
                {{ rating }}
            </span>
        </el-col>
    </el-row>
    <DeleteQuestion ref="deleteQuestion" :num="num" @deleteQuestion="onDeleteQuestion" />
</template>

<script lang="ts">
import QuestionTitle from './QuestionTitle.vue'
import DeleteQuestion from './DeleteQuestion.vue'
import draggable from 'vuedraggable'
import { useAppStore } from '../store/app';

export default {
    name: 'Rating',
    emits: ['deleteQuestion', 'saveChanges'],
    props: {
        num: {
            type: Number,
            required: true,
            default: 0
        },
    },
    components: {
        draggable,
        QuestionTitle,
        DeleteQuestion,
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
            drag: false,
            ratings: [1, 2, 3, 4, 5],
        }
    },
    methods: {
        removeItem() {
            this.ratings.splice(this.ratings.length - 1, 1)
        },
        addItem(name: string) {
            this.ratings.push(this.ratings.length + 1)
        },
        onDeleteQuestion(idx: number) {
            this.$emit('deleteQuestion', idx)
        },
        save() {
            let question = {
                "type": "rating",
                "name": "question" + this.num,
                "isRequired": this.$refs.deleteQuestion.required,
                "title": this.$refs.questionTitle.question,
                "rateMax": this.ratings.length,
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
    },
}
</script>

<style scoped>
.rating-container {
    padding: 0 0 0 50px;
    display: inline-flex;
    width: 100%;
    align-items: center;
    margin-bottom: 30px;
}

.rating {
    font-size: 50px;
    color: #3A3A3A;
}

.circle {
    width: 50px;
    height: 50px;
    line-height: 50px;
    border-radius: 50%;
    font-size: 20px;
    color: #3A3A3A;
    text-align: center;
    background: white;
    margin: 5px;
    box-shadow: 0px 10px 20px 1px #ecebeb;
}

.rating-circles {
    display: flex;
    flex-wrap: wrap;
}
</style>