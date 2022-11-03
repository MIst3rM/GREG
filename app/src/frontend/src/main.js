import { createApp } from "vue";
import ElementPlus from "element-plus";
import * as ElementPlusIconsVue from "@element-plus/icons-vue";
import { createRouter, createWebHistory } from "vue-router";
import "element-plus/dist/index.css";
import "./style.css";
import "survey-core/defaultV2.min.css";
import "survey-core/modern.min.css";
import "survey-creator-core/survey-creator-core.min.css";
import App from "./App.vue";

import { Home, Profile } from "./views";
import { SurveyDesigner, SurveyCreator } from "./components";

const routes = [
  { path: "/", component: Home },
  { path: "/profile", component: Profile },
  { path: "/survey-designer", component: SurveyDesigner },
  { path: "/survey-creator", component: SurveyCreator },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

const app = createApp(App);
app.use(ElementPlus);
app.use(router);
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component);
}
app.mount("#app");
