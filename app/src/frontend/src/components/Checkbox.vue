<template>
    <QuestionTitle ref="questionTitle" :saving="saving" :num="num" />
    <el-row class="group-container">
        <el-checkbox-group v-model="check">
            <draggable v-model="list" v-bind="dragOptions" :component-data="{
                tag: 'div',
                type: 'transition-group',
                name: 'fade',
            }" @start="drag = true" @end="drag = false" item-key="id">
                <template #item="{ element, index }">
                    <el-col class="answers" :itemKey="index">
                        <span v-if="!element.addremove" class="minus" :itemid="index + '-1'">
                            <span class="material-icons-outlined minus-inner" v-on:click="removeItem(index)"
                                :itemid="index + '-1.1'">
                                remove_circle
                            </span>
                        </span>
                        <span v-else class="plus" :itemid="index + '-2'">
                            <span class="material-icons-outlined plus-inner" v-on:click="addItem(element.name)"
                                :itemid="index + '-2.1'">
                                add_circle
                            </span>
                        </span>
                        <el-checkbox :label="element.order" :itemid="index + '-4'">
                            <el-input v-model="element.binding" class="choice" size="large" :placeholder="element.name"
                                :itemid="index + '-4.1'" />
                        </el-checkbox>
                    </el-col>
                </template>
            </draggable>
        </el-checkbox-group>
    </el-row>
    <DeleteQuestion ref="deleteQuestion" :num="num" @deleteQuestion="onDeleteQuestion" />
</template>

<script lang="ts">
import draggable from 'vuedraggable'
import QuestionTitle from './QuestionTitle.vue'
import DeleteQuestion from './DeleteQuestion.vue'
import { useAppStore } from '../store/app';
const answers = ["item1", "item2", "item3", "item4", "None", "Other (describe)"];

export default {
    name: 'Checkbox',
    emits: ['deleteQuestion', 'saveChanges'],
    components: {
        draggable,
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
            check: [],
            drag: false,
            list: answers.map((name, index) => {
                return { name, order: index + 1, addremove: index > 2 ? 1 : 0, binding: "" };
            }),
            question: '',
            show: false,
            initialLength: answers.length,
            saving: false
        };
    },
    methods: {
        removeItem(idx: number) {
            this.list.splice(idx, 1);
        },
        addItem(name: string) {
            this.list.find((item) => item.name === name).addremove = 0;
            this.list.find((item) => item.addremove === 1) ? "" : (this.initialLength++, this.list.push({ name: "item" + this.initialLength, order: this.initialLength, addremove: 1, binding: "" }));
        },
        onDeleteQuestion(idx: number) {
            this.$emit('deleteQuestion', idx);
        },
        save() {
            let question = {
                "type": "checkbox",
                "name": "question " + this.num,
                "isRequired": this.$refs.deleteQuestion.required,
                "title": this.$refs.questionTitle.question,
                "choices": []
            }
            this.list.forEach((item) => {
                if (item.addremove === 0) {
                    question.choices.push(item.binding);
                }
            })
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
    computed: {
        dragOptions() {
            return {
                animation: 250,
                group: "people",
                disabled: false,
                ghostClass: "ghost"
            };
        },
    }
};
</script>