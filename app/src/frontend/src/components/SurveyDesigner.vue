<template>
    <el-tabs v-model="activeTab" class="designer-tabs">
        <el-tab-pane label="Designer" name="first">
            <el-row id="editor" justify="space-between">
                <el-col :span="1.5">
                    <el-menu id="tools_menu" :collapse="isCollapse">
                        <el-popover v-for="tool in primaryToolbox" placement="right" :title="tool.name" :width="200"
                            trigger="hover" :content="tool.content">
                            <template #reference>
                                <el-menu-item :index="tool.id" @click="add_item">
                                    <span class="material-icons-outlined">{{ tool.type }}</span>
                                </el-menu-item>
                            </template>
                        </el-popover>
                        <el-divider />
                        <el-popover v-for="tool in secondaryToolbox" placement="right" :title="tool.name" :width="200"
                            trigger="hover" :content="tool.content">
                            <template #reference>
                                <el-menu-item :index="tool.id" @click="add_item">
                                    <span class="material-icons-outlined">
                                        {{ tool.type }}
                                    </span>
                                </el-menu-item>
                            </template>
                        </el-popover>
                    </el-menu>
                </el-col>
                <el-col class="designer" :span="21">
                    <DesignerHeader v-if="appStore.getNumberOfPages !== 0" />
                    <SurveyPage v-if="appStore.getNumberOfPages !== 0" @deleteQuestion="onDeleteQuestion"
                        @saveChanges="onSaveChanges" :questions="{ ...questions }" :numOfQuestions="questions.length" />
                    <el-empty v-if="appStore.getNumberOfPages === 0"
                        description="Click The Button Below To Get Started">
                        <el-button type="primary" @click="addPage">Add A Page</el-button>
                    </el-empty>
                </el-col>
                <el-row class="submit-survey-container">
                    <el-button class="create-survey">
                        Create Survey
                    </el-button>
                </el-row>
            </el-row>
        </el-tab-pane>
        <el-tab-pane label="Preview" name="second">
            <div id="survey"></div>
        </el-tab-pane>
        <el-tab-pane label="Logic" name="third"></el-tab-pane>
        <el-tab-pane label="JSON Editor" name="fourth"></el-tab-pane>
    </el-tabs>
</template> 

<script lang="ts">
import "survey-core/modern.min.css";
import { StylesManager, SurveyTriggerSetValue } from "survey-core";
StylesManager.applyTheme("modern");

import { Survey } from 'survey-knockout-ui';

StylesManager.applyTheme("modern");

import SurveyPage from './SurveyPage.vue';
import DesignerHeader from "./DesignerHeader.vue";
import Radiogroup from "./Radiogroup.vue";
import Checkbox from "./Checkbox.vue";
import Carousel from "./Carousel.vue";
import Boolean from "./Boolean.vue";
import Ranking from "./Ranking.vue";
import ImagePicker from "./ImagePicker.vue";
import toolbox1 from "../assets/toolbox1.json";
import toolbox2 from "../assets/toolbox2.json";
import { Auth } from 'aws-amplify';
import { useAppStore } from '../store/app';

import { ref } from 'vue';

export default {
    name: 'SurveyDesigner',
    async beforeRouteEnter(to, from, next) {
        let isAuthenticated = false;
        try {
            if (await Auth.currentUserInfo()) {
                isAuthenticated = true;
            }
        } catch (error) {
            isAuthenticated = false;
        }
        next(vm => {
            if (!isAuthenticated) {
                vm.$router.push('/');
            } else {
                vm.$router.push('/survey-designer');
            }
        });
    },
    components: {
        SurveyPage,
        DesignerHeader,
        Radiogroup,
        Checkbox,
        Carousel,
        Boolean,
        Ranking,
        ImagePicker
    },
    setup() {
        const appStore = useAppStore();
        return {
            appStore
        }
    },
    data() {
        return {
            idx: 0,
            timer: null,
            isCollapse: false,
            questions: [] as Array<any>,
            pages: [] as Array<string>,
            activeTab: ref('first'),
            primaryToolbox: toolbox1,
            secondaryToolbox: toolbox2,
        }
    },
    methods: {
        addPage() {
            this.idx += 1;
            this.appStore.addPage({
                "name": "Page" + this.idx,
                "elements": []
            })
            this.pages.push("Page" + this.idx);
        },
        onDeleteQuestion(num: number) {
            this.questions.splice(num, 1);
        },
        onSaveChanges() {
            let survey = new Survey(this.appStore.getSurvey);
            survey.render("survey");
        },
        add_item(el) {
            switch (el.index) {
                case "1":
                    this.questions.push(Radiogroup);
                    break
                case "2":
                    this.questions.push(Checkbox);
                    break
                case "3":
                    console.log('dropdown')
                    break
                case "4":
                    this.questions.push(Carousel);
                    break
                case "5":
                    this.questions.push(Boolean);
                    break
                case "6":
                    console.log('file')
                    break
                case "7":
                    this.questions.push(ImagePicker);
                    break
                case "8":
                    this.questions.push(Ranking);
                    break
                case "9":
                    console.log('single')
                    break
                case "10":
                    console.log('comment')
                    break
                case "11":
                    console.log('multiple')
                    break
            }
        }
    },
}
</script>

<style>
.submit-survey-container {
    width: 100%;
    justify-content: center;
}

.create-survey {
    margin: 20px;
    height: 50px;
    width: 200px;
    font-size: larger;
}

#editor {
    background-color: #f3f3f3;
}

#tools_menu {
    height: 100%;
}

.designer {
    padding-top: 30px;
    padding-right: 6%;
}
</style>