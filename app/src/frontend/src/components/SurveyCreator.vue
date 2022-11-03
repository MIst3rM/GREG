<template>
    <div id="surveyCreator" />
</template>

<script>
import { SurveyCreator } from "survey-creator-knockout";
const creatorOptions = {
    showLogicTab: true,
    isAutoSave: true
};

export default {
    name: 'SurveyCreator',
    components: {
    },
    data() {
        return {
        }
    },
    mounted() {
        const creator = new SurveyCreator(creatorOptions);
        creator.render("surveyCreator");
    }
}
</script>

<style scoped>
#surveyCreator {
    height: 100vh;
    width: 100vw;
}
</style>