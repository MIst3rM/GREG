<template>
    <QuestionTitle :saving="saving" :num="num" ref="questionTitle" />
    <el-row class="group-container">
        <el-radio-group class="multiple-container" v-model="radio">
            <draggable class="draggable" :list="list" v-bind="dragOptions" :component-data="{
                tag: 'div',
                type: 'transition-group',
                name: 'fade',
            }" @start="drag = true" @end="drag = false" item-key="id">
                <template #item="{ element, index }">
                    <el-col :key="index" class="answers">
                        <span v-if="!element.addremove" class="minus" :key="index + '-1'">
                            <span class="material-icons-outlined minus-inner" v-on:click="removeItem(index)"
                                :key="index + '-1.1'">
                                remove_circle
                            </span>
                        </span>
                        <span v-else class="plus" :key="index + '-2'">
                            <span class="material-icons-outlined plus-inner" v-on:click="addItem(element.name)"
                                :key="index + '-2.1'">
                                add_circle
                            </span>
                        </span>
                        <el-input class="multiple" autofocus clearable size="large" v-model="element.binding"
                            :key="index + '-4'">
                            <template #prepend>
                                <el-input class="prepend-input" type="text" v-model="element.name"
                                    :input-style="{ textAlign: 'center' }" />
                            </template>
                        </el-input>
                    </el-col>
                </template>
            </draggable>
        </el-radio-group>
    </el-row>
    <DeleteQuestion ref="deleteQuestion" :num="num" @deleteQuestion="onDeleteQuestion" />
</template>

<script lang="ts">
import draggable from 'vuedraggable'
import QuestionTitle from './QuestionTitle.vue'
import DeleteQuestion from './DeleteQuestion.vue'
import { useAppStore } from '../store/app';

const texts = ['text1', 'text2', 'text3'];

export default {
    name: 'Multiple',
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
            saving: false,
            radio: '',
            drag: false,
            initialLength: texts.length,
            list: texts.map((text, index) => {
                return {
                    id: index,
                    name: text,
                    binding: '',
                    order: index,
                    addremove: index > 1 ? 1 : 0
                }
            })
        }
    },
    methods: {
        addItem(name: string) {
            this.list.find((item) => item.name === name).addremove = 0;
            this.list.find((item) => item.addremove === 1) ? "" : (this.initialLength++, this.list.push({ name: "item" + this.initialLength, order: this.initialLength, addremove: 1, binding: "" }));
        },
        removeItem(idx: number) {
            this.list.splice(idx, 1);
        },
        onDeleteQuestion(idx: number) {
            this.$emit('deleteQuestion', idx);
        },
        save() {
            let question = {
                "type": "multipletext",
                "name": "question" + this.num,
                "title": this.$refs.questionTitle.question,
                "isRequired": this.$refs.deleteQuestion.required,
                "placeholder": this.placeholder,
                "items": this.list.filter(f => f.addremove === 0).map((item) => {
                    return {
                        "name": item.name
                    }
                })
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
}
</script>

<style scoped>
.multiple:deep(.el-input__wrapper) {
    border: 1px solid #e4e7ed;
}

.multiple:deep(.el-input-group__prepend) {
    background-color: #c5e9e2;
    padding: 0px;
}

.multiple-container {
    width: 100%;
}

.draggable {
    width: 80%;
}

.prepend-input {
    all: unset;
    width: 120px;
}

.prepend-input:deep(.el-input__wrapper) {
    border: none;
}

.prepend-input:deep(.el-input__wrapper.is-focus),
.prepend-input:deep(.el-input__wrapper:hover) {
    box-shadow: none;
}
</style>