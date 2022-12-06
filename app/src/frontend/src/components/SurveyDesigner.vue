<template>
    <el-tabs v-loading="loading" element-loading-text="Creating..." :element-loading-spinner="svg"
        element-loading-svg-view-box="-10, -10, 50, 50" element-loading-background="rgba(122, 122, 122, 0.8)"
        v-model="activeTab" class="designer-tabs">
        <el-tab-pane label="Designer" name="first">
            <el-row>
                <el-col :span="2" class="toolbox">
                    <el-menu id="tools_menu" :collapse="isCollapse">
                        <el-popover v-for="tool in primaryToolbox" placement="right" :title="tool.name" :width="200"
                            trigger="hover" :content="tool.content">
                            <template #reference>
                                <el-menu-item class="toolbox-item"
                                    :disabled="appStore.getNumberOfPages === 0 ? true : false" :index="tool.id"
                                    @click="add_item">
                                    <span class="material-icons-outlined">{{ tool.type }}</span>
                                </el-menu-item>
                            </template>
                        </el-popover>
                        <el-divider />
                        <el-popover v-for="tool in secondaryToolbox" placement="right" :title="tool.name" :width="200"
                            trigger="hover" :content="tool.content">
                            <template #reference>
                                <el-menu-item class="toolbox-item"
                                    :disabled="appStore.getNumberOfPages === 0 ? true : false" :index="tool.id"
                                    @click="add_item">
                                    <span class="material-icons-outlined">
                                        {{ tool.type }}
                                    </span>
                                </el-menu-item>
                            </template>
                        </el-popover>
                    </el-menu>
                </el-col>
                <el-col :span="21" :xs="20" :sm="20" :md="21" :lg="21" :xl="21" class="designer">
                    <DesignerHeader ref="header" @reset="onReset" v-if="appStore.getNumberOfPages !== 0" />
                    <SurveyPage ref="page1" v-if="appStore.getNumberOfPages !== 0" @deleteQuestion="onDeleteQuestion"
                        @saveChanges="onSaveChanges" :questions="{ ...questions }" :numOfQuestions="numOfQuestions" />
                    <el-empty v-if="appStore.getNumberOfPages === 0"
                        description="Click The Button Below To Get Started">
                        <el-button type="primary" @click="addPage">Add A Page</el-button>
                    </el-empty>
                    <el-row class="submit-survey-container">
                        <el-button-group>
                            <el-button type="info" :disabled="questions.length === 0 ? true : false" @click="saveSurvey"
                                v-if="appStore.getNumberOfPages !== 0" class="create-survey">
                                Save
                            </el-button>
                            <el-button type="warning" :disabled="questions.length === 0 ? true : false"
                                @click="saveSurvey" v-if="appStore.getNumberOfPages !== 0" class="create-survey">
                                Generate Preview
                            </el-button>
                            <el-button type="primary" :disabled="questions.length === 0 ? true : false"
                                @click="createSurvey" v-if="appStore.getNumberOfPages !== 0" class="create-survey">
                                {{ isNewSurvey }}
                            </el-button>
                        </el-button-group>
                    </el-row>
                </el-col>
                <el-col :span="1">
                    <lord-icon class="open-drawer" src="https://cdn.lordicon.com/zmkotitn.json" trigger="loop"
                        delay="2000" state="hover-3" style="width:32px;height:32px" @click="drawer = true">
                    </lord-icon>
                </el-col>
                <el-drawer size="35%" v-model="drawer" title="Survey Settings" direction="rtl">
                    <span v-if="!datesSet" class="error_msg">{{ error_msg }}</span>
                    <h3>Start & End Date</h3>
                    <el-date-picker v-model="surveyDates" type="datetimerange" placeholder="Select date and time"
                        range-separator="To" start-placeholder="Start date" end-placeholder="End date"
                        value-format="YYYY-MM-DDTHH:mm:ss" @change="onDatesSet" />
                    <el-divider />
                    <h3>Survey Complete</h3>
                    <el-input v-model="htmlArea" :autosize="{ minRows: 3, maxRows: 5 }" type="textarea"
                        placeholder="Custom HTML Here (Optional)" />
                    <el-divider />
                    <el-row><el-checkbox v-model="publish" label="Publish Survey" size="larger" /></el-row>
                    <el-button @click="closeDrawer" :style="{ marginTop: '20px' }" type="primary">Save</el-button>
                </el-drawer>
            </el-row>
        </el-tab-pane>
        <el-tab-pane name="second" :disabled="appStore.getNumberOfQuestions === 0 ? true : false">
            <template #label>
                <el-popover :disabled="appStore.getNumberOfQuestions !== 0 ? true : false" :width="180"
                    placement="bottom" trigger="hover" content="Save your changes first">
                    <template #reference>
                        <span class="custom-tabs-label">
                            <span>Preview</span>
                        </span>
                    </template>
                </el-popover>
            </template>
            <div v-if="appStore.getNumberOfPages !== 0" id="survey"></div>
            <el-empty v-else></el-empty>
        </el-tab-pane>
        <el-tab-pane disabled label="JSON Editor" name="third">
            <lord-icon target="div" trigger="hover" icon="system-outline-99-copy"></lord-icon>
        </el-tab-pane>
    </el-tabs>
