<template>
    <QuestionTitle :num="num" />
    <el-row class="group-container">
        <el-radio-group v-model="radio">
            <draggable class="el-col el-col-24" :list="list" tag="transition-group" :component-data="{
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
                        <el-radio :label="element.order" :itemid="index + '-4'">
                            <el-input v-model="element.binding" class="choice" size="large" :placeholder="element.name"
                                :itemid="index + '-4.1'" />
                        </el-radio>
                    </el-col>
                </template>
            </draggable>
        </el-radio-group>
    </el-row>
    <DeleteQuestion :num="num" @deleteQuestion="onDeleteQuestion" />
</template>

<script lang="ts">
import draggable from 'vuedraggable'
import QuestionTitle from './QuestionTitle.vue'
import DeleteQuestion from './DeleteQuestion.vue'
const answers = ["item1", "item2", "item3", "item4", "None", "Other (describe)"];


export default {
    name: 'Radiogroup',
    emits: ['deleteQuestion'],
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
    data() {
        return {
            radio: '',
            drag: false,
            list: answers.map((name, index) => {
                return { name, order: index + 1, addremove: index > 2 ? 1 : 0, binding: "" };
            }),
            question: '',
            show: false,
            initialLength: answers.length
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
        }
    },

};
</script>

<style>
.list-move {
    transition: all 0.5s ease;
}

.minus {
    color: red;
    display: inherit;
    margin-right: 20px;
    border-radius: 20px;
    scale: 1.5;
}

.plus {
    color: green;
    display: inherit;
    margin-right: 20px;
    border-radius: 20px;
    scale: 1.5;
}

.minus:hover {
    background-color: var(--red-light, rgba(229, 10, 62, 0.1));
}

.plus:hover {
    background-color: var(--green-light, rgba(25, 179, 148, 0.1));
}

.minus-inner,
.plus-inner {
    scale: 0.7;
}

ul {
    padding: 0;
    list-style-type: none;
}

.answers {
    display: flex;
    align-items: center;
    margin: 10px 0;
}

.question {
    align-items: center;
}

.group-container {
    padding: 0 0 0 50px;
}
</style>