import { createApp } from "vue";
import { createPinia } from "pinia";
import piniaPluginPersistedstate from "pinia-plugin-persistedstate";
import ElementPlus from "element-plus";
import * as ElementPlusIconsVue from "@element-plus/icons-vue";
import { createRouter, createWebHistory } from "vue-router";
import { vue3Debounce } from 'vue-debounce'
import "element-plus/dist/index.css";
import "./style.css";
import "survey-core/defaultV2.min.css";
import "survey-core/modern.min.css";
import "survey-creator-core/survey-creator-core.min.css";
import App from "./App.vue";
import axios from "axios";
import UUID from "vue3-uuid";

import { Home, Profile, SharedResults } from "./views";
import { SurveyDesigner } from "./components";

axios.defaults.baseURL = "https://backend-env.eba-ndzmkia6.us-east-1.elasticbeanstalk.com/api/v1";

const routes = [
  { path: "/", component: Home },
  { path: "/profile", component: Profile },
  { path: "/survey-designer", component: SurveyDesigner },
  { path: "/results", component: SharedResults },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

const pinia = createPinia();
pinia.use(piniaPluginPersistedstate);

const app = createApp(App);
app.config.warnHandler = () => null;
app.use(router);
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component);
}
app.use(pinia);
app.use(ElementPlus);
app.directive('debounce', vue3Debounce({ lock: true }))
app.use(UUID);
app.mount("#app");