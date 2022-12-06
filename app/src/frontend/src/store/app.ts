import { defineStore } from "pinia";

export const useAppStore = defineStore("app", {
  state: () => ({
    surveyJson: {
        "title": "",
        "description": "",
        "logoPosition": "right",
        "completedHtml": "<h3>Thank you for completing this survey</h3>",
        "pages": [] as Array<{}>,
    },
    surveyId: "",
    questions: [] as Array<{}>,
  }),
  getters: {
    getSurvey: (state) => {
        return state.surveyJson;
    },
    getSurveyId: (state) => {
        return state.surveyId;
    },
    getNumberOfPages: (state) => {
        return state.surveyJson.pages.length;
    },
    getNumberOfQuestions: (state) => {
        return state.surveyJson.pages.reduce((acc, page) => {
            return acc + page.elements.length;
        }, 0);
    },
    getQuestions: (state) => {
        if (state.surveyJson.pages.length === 0) {
            return [];
        }
        return state.surveyJson.pages.reduce((acc, page) => {
            return acc.concat(page.elements);
        });
    },
    getSavedQuestions: (state) => {
        return state.questions;
    },
    getTitle: (state) => {
        return state.surveyJson.title;
    },
    getDescription: (state) => {
        return state.surveyJson.description;
    }
  },
  actions: {
    addPage(page: {}) {
        this.surveyJson.pages.push(page);
    },
    addQuestion(page_name: string, question: {}) {
       this.surveyJson.pages[0].elements.push(question);
    },
    findElement(idx: number) {
        if (this.surveyJson.pages[0].elements[idx] !== undefined) {
            return true;
        }else{
            return false;
        }
    },
    updateElement(idx: number, question: {}) {
        this.surveyJson.pages[0].elements[idx] = question;
    },
    removeElement(idx: number) {
        this.surveyJson.pages[0].elements.splice(idx, 1);
    },
    setSurvey(survey: {}) {
        this.surveyJson = survey;
    },
    setSurveyId(surveyId: string) {
        this.surveyId = surveyId;
    },
    setTitle(title: string) {
        this.surveyJson.title = title;
    },
    setDescription(description: string) {
        this.surveyJson.description = description;
    },
    saveQuestions(questions: Array<any>) {
        this.questions = questions;
    },
    clearSurvey() {
        this.surveyJson = {
            "title": "",
            "description": "",
            "logoPosition": "right",
            "completedHtml": "<h3>Thank you for completing this survey</h3>",
            "pages": [] as Array<{}>,
        };
        this.questions = [] as Array<{}>;
    },
    reset(){
        this.surveyJson.title = "";
        this.surveyJson.description = "";
        this.surveyJson.pages = [] as Array<{}>;
        this.questions = [] as Array<{}>;
        this.surveyId = "";
    }
  },
  persist: {
    storage: sessionStorage,
  },
});