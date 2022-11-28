<template>
    <QuestionTitle :saving="saving" :num="num" ref="questionTitle" />
    <el-row class="dropdown-container">
        <el-col class="dropdown">
            <el-autocomplete fit-input-width v-model="state" :fetch-suggestions="querySearch" clearable
                class="inline-input" placeholder="Select...Or input">
                <template #default="{ item }">
                    <div class="value">{{ item.binding }}</div>
                </template>
            </el-autocomplete>
        </el-col>
    </el-row>
    <h4>Edit Suggestions</h4>
    <el-row class="group-container">
        <draggable class="answers" :list="suggestions" tag="transition-group" :component-data="{
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
                    <el-input v-model="element.binding" class="choice" size="large" placeholder="add new suggestion"
                        :key="index + '-3'" />
                </el-col>
            </template>
        </draggable>
    </el-row>
    <DeleteQuestion ref="deleteQuestion" :num="num" @deleteQuestion="onDeleteQuestion" />
</template>

<script lang="ts">
import QuestionTitle from './QuestionTitle.vue';
import DeleteQuestion from './DeleteQuestion.vue';
import draggable from 'vuedraggable'
import { useAppStore } from '../store/app';

const demo_suggestions = ["vue", "element", "cooking", "mint-ui", "vuex", "vue-router", "babel", ""]

export default {
    name: 'Dropdown',
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
            state: '',
            saving: false,
            suggestions: demo_suggestions.map((item, index) => {
                return { name: item, order: index + 1, addremove: (index + 1) === demo_suggestions.length ? 1 : 0, binding: item }
            }),
            drag: false,
            initialLength: demo_suggestions.length
        };
    },
    methods: {
        removeItem(idx: number) {
            this.suggestions.splice(idx, 1);
        },
        addItem(name: string) {
            this.suggestions.find((item) => item.name === name).addremove = 0;
            this.suggestions.find((item) => item.addremove === 1) ? "" : (this.initialLength++, this.suggestions.push({ name: "item" + this.initialLength, order: this.initialLength, addremove: 1, binding: "" }));
        },
        querySearch(queryString: string, cb: any) {
            const results = queryString
                ? this.suggestions.filter(s => s.binding.toLowerCase().indexOf(queryString.toLowerCase()) === 0)
                : this.suggestions
            cb(results)
        },
        onDeleteQuestion(idx: number) {
            this.$emit('deleteQuestion', idx);
        },
        save() {
            let question = {
                "type": "dropdown",
                "name": "question " + this.num,
                "isRequired": this.$refs.deleteQuestion.required,
                "showOtherItem": true,
                "title": this.$refs.questionTitle.question,
                "choices": []
            }
            this.suggestions.forEach((item) => {
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
    }
}
</script>

<style scoped>
.dropdown-container {
    margin: 20px;
}

.dropdown>>>.inline-input {
    width: 100%;
}

.dropdown>>>.el-input {
    font-size: larger;
}

.choice {
    width: 50%
}

h4 {
    padding: 0px 20px 0px 30px;
}

.answers {
    flex-wrap: wrap;
}
</style>