<template>
    <el-tabs class="designer-tabs">
        <el-tab-pane label="Designer" name="first">
            <el-row id="editor" justify="space-between">
                <el-col :span="1.5">
                    <el-menu id="tools_menu" :collapse="isCollapse">
                        <el-popover placement="right" title="Radiogroup" :width="200" trigger="hover"
                            content="radiogroup toolbox item">
                            <template #reference>
                                <el-menu-item index="1" @click="add_item">
                                    <span class="material-icons-outlined">radio_button_checked</span>
                                </el-menu-item>
                            </template>
                        </el-popover>
                        <el-popover placement="right" title="Checkbox" :width="200" trigger="hover"
                            content="checkbox toolbox item">
                            <template #reference>
                                <el-menu-item index="2" @click="add_item">
                                    <span class="material-icons-outlined">check_box</span>
                                </el-menu-item>
                            </template>
                        </el-popover>
                        <el-popover placement="right" title="Dropdown" :width="200" trigger="hover"
                            content="dropdown toolbox item">
                            <template #reference>
                                <el-menu-item index="3" @click="add_item">
                                    <span class="material-icons-outlined">
                                        segment
                                    </span>
                                </el-menu-item>
                            </template>
                        </el-popover>
                        <el-popover placement="right" title="Boolean" :width="200" trigger="hover"
                            content="boolean toolbox item">
                            <template #reference>
                                <el-menu-item index="4" @click="add_item">
                                    <span class="material-icons-outlined">
                                        toggle_on
                                    </span>
                                </el-menu-item>
                            </template>
                        </el-popover>
                        <el-popover placement="right" title="File Upload" :width="200" trigger="hover"
                            content="file toolbox item">
                            <template #reference>
                                <el-menu-item index="5" @click="add_item">
                                    <span class="material-icons-outlined">
                                        upload_file
                                    </span>
                                </el-menu-item>
                            </template>
                        </el-popover>
                        <el-popover placement="right" title="Image Picker" :width="200" trigger="hover"
                            content="Image Picker toolbox item">
                            <template #reference>
                                <el-menu-item index="6" @click="add_item">
                                    <span class="material-icons-outlined">
                                        photo_library
                                    </span>
                                </el-menu-item>
                            </template>
                        </el-popover>
                        <el-popover placement="right" title="Ranking" :width="200" trigger="hover"
                            content="Ranking toolbox item">
                            <template #reference>
                                <el-menu-item index="7" @click="add_item">
                                    <span class="material-icons-outlined">
                                        sort
                                    </span>
                                </el-menu-item>
                            </template>
                        </el-popover>
                        <el-divider />
                        <el-popover placement="right" title="Single Input" :width="200" trigger="hover"
                            content="Single Input toolbox item">
                            <template #reference>
                                <el-menu-item index="8" @click="add_item">
                                    <span class="material-icons-outlined">
                                        horizontal_rule
                                    </span>
                                </el-menu-item>
                            </template>
                        </el-popover>
                        <el-popover placement="right" title="Comment" :width="200" trigger="hover"
                            content="Comment toolbox item">
                            <template #reference>
                                <el-menu-item index="9" @click="add_item">
                                    <span class="material-icons-outlined">
                                        insert_comment
                                    </span>
                                </el-menu-item>
                            </template>
                        </el-popover>
                        <el-popover placement="right" title="Multiple Text" :width="200" trigger="hover"
                            content="Multiple Text toolbox item">
                            <template #reference>
                                <el-menu-item index="10" @click="add_item">
                                    <span class="material-icons-outlined">
                                        format_align_justify
                                    </span>
                                </el-menu-item>
                            </template>
                        </el-popover>
                    </el-menu>
                </el-col>
                <el-col class="designer" :span="21">
                    <DesignerHeader />
                    <SurveyPage :questions="{ ...questions }" :numOfQuestions="questions.length" />
                    <!-- <el-empty
                        description="The survey is empty. Add an element from the toolbox or click the button below." /> -->
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

<script >
import "survey-core/modern.min.css";
import { StylesManager, SurveyTriggerSetValue } from "survey-core";
StylesManager.applyTheme("modern");

import { Survey } from 'survey-knockout-ui';

import SurveyPage from './SurveyPage.vue';
import DesignerHeader from "./DesignerHeader.vue";
import Radiogroup from "./Radiogroup.vue";

export default {
    name: 'SurveyDesigner',
    components: {
        SurveyPage,
        DesignerHeader,
        Radiogroup
    },
    data() {
        return {
            isCollapse: false,
            surveyJson: {},
            questions: [],
        }
    },
    methods: {
        add_item(el) {
            switch (el.index) {
                case '1':
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
                case '2':
                    console.log('checkbox')
                    break
                case '3':
                    console.log('dropdown')
                    break
                case '4':
                    console.log('boolean')
                    break
                case '5':
                    console.log('file')
                    break
                case '6':
                    console.log('image')
                    break
                case '7':
                    console.log('ranking')
                    break
                case '8':
                    console.log('single')
                    break
                case '9':
                    console.log('comment')
                    break
                case '10':
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