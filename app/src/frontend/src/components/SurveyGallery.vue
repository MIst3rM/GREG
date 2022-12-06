<template>
    <h1 class="title">Your Surveys</h1>
    <el-divider />

    <h3 class="active-subtitle">Active Surveys</h3>
    <el-carousel v-if="activeSurveys.length !== 0" class="carousel" trigger="click" :autoplay=false type="card"
        height="450px">
        <el-carousel-item class="carousel-item" v-for="survey in activeSurveys" :key="survey.form_id">
            <el-card :body-style="{ padding: '0px' }">
                <img :src="carousel_img" class="image" alt="" />
                <div style="padding: 14px">
                    <span>{{ survey.title }}</span>
                    <div class="bottom">
                        published on:<time class="time">{{ formattedDate(survey.published_at) }}</time>
                        last modified on:<time class="time">{{ formattedDate(survey.last_modified_at) }}</time>
                        <p>{{ survey.description }}</p>
                        <el-button @click="showDetails(survey)" text class="button">See Details</el-button>
                    </div>
                </div>
            </el-card>
        </el-carousel-item>
    </el-carousel>
    <el-empty description="Nothing to show you" v-else>
        <el-button @click="$router.push('/survey-designer')" type="primary">Click here to start</el-button>
    </el-empty>

    <h3 class="editing-subtitle">Currently Editing</h3>
    <el-carousel v-if="editingSurveys.length !== 0" class="carousel" trigger="click" :autoplay=false type="card"
        height="450px">
        <el-carousel-item class="carousel-item" v-for="survey in editingSurveys" :key="survey.form_id">
            <el-card :body-style="{ padding: '0px' }">
                <img :src="carousel_img" class="image" alt="" />
                <div style="padding: 14px">
                    <span>{{ survey.title }}</span>
                    <div class="bottom">
                        created on:<time class="time">{{ formattedDate(survey.created_at) }}</time>
                        last modified on:<time class="time">{{ formattedDate(survey.last_modified_at) }}</time>
                        <p>{{ survey.description }}</p>
                        <el-button @click="continueEditing(survey)" text class="button">Continue Editing</el-button>
                    </div>
                </div>
            </el-card>
        </el-carousel-item>
    </el-carousel>
    <el-empty description="You have no surveys in editing" v-else>
        <el-button @click="$router.push('/survey-designer')" type="primary">Make A New Survey</el-button>
    </el-empty>

    <h3 class="expired-subtitle">Past Surveys</h3>
    <el-carousel v-if="expiredSurveys.length !== 0" class="carousel" trigger="click" :autoplay=false type="card"
        height="450px">
        <el-carousel-item class="carousel-item" v-for="survey in expiredSurveys" :key="survey.form_id">
            <el-card :body-style="{ padding: '0px' }">
                <img :src="carousel_img" class="image" alt="" />
                <div style="padding: 14px">
                    <span>{{ survey.title }}</span>
                    <div class="bottom">
                        created on:<time class="time">{{ formattedDate(survey.created_at) }}</time>
                        last modified on:<time class="time">{{ formattedDate(survey.last_modified_at) }}</time>
                        <p>{{ survey.description }}</p>
                        <el-button v-if="!survey.sharedPublicly" @click="exposeSurveyApi(survey.form_id, true)" text class="button">Make Results Public</el-button>
                        <el-button v-else @click="exposeSurveyApi(survey.form_id, false)" text class="button">Stop Sharing Results</el-button>
                    </div>
                </div>
            </el-card>
        </el-carousel-item>
    </el-carousel>
    <el-empty v-else description="You have no past surveys"></el-empty>

    <el-dialog v-model="showDialog" title="Survey Details" width="30%" align-center center>
        <el-progress type="dashboard" :percentage="percentageDays" :color="colors">
            <template #default="{ percentage }">
                <span class="percentage-value">{{ percentage }}%</span>
                <span class="percentage-label">{{ progress_msg }}</span>
            </template>
        </el-progress>
        <h4>Has been running for {{ running_for }} day(s)</h4>
        <h4>Has {{ respondents }} respondent(s)</h4>
    </el-dialog>
</template>

<script lang="ts">
import { Auth } from 'aws-amplify';
import axios from 'axios';
import { useAppStore } from '../store/app';
import carousel_card_img from '../assets/carousel_card_img.webp';
import { PropType } from 'vue';
import { h } from 'vue'
import { ElNotification } from 'element-plus'

export default {
    name: 'SurveyGallery',
    emits: ['shared'],
    props: {
        surveys: {
            type: Array as PropType<Survey[]>,
            required: true,
        }
    },
    setup() {
        const appStore = useAppStore();
        return {
            appStore
        }
    },
    data() {
        return {
            carousel_img: carousel_card_img,
            showDialog: false,
            percentageDays: 0,
            progress_msg: 'Progressing',
            running_for: 0,
            respondents: 0,
            colors: [
                { color: '#f56c6c', percentage: 20 },
                { color: '#e6a23c', percentage: 40 },
                { color: '#e6a23c', percentage: 60 },
                { color: '#1989fa', percentage: 80 },
                { color: '#5cb87a', percentage: 100 },
            ]
        }
    },
    methods: {
        async exposeSurveyApi(form_id: string, share: boolean) {
            await Auth.currentSession().then((session) => {
                axios.defaults.headers.common['Authorization'] = `Bearer ${session.getAccessToken().getJwtToken()}`;
                axios.defaults.headers.common['Content-Type'] = 'application/json';
            });
            await axios.put("/setShareable", {
                form_id: form_id,
                sharedPublicly: share
            })
                .then((response) => {
                    console.log(response)
                    this.$emit('shared')
                    ElNotification({
                        title: 'Success',
                        message: response.data,
                        type: 'success',
                    })
                }, (error) => {
                    console.log(error);
                    ElNotification({
                        title: 'Error',
                        message: error.response.data,
                        type: 'error',
                    })
                });
        },
        formattedDate(date: string) {
            return new Date(date).toLocaleDateString(
                'en-gb',
                {
                    year: 'numeric',
                    month: 'long',
                    day: 'numeric'
                }
            );
        },
        showDetails(survey: Survey) {
            this.showDialog = true;
            if (survey.status === 'ACTIVE') {

                let start_date = Date.parse(survey.published_at) / 1000;
                let end_date = Date.parse(survey.closing_at) / 1000;
                let total_running_days = Math.floor((end_date - start_date) / (60 * 60 * 24));
                let timeLeft = Math.floor((end_date - (Date.now() / 1000)) / (60 * 60 * 24));

                this.running_for = total_running_days - timeLeft;

                // Less than a day left
                if (timeLeft < 1 && timeLeft >= 0) {
                    this.percentageDays = 99;
                }
                else if (timeLeft < 0) {
                    this.percentageDays = 100;
                }
                else {
                    this.percentageDays = Math.floor(((total_running_days - timeLeft) / total_running_days) * 100);
                }

                this.respondents = survey.submissions_count;
            }
        },
        continueEditing(survey: Survey) {
            this.appStore.setSurvey(JSON.parse(survey.surveyJson));
            this.appStore.setSurveyId(survey.form_id);
            this.$router.push('/survey-designer');
        }
    },
    computed: {
        activeSurveys() {
            return this.surveys.filter((survey: any) => survey.status === 'ACTIVE');
        },
        editingSurveys() {
            return this.surveys.filter((survey: any) => survey.status === 'EDITING');
        },
        expiredSurveys() {
            return this.surveys.filter((survey: any) => survey.status === 'EXPIRED');
        }
    }
}

export interface Survey {
    form_id: string;
    title: string;
    description: string;
    status: string;
    created_at: string;
    last_modified_at: string;
    published_at: string;
    closing_at: string;
    submissions_count: number;
    surveyJson: string;
    sharedPublicly: boolean;
}

</script>

<style scoped>
.el-progress {
    width: 100%;
}

.el-progress:deep(.el-progress-circle) {
    margin: 0 auto;
}

.percentage-value {
    display: block;
    margin-top: 10px;
    font-size: 28px;
}

.percentage-label {
    display: block;
    margin-top: 10px;
    font-size: 12px;
}

.time {
    font-size: 12px;
    color: #999;
    margin: 5px 0 5px 5px;
}

.el-carousel:deep(.el-carousel__item) {
    background-color: transparent !important;
}

.el-button {
    min-height: auto;
}

.bottom {
    margin-top: 13px;
    line-height: 12px;
    display: flex;
    font-size: 13px;
    flex-direction: column;
}

.image {
    width: 100%;
    height: 200px;
    display: block;
    object-fit: cover;
}

.title {
    margin: 40px 40px 0 40px;
}

.active-subtitle {
    margin: 0 40px 40px 40px;
}

.expired-subtitle,
.editing-subtitle {
    margin: 40px 40px 0 40px;
}

.carousel {
    margin: 0 auto;
    width: 60%;
}

.el-carousel__item h3 {
    color: #475669;
    opacity: 0.75;
    line-height: 200px;
    margin: 0;
    text-align: center;
}

.el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n + 1) {
    background-color: #d3dce6;
}
</style>
