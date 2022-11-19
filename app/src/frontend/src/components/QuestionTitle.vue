<template>
    <el-row class="question-title-container">
        <el-form :inline="true" class="question-title">
            <el-form-item :label="num + '.'" class="question">
                <el-input v-model="question" size="large" placeholder="Question" />
            </el-form-item>
            <el-form-item class="status">
                <span v-if="saving" class="ping"></span>
            </el-form-item>
        </el-form>
    </el-row>
</template>

<script lang="ts">
export default {
    name: 'QuestionTitle',
    props: {
        num: {
            type: Number,
            required: true,
            default: 0
        },
        saving: {
            type: Boolean,
            required: true,
            default: false
        }
    },
    data() {
        return {
            question: ''
        }
    }
}
</script>

<style scoped>
.question-title-container {
    padding: 20px 20px 0px 20px;
}

.question-title {
    display: flex;
    justify-content: space-between;
    width: 100%;
}

.question {
    width: 100%;
}

.status {
    width: 20%;
    align-items: center;
}

.ping {
    height: 25px;
    width: 25px;
    background-color: #bbb;
    border-radius: 50%;
    display: inline-block;
    -webkit-animation: ping 2s cubic-bezier(.39, .575, .565, 1.000) infinite both;
    animation: ping 2s cubic-bezier(.39, .575, .565, 1.000) infinite both
}

@-webkit-keyframes ping {
    0% {
        -webkit-transform: scale(.2);
        transform: scale(.2);
        opacity: .8
    }

    80% {
        -webkit-transform: scale(1.2);
        transform: scale(1.2);
        opacity: 0
    }

    100% {
        -webkit-transform: scale(2.2);
        transform: scale(2.2);
        opacity: 0
    }
}

@keyframes ping {
    0% {
        -webkit-transform: scale(.2);
        transform: scale(.2);
        opacity: .8
    }

    80% {
        -webkit-transform: scale(1.2);
        transform: scale(1.2);
        opacity: 0
    }

    100% {
        -webkit-transform: scale(2.2);
        transform: scale(2.2);
        opacity: 0
    }
}
</style>