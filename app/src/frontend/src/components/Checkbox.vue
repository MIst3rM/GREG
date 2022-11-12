<template>
    <QuestionTitle :num="num" />
    <el-row class="group-container">
        <el-checkbox-group v-model="check">
            <draggable v-model="list" tag="transition-group" :component-data="{
                tag: 'ul',
                type: 'transition-group',
            }" @start="drag = true" @end="drag = false" item-key="id">
                <template #item="{ element }">
                    <el-col class="answers">
                        <span class="minus">
                            <span class="material-icons-outlined minus-inner">
                                remove_circle
                            </span>
                        </span>
                        <el-checkbox :label="element.order">
                            <el-input v-model="element.binding" class="choice" size="large"
                                :placeholder="element.name" />
                        </el-checkbox>
                    </el-col>
                </template>
            </draggable>
        </el-checkbox-group>
    </el-row>
    <DeleteQuestion :num="num" @deleteQuestion="onDeleteQuestion" />
</template>

<script lang="ts" setup>
import draggable from 'vuedraggable'
import QuestionTitle from './QuestionTitle.vue'
import DeleteQuestion from './DeleteQuestion.vue'
import { ref } from 'vue'
const answers = ["item1", "item2", "item3"];
defineProps({
    num: {
        type: Number,
        default: 0
    },
});

let drag = ref(false);

const list = ref(answers.map((name, index) => {
    return { name, order: index + 1, binding: "" };
}))

const emit = defineEmits(['deleteQuestion']);
const onDeleteQuestion = (num: number) => {
    emit('deleteQuestion', num);
}

const check = ref([]);
</script>