</template> 

<script lang="ts">
import "survey-core/defaultV2.css";
import { StylesManager } from "survey-core";
StylesManager.applyTheme("defaultV2");
import { Survey } from 'survey-knockout-ui';
import { uuid } from 'vue3-uuid';
import { ElNotification } from 'element-plus'

import SurveyPage from './SurveyPage.vue';
import DesignerHeader from "./DesignerHeader.vue";
import Radiogroup from "./Radiogroup.vue";
import Checkbox from "./Checkbox.vue";
import Carousel from "./Carousel.vue";
import Boolean from "./Boolean.vue";
import Ranking from "./Ranking.vue";
import ImagePicker from "./ImagePicker.vue";
import Dropdown from "./Dropdown.vue";
import Single from "./Single.vue";
import Multiple from "./Multiple.vue";
import Comment from "./Comment.vue";
import Video from "./Video.vue";
import Rating from "./Rating.vue";
import toolbox1 from "../assets/toolbox1.json";
import toolbox2 from "../assets/toolbox2.json";
import { Auth } from 'aws-amplify';
import { useAppStore } from '../store/app';
import axios from "axios";
import { ref } from 'vue';

const tzoffset = (new Date()).getTimezoneOffset() * 60000; //offset in milliseconds

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
    beforeRouteLeave(to, from, next) {
        if (this.isEditing) {
            if (!window.confirm("Leave without saving?")) {
                return;
            }
        }
        next();
    },
    components: {
        SurveyPage,
        DesignerHeader,
        Radiogroup,
        Checkbox,
        Carousel,
        Boolean,
        Ranking,
        ImagePicker,
        Dropdown,
        Single,
        Multiple,
        Comment,
        Video,
        Rating
    },
    setup() {
        const appStore = useAppStore();
        Auth.currentSession().then((session) => {
            axios.defaults.headers.common['Authorization'] = `Bearer ${session.getAccessToken().getJwtToken()}`;
            axios.defaults.headers.post['Content-Type'] = 'application/json';
        });
        return {
            appStore
        }
    },
    beforeMount() {
        this.numOfQuestions = this.appStore.getNumberOfQuestions;
        if (this.appStore.getQuestions.length !== 0) {
            this.appStore.getQuestions.elements.forEach((element) => {
                switch (element.type) {
                    case "radiogroup":
                        this.questions.push(Radiogroup);
                        break;
                    case "checkbox":
                        this.questions.push(Checkbox);
                        break;
                    case "dropdown":
                        this.questions.push(Dropdown);
                        break;
                    case "boolean":
                        this.questions.push(Boolean);
                        break;
                    case "ranking":
                        this.questions.push(Ranking);
                        break;
                    case "rating":
                        this.questions.push(Rating);
                        break;
                    case "text":
                        this.questions.push(Single);
                        break;
                    case "multipletext":
                        this.questions.push(Multiple);
                        break;
                    case "comment":
                        this.questions.push(Comment);
                        break;
                    case "panel":
                        this.questions.push(Video);
                        break;
                }
            });
        }
    },
    mounted() {
        if (this.appStore.getQuestions.length !== 0) {
            this.$refs.header.surveyName = this.appStore.getTitle;
            this.$refs.header.surveyDesc = this.appStore.getDescription;
            let fetchQuestions = this.appStore.getQuestions.elements;
            console.log(fetchQuestions);

            for (let i = 0; i < this.questions.length; i++) {
                let q = fetchQuestions[i]
                if (['radiogroup', 'checkbox', 'dropdown', 'ranking'].includes(q.type)) {
                    this.$refs.page1.$refs[i][0].$refs.questionTitle.question = q.title;
                    this.$refs.page1.$refs[i][0].$refs.deleteQuestion.required = q.isRequired
                    q.choices.forEach((choice, index) => {
                        this.$refs.page1.$refs[i][0].list[index].addremove = 0
                        this.$refs.page1.$refs[i][0].list[index].binding = choice
                    });
                }
                else if (q.type === 'boolean') {
                    this.$refs.page1.$refs[i][0].$refs.questionTitle.question = q.title;
                    this.$refs.page1.$refs[i][0].$refs.deleteQuestion.required = q.isRequired
                    this.$refs.page1.$refs[i][0].left = q.labelFalse
                    this.$refs.page1.$refs[i][0].right = q.labelTrue
                }
                else if (q.type === 'rating') {
                    this.$refs.page1.$refs[i][0].$refs.questionTitle.question = q.title;
                    this.$refs.page1.$refs[i][0].$refs.deleteQuestion.required = q.isRequired

                    let ratings = []
                    for (let j = 0; j < q.rateMax; j++) {
                        ratings.push(j + 1)
                    }
                    this.$refs.page1.$refs[i][0].ratings = ratings
                }
                else if (q.type === 'text') {
                    this.$refs.page1.$refs[i][0].$refs.questionTitle.question = q.title;
                    this.$refs.page1.$refs[i][0].$refs.deleteQuestion.required = q.isRequired
                }
                else if (q.type === 'comment') {
                    this.$refs.page1.$refs[i][0].$refs.questionTitle.question = q.title;
                    this.$refs.page1.$refs[i][0].$refs.deleteQuestion.required = q.isRequired
                }
                else if (q.type === 'multipletext') {
                    this.$refs.page1.$refs[i][0].$refs.questionTitle.question = q.title;
                    this.$refs.page1.$refs[i][0].$refs.deleteQuestion.required = q.isRequired
                    q.items.forEach((item, index) => {
                        this.$refs.page1.$refs[i][0].list[index].addremove = 0
                        this.$refs.page1.$refs[i][0].list[index].name = item.name
                    });
                    this.$refs.page1.$refs[i][0].list.push({ addremove: 1, name: 'Other' })
                }
                else if (q.type === 'panel') {
                    this.$refs.page1.$refs[i][0].$refs.questionTitle.question = q.elements[0].title;
                    this.$refs.page1.$refs[i][0].$refs.deleteQuestion.required = q.isRequired
                    let url = q.elements[1].html.substring(q.elements[1].html.indexOf("src") + 5, q.elements[1].html.indexOf("type") - 2)
                    this.$refs.page1.$refs[i][0].video_url = url
                    if (q.elements[1].html !== '') {
                        this.$refs.page1.$refs[i][0].showVideo = true;
                    }
                }
            }

            this.onSaveChanges();
        }
    },
    data() {
        return {
            surveyId: '',
            idx: 0,
            drawer: false,
            surveyDates: [
                new Date(Date.now() - tzoffset).toISOString().slice(0, -1),
                new Date(Date.now() + 604800000 - tzoffset).toISOString().slice(0, -1)
            ],
            datesSet: false,
            publish: false,
            error_msg: '',
            surveyQuestions: {},
            htmlArea: '',
            timer: {} as any,
            isCollapse: false,
            questions: [] as Array<any>,
            numOfQuestions: 0,
            pages: [] as Array<string>,
            activeTab: ref('first'),
            primaryToolbox: toolbox1,
            secondaryToolbox: toolbox2,
            saveDisabled: true,
            isEditing: false,
            loading: false,
            svg: "<path class='path' d='M 30 15 L 28 17 M 25.61 25.61 A 15 15, 0, 0, 1, 15 30 A 15 15, 0, 1, 1, 27.99 7.5 L 15 15' style='stroke-width: 4px; fill: rgba(0, 0, 0, 0)'/>"
        }
    },
    computed: {
        isNewSurvey() {
            return this.appStore.getSurveyId === '' ? "Create" : "Save Edits";
        }
    },
    methods: {
        closeDrawer() {
            this.drawer = false;
        },
        onDatesSet() {
            this.datesSet = true;
        },
        preventNav(event) {
            if (!this.isEditing) return
            event.preventDefault()
            event.returnValue = ""
        },
        createSurvey() {
            if (!this.datesSet) {
                this.drawer = true;
                this.error_msg = 'You Must Set Start & End Dates First';
            } else {
                this.appStore.getQuestions.elements.forEach((question) => {
                    if (['radiogroup', 'checkbox', 'dropdown', 'ranking'].includes(question.type)) {
                        this.surveyQuestions[question.title] = question.choices;
                    } else if (question.type === 'boolean') {
                        this.surveyQuestions[question.title] = [question.labelFalse, question.labelTrue]
                    } else if (question.type === 'rating') {
                        this.surveyQuestions[question.title] = [question.rateMax]
                    } else if (['single', 'comment'].includes(question.type)) {
                        this.surveyQuestions[question.title] = [""]
                    } else if (question.type === 'multiple') {
                        this.surveyQuestions[question.title] = question.items
                    } else if (question.type === 'panel') {
                        this.surveyQuestions[question.elements[0].title] = [question.elements[1].html]
                    }
                });
                this.loading = true;
                axios.post('/createForm', {
                    form_id: this.appStore.getSurveyId === "" ? uuid.v4() : this.appStore.getSurveyId,
                    title: this.appStore.getTitle,
                    description: this.appStore.getDescription,
                    questions: this.surveyQuestions,
                    status: this.publish ? 'ACTIVE' : '"EDITING"',
                    created_at: new Date().toISOString(),
                    last_modified_at: new Date().toISOString(),
                    published_at: this.surveyDates[0],
                    closing_at: this.surveyDates[1],
                    sharedPublicly: false,
                    submissions_count: 0,
                    surveyJson: JSON.stringify(this.appStore.getSurvey)
                }).then((response) => {
                    setTimeout(() => {
                        this.loading = false;
                        ElNotification({
                            title: 'Success',
                            message: response.data,
                            type: 'success',
                        })
                    }, 3000);
                    this.idx = 0;
                    this.questions = [] as Array<any>;
                    this.numOfQuestions = 0;
                    this.pages = [] as Array<string>;
                    this.appStore.reset();
                    console.log(response);
                }).catch((error) => {
                    this.loading = false;
                    console.log(error);
                });
            }
        },
        saveSurvey() {
            for (let i = 0; i < this.questions.length; i++) {
                this.$refs.page1.$refs[i][0].save();
            }
        },
        addPage() {
            this.idx += 1;
            this.appStore.addPage({
                "name": "Page" + this.idx,
                "elements": []
            })
            this.pages.push("Page" + this.idx);
        },
        onReset() {
            this.idx = 0;
            this.numOfQuestions = 0;
            this.questions = [] as Array<any>;
        },
        onDeleteQuestion(num: number) {
            this.questions.splice(num, 1);
            this.appStore.removeElement(num);
            this.numOfQuestions -= 1;
            this.onSaveChanges();
        },
        onSaveChanges() {
            let survey = new Survey(this.appStore.getSurvey);
            survey.render("survey");
        },
        add_item(el) {
            this.isEditing = true;
            this.numOfQuestions += 1;
            switch (el.index) {
                case "1":
                    this.questions.push(Radiogroup);
                    break
                case "2":
                    this.questions.push(Checkbox);
                    break
                case "3":
                    this.questions.push(Dropdown);
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
                    this.questions.push(Rating);
                    break
                case "10":
                    this.questions.push(Video);
                    break
                case "11":
                    this.questions.push(Single);
                    break
                case "12":
                    this.questions.push(Comment);
                    break
                case "13":
                    this.questions.push(Multiple);
                    break
            }
        }
    },
}

export interface Item {
    name: string;
}

export interface Question {
    type: string,
    name: string,
    title: string,
    isRequired: boolean,
    choices?: Array<string>,
    labelTrue?: string,
    labelFalse?: string,
    rateMax?: number,
    items?: Array<Item>,
    html?: string,
}
</script>

<style>
.error_msg {
    color: red;
    font-style: italic;
}

.open-drawer {
    -moz-transform: scale(-1, 1);
    -webkit-transform: scale(-1, 1);
    -o-transform: scale(-1, 1);
    -ms-transform: scale(-1, 1);
    transform: scale(-1, 1);
    float: right;
    cursor: pointer;
}

.submit-survey-container {
    width: 100%;
    justify-content: center;
}

.create-survey {
    margin: 20px;
    height: 50px;
    width: 200px;
    font-size: large;
}

#tools_menu {
    height: 100%;
}

.designer {
    padding-top: 30px;
    padding-right: 30px;
    padding-left: 30px;
    background-color: #f3f3f3;
}

.toolbox-item {
    justify-content: center;
}
</style>