<template>
    <el-row class="group-container">
        <el-checkbox-group v-model="check">
            <el-row>
                <el-col>
                    <el-form>
                        <el-form-item :label="num + '.'" class="question">
                            <el-input v-model="question" size="large" placeholder="Question" />
                        </el-form-item>
                    </el-form>
                </el-col>
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
                                <el-input class="choice" size="large" :placeholder="element.name" />
                            </el-checkbox>
                        </el-col>
                    </template>
                </draggable>
            </el-row>
        </el-checkbox-group>
    </el-row>
</template>

<script lang="ts" setup>
import draggable from 'vuedraggable'
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
    return { name, order: index + 1 };
}))

const check = ref([]);

const question = ref('');
</script>

<style>
</style>