import { defineStore } from "pinia";

export const useAppStore = defineStore("app", {
  state: () => ({
    surveyJson: {
        "title": "",
        "description": "",
        "logoPosition": "right",
        "pages": [] as Array<{}>
    },
  }),
  getters: {
    getSurvey: (state) => {
        return state.surveyJson;
    },
    getNumberOfPages: (state) => {
        return state.surveyJson.pages.length;
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
    }
  },
  persist: {
    storage: sessionStorage,
  },
});