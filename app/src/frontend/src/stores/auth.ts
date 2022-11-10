import { defineStore } from "pinia";

export const useAuthStore = defineStore("auth", {
  state: () => ({
    user: null,
    isAuthenticated: false,
    role: "",
  }),
  getters: {
    getUser: (state) => state.user,
    getIsAuthenticated: (state) => state.isAuthenticated,
    getRole: (state) => state.role,
  },
  actions: {
    login(user) {
      this.user = user;
      this.isAuthenticated = true;
      this.role = "admin";
    },
    logout() {
      this.user = null;
      this.isAuthenticated = false;
      this.role = "";
    },
  },
  persist: {
    storage: sessionStorage,
  },
});
