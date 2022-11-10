import { createApp } from "vue";
import { createAuth0 } from "@auth0/auth0-vue";
import { createPinia } from "pinia";
import piniaPluginPersistedstate from "pinia-plugin-persistedstate";
import ElementPlus from "element-plus";
import * as ElementPlusIconsVue from "@element-plus/icons-vue";
import { createRouter, createWebHistory } from "vue-router";
import "element-plus/dist/index.css";
import "./style.css";
import "survey-core/defaultV2.min.css";
import "survey-core/modern.min.css";
import "survey-creator-core/survey-creator-core.min.css";
import App from "./App.vue";

import { Home, Profile, Report } from "./views";
import { SurveyDesigner, SurveyCreator } from "./components";

const routes = [
  { path: "/", component: Home },
  { path: "/profile", component: Profile },
  { path: "/survey-designer", component: SurveyDesigner },
  { path: "/survey-creator", component: SurveyCreator },
  { path: "/report", component: Report },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

const pinia = createPinia();
pinia.use(piniaPluginPersistedstate);

const app = createApp(App);
app.use(router);
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component);
}
app.use(pinia);
app.use(ElementPlus);
app.use(
  createAuth0({
    domain: "dev-zpqo8nwz3kxik1py.us.auth0.com",
    client_id: "7SaF0Mjp9BdXrc1sIAnNKS1iyc1eRNNo",
    redirect_uri: window.location.origin,
  })
);
app.mount("#app");