<template>
    <h1 class="title">Survey Results</h1>
    <el-divider />
    <el-carousel class="carousel" trigger="click" :autoplay=false type="card" height="350px">
        <el-carousel-item class="carousel-item" v-for="survey in surveys" :key="survey">
            <el-card :body-style="{ padding: '0px' }">
                <img :src="carousel_img" class="image" alt="" />
                <div style="padding: 14px">
                    <span>{{ survey.title }}</span>
                    <div class="bottom">
                        <p>{{ survey.description }}</p>
                        <el-button @click="requestAccess" text class="button">Request Access</el-button>
                    </div>
                </div>
            </el-card>
        </el-carousel-item>
    </el-carousel>
</template>

<script lang="ts">
import carousel_card_img from '../assets/carousel_card_img.webp';
import axios from 'axios';

export default {
    name: 'SharedResults',
    data() {
        return {
            surveys: [],
            carousel_img: carousel_card_img
        }
    },
    async beforeMount() {
        await axios.get('/getAllSharedForms')
            .then((response) => {
                this.surveys = response.data;
            })
            .catch((error) => {
                console.log(error);
            });
    },
    methods: {
        requestAccess() {
            console.log('requestAccess');
        }
    }
};
</script>

<style scoped>
.title {
    margin: 20px;
}

.carousel {
    margin: 0 auto;
    width: 60%;
}

.image {
    width: 100%;
    height: 200px;
    display: block;
    object-fit: cover;
}

.bottom {
    margin-top: 13px;
    line-height: 12px;
    display: flex;
    font-size: 13px;
    flex-direction: column;
}
</style>