<template>
    <el-row class="question-title-container">
        <el-col :span="20">
            <el-form>
                <el-form-item :label="num + '.'" />
            </el-form>
        </el-col>
    </el-row>
    <el-row class="carousel-container">
        <el-carousel trigger="click" :autoplay=false type="card" height="350px">
            <el-carousel-item class="carousel-item" v-for="question in questions" :key="question">
                <el-input v-model="question.binding" type="textarea" class="question"
                    :autosize="{ minRows: 2, maxRows: 6 }" :input-style="{
                        background: 'transparent', border: 'none', fontSize: '20px', textAlign: 'center', boxShadow: 'none'
                    }" :placeholder="question.name" resize="none" />
            </el-carousel-item>
        </el-carousel>
        <el-button-group class="answer-btns">
            <el-button v-for="btn in answers" :key="btn">
                {{ btn }}
            </el-button>
        </el-button-group>
    </el-row>
    <DeleteQuestion ref="deleteQuestion" :num="num" @deleteQuestion="onDeleteQuestion" />
</template>

<script lang="ts">
import DeleteQuestion from './DeleteQuestion.vue'
const qs = ["Question1", "Question2", "Question3", "Question4", "Question5", "Question6"]
export default {
    name: 'Carousel',
    props: {
        num: {
            type: Number,
            default: 0
        },
    },
    components: {
        DeleteQuestion
    },
    data() {
        return {
            questions: qs.map((name) => {
                return { name, binding: "" };
            }),
            answers: ["Strongly agree", "Somewhat agree", "Neutral", "Somewhat disagree", "Strongly disagree"],
        }
    },
    methods: {
        onDeleteQuestion(idx: number) {
            this.$emit('deleteQuestion', idx)
        }
    }
}
</script>

<style scoped>
.question {
    --el-input-placeholder-color: black;
}

.question-title-container {
    padding: 20px 20px 0px 20px;
}

.carousel-container {
    padding: 0 50px;
}

.el-carousel {
    width: 100%;
}

.answer-btns {
    width: 50%;
    margin: 0 auto;
    display: inline-grid;
}

.el-carousel__item {
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
}

.el-carousel__item h3 {
    color: #475669;
    opacity: 0.75;
    line-height: 200px;
    margin: 0;
    text-align: center;
}

.el-carousel__item:nth-child(2n) {
    background-color: #76c3b4;
}

.el-carousel__item:nth-child(2n + 1) {
    background-color: #c5e9e2;
}
</style>