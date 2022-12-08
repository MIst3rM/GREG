<template>
    <h1 class="title">Survey Results</h1>
    <el-divider />
    <h5 class="subtitle1">To get access to publicly shared survey results, you must first obtain an API key.</h5>
    <h5 class="subtitle2">To get started, click on request access on the survey that you want the results for.</h5>

    <h3 class="subtitle4">Request API Key</h3>
    <el-row class="request-form">
        <el-form ref="requestFormRef" :inline="true" :model="requestForm" :rules="rules">
            <el-form-item label="Email" prop="email">
                <el-input class="email" v-model="requestForm.email" placeholder="Enter your email" type="email" />
            </el-form-item>
            <el-form-item>
                <el-button class="button" type="primary" @click="submitForm(requestFormRef)">
                    Request Access Key
                </el-button>
            </el-form-item>
        </el-form>
    </el-row>

    <h3 class="subtitle3">Browse Surveys</h3>
    <el-carousel class="carousel" trigger="click" :autoplay=false type="card" height="450px">
        <el-carousel-item class="carousel-item" v-for="survey in surveys" :key="survey">
            <el-card :body-style="{ padding: '0px' }">
                <img :src="carousel_img" class="image" alt="" />
                <div style="padding: 14px">
                    <span>{{ survey.title }}</span>
                    <div class="bottom">
                        <h4>{{ survey.submissions_count }} respondent(s)</h4>
                        <p>{{ survey.description }}</p>
                        <el-button @click="requestAccess(survey.form_id)" text class="button">Get Api
                            Endpoint</el-button>
                    </div>
                </div>
            </el-card>
        </el-carousel-item>
    </el-carousel>

    <el-dialog v-model="dialogVisible" title="Request Example" width="40%">
        <h2>Never Share Your Api Key</h2>
        <el-input class="apikey" v-model="apiKey" placeholder="Your Api Key Here" />
        <el-divider />
        <h3>Example Request With Curl</h3>
        <el-input class="apiExample" readonly ref="apiExample" type="textarea" :autosize="{ minRows: 5, maxRows: 5 }"
            v-model="curlExample" @click="copyToClipboard" />
    </el-dialog>
</template>

<script lang="ts">
import carousel_card_img from '../assets/carousel_card_img.webp';
import axios from 'axios';
import { ElNotification } from 'element-plus'
import { ref, reactive, getCurrentInstance } from 'vue';
import type { FormInstance, FormRules } from 'element-plus'

export default {
    name: 'SharedResults',
    setup() {
        const requestForm = reactive({
            email: '',
        })

        const requestFormRef = ref<FormInstance>()

        const requesterEmail = ref<string>('')

        let apiKey = ref<string>('')

        const rules = reactive<FormRules>({
            email: [
                {
                    required: true,
                    type: 'email',
                    message: 'Please input your email',
                    trigger: 'change, blur'
                }
            ],
        })

        const resetForm = (formEl: FormInstance | undefined) => {
            if (!formEl) return
            formEl.resetFields()
        }

        const submitForm = async (formEl: FormInstance | undefined) => {
            if (!formEl) return
            await formEl.validate((valid, fields) => {
                if (valid) {
                    axios.get('/generateAPIKey', { params: { name: requestForm.email } })
                        .then((response) => {
                            apiKey.value = response.data;
                            ElNotification({
                                title: 'Success',
                                message: `Click To Save Your API Key: ${apiKey.value}`,
                                type: 'success',
                                duration: 10000,
                                customClass: 'api-notification',
                                onClick: () => {
                                    navigator.clipboard.writeText(apiKey.value);
                                }
                            })
                            resetForm(formEl)
                        })
                        .catch((error) => {
                            console.log(error);
                        });
                } else {
                    console.log('error submit!', fields)
                }
            })
        }

        return {
            requestForm,
            requestFormRef,
            rules,
            submitForm,
            requesterEmail,
            apiKey,
            resetForm,
        }
    },
    data() {
        return {
            surveys: [] as Array<Survey>,
            carousel_img: carousel_card_img,
            dialogVisible: false,
            form_id: 0,
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
    computed: {
        curlExample() {
            return "curl -L -X GET 'https://survey-advisor.cloud/api/v1/getResultsForSharedForm?form_id=" + this.form_id + "' -H 'x-api-key:" + this.apiKey + "'";
        }
    },
    methods: {
        requestAccess(form_id: number) {
            this.dialogVisible = true;
            this.form_id = form_id;
        },
        copyToClipboard() {
            this.$refs.apiExample.select();
            document.execCommand('copy');
            ElNotification({
                title: 'Success',
                message: 'Request Copied to Clipboard',
                type: 'success',
            })
        },
    }
};

export interface Survey {
    form_id: number;
    title: string;
    description: string;
    submissions_count: number;
};
</script>

<style scoped>
.request-form {
    justify-content: center;
    align-items: center;
}

.apiExample:deep(.el-textarea__inner) {
    cursor: pointer;
}

.email {
    width: 300px;
}

.title {
    margin: 20px;
}

.subtitle1 {
    margin: 20px;
    color: #77797f;
}

.subtitle2 {
    margin: 0 20px 20px 20px;
    color: #77797f;
}

.subtitle3,
.subtitle4 {
    margin: 30px;
    text-align: center;
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