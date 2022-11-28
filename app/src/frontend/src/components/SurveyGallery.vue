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
                        created:<time class="time">{{ formattedDate(survey.created_at) }}</time>
                        last modified:<time class="time">{{ formattedDate(survey.last_modified_at) }}</time>
                        <p>{{ survey.description }}</p>
                        <el-button @click="exposeSurveyApi" text class="button">Make Results Public</el-button>
                    </div>
                </div>
            </el-card>
        </el-carousel-item>
    </el-carousel>
    <el-empty description="Nothing to show you" v-else>
        <el-button @click="$router.push('/survey-designer')" type="primary">Click here to start</el-button>
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
                        created:<time class="time">{{ formattedDate(survey.created_at) }}</time>
                        last modified:<time class="time">{{ formattedDate(survey.last_modified_at) }}</time>
                        <p>{{ survey.description }}</p>
                        <el-button @click="exposeSurveyApi" text class="button">Make Results Public</el-button>
                    </div>
                </div>
            </el-card>
        </el-carousel-item>
    </el-carousel>
    <el-empty v-else description="You have no past surveys"></el-empty>
</template>

<script lang="ts">
import carousel_card_img from '../assets/carousel_card_img.webp';

export default {
    name: 'SurveyGallery',
    props: {
        surveys: {
            type: Array,
            required: true,
        }
    },
    data() {
        return {
            carousel_img: carousel_card_img
        }
    },
    methods: {
        exposeSurveyApi(form_id: string) {
            console.log('exposeSurveyApi');
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
        }
    },
    computed: {
        activeSurveys() {
            return this.surveys.filter((survey: any) => survey.status === 'ACTIVE');
        },
        expiredSurveys() {
            return this.surveys.filter((survey: any) => survey.status === 'EXPIRED');
        }
    }
}

</script>

<style scoped>
.time {
    font-size: 12px;
    color: #999;
}

.el-carousel>>>.el-carousel__item {
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

.expired-subtitle {
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
