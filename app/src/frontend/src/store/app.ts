import { defineStore } from "pinia";
import { uuid } from 'vue3-uuid';

export const useAppStore = defineStore("app", {
  state: () => ({
    surveyJson: {
        "surveyId": uuid.v4(),
        "title": "",
        "description": "",
        "logoPosition": "right",
        "pages": [] as Array<{}>
    },
    questions: [] as Array<any>
  }),
  getters: {
    getSurvey: (state) => {
        return state.surveyJson;
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
        return state.surveyJson.pages.reduce((acc, page) => {
            return acc.concat(page.elements);
        });
    },
    getTitle: (state) => {
        return state.surveyJson.title;
    },
    getDescription: (state) => {
        return state.surveyJson.description;
    },
    getSurveyId: (state) => {
        return state.surveyJson.surveyId;
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
    setTitle(title: string) {
        this.surveyJson.title = title;
    },
    setDescription(description: string) {
        this.surveyJson.description = description;
    },
    saveQuestions(questions: Array<any>) {
        this.questions = questions;
    }
  },
  persist: {
    storage: sessionStorage,
  },
});