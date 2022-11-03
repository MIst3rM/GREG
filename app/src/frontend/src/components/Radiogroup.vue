<template>
    <el-radio-group v-model="radio">
        <el-row>
            <el-col>
                <el-form>
                    <el-form-item :label="num + '.'" class="question">
                        <el-input size="large" placeholder="Question" />
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
                        <el-radio :label="element.order">
                            <el-input class="choice" size="large" :placeholder="element.name" />
                        </el-radio>
                    </el-col>
                </template>
            </draggable>
        </el-row>
    </el-radio-group>

</template>

<script>
import draggable from 'vuedraggable'
const answers = ["item1", "item2", "item3"];

export default {
    name: 'Radiogroup',
    components: {
        draggable
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
                return { name, order: index + 1 };
            }),
        };
    }

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

.minus:hover {
    background-color: var(--red-light, rgba(229, 10, 62, 0.1));
}

.minus-inner {
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
</style>