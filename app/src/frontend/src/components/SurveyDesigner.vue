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
                    <DesignerHeader />
                    <SurveyPage @deleteQuestion="onDeleteQuestion" :questions="{ ...questions }"
                        :numOfQuestions="questions.length" />
                </el-col>
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

import SurveyPage from './SurveyPage.vue';
import DesignerHeader from "./DesignerHeader.vue";
import Radiogroup from "./Radiogroup.vue";
import Checkbox from "./Checkbox.vue";
import Carousel from "./Carousel.vue";
import toolbox1 from "../assets/toolbox1.json";
import toolbox2 from "../assets/toolbox2.json";

import { ref } from 'vue';

export default {
    name: 'SurveyDesigner',
    components: {
        SurveyPage,
        DesignerHeader,
        Radiogroup,
        Checkbox,
        Carousel
    },
    data() {
        return {
            isCollapse: false,
            surveyJson: {},
            questions: [],
            activeTab: ref('first'),
            primaryToolbox: toolbox1,
            secondaryToolbox: toolbox2
        }
    },
    methods: {
        onDeleteQuestion(num: number) {
            this.questions.splice(num, 1);
        },
        add_item(el) {
            switch (el.index) {
                case 1:
                    this.questions.push(Radiogroup);
                    // this.surveyJson = {
                    //     "logoPosition": "right",
                    //     "pages": [
                    //         {
                    //             "name": "page1",
                    //             "elements": [
                    //                 {
                    //                     "type": "radiogroup",
                    //                     "name": "question1",
                    //                     "choices": [
                    //                         "item1",
                    //                         "item2",
                    //                         "item3"
                    //                     ]
                    //                 }
                    //             ]
                    //         }
                    //     ]
                    // };
                    // const survey = new Survey();
                    // const page = survey.addNewPage("page1");
                    // const question = page.addNewQuestion("radiogroup", "question1");
                    // question.title = "Please select the best item:";
                    // question.choices = [{
                    //     "value": "item1",
                    //     "text": "item1"
                    // }, {
                    //     "value": "item2",
                    //     "text": "item2"
                    // }, {
                    //     "value": "item3",
                    //     "text": "item3"
                    // }];
                    // survey.render("survey");
                    break
                case 2:
                    this.questions.push(Checkbox);
                    break
                case 3:
                    console.log('dropdown')
                    break
                case 4:
                    this.questions.push(Carousel);
                    break
                case 5:
                    console.log('boolean')
                    break
                case 6:
                    console.log('file')
                    break
                case 7:
                    console.log('image')
                    break
                case 8:
                    console.log('ranking')
                    break
                case 9:
                    console.log('single')
                    break
                case 10:
                    console.log('comment')
                    break
                case 11:
                    console.log('multiple')
                    break
            }
        }
    },
}
</script>

<style>
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