<template>
    <QuestionTitle :saving="saving" :num="num" ref="questionTitle" />
    <el-row class="video-container" v-if="showVideo">
        <video class="video" ref="video" :src="video_url" controls controlsList="nodownload" :width="width"
            :height="height" />
        <el-col class="edit-container"><el-button type="primary" @click="changeVideo">Change Video</el-button></el-col>
        <el-col><span>Width Adjuster</span><el-slider v-model="width" :min="300" :max="600" /></el-col>
        <el-col><span>Height Adjuster</span><el-slider v-model="height" :min="300" :max="800" /></el-col>
    </el-row>
    <el-upload v-if="!showVideo" v-model:file-list="filelist" class="upload-container" ref="upload" drag show-file-list
        :auto-upload="false" :limit="1" :on-exceed="handleExceed" action="https://survey-advisor.cloud/api/v1/uploadVideo"
        :headers="headers" :http-request="request">
        <el-icon class="el-icon--upload">
            <upload-filled />
        </el-icon>
        <div class="el-upload__text">
            Drop file here or <em>click to upload</em>
        </div>
        <template #tip>
            <div class="el-upload__tip">
                mp4 files with a size less than 5mb
            </div>
        </template>
    </el-upload>
    <el-progress v-if="(showProgress && !showVideo)" class="videoUploadProgress" :percentage="uploadPercentage" />
    <el-button v-if="!showVideo" class="upload-button" type="primary" @click="submitUpload">
        Upload Video
    </el-button>
    <DeleteQuestion ref="deleteQuestion" :num="num" @deleteQuestion="onDeleteQuestion" />
</template>

<script lang="ts">
import draggable from 'vuedraggable'
import QuestionTitle from './QuestionTitle.vue'
import DeleteQuestion from './DeleteQuestion.vue'
import { useAppStore } from '../store/app';
import { UploadFilled } from '@element-plus/icons-vue'
import type { UploadInstance, UploadProps, UploadRawFile, UploadUserFile, UploadRequestHandler, UploadRequestOptions } from 'element-plus'
import { genFileId } from 'element-plus'
import { ref } from 'vue'
import { Auth } from 'aws-amplify';
import axios from 'axios';

export default {
    name: 'Video',
    emits: ['deleteQuestion', 'saveChanges'],
    components: {
        draggable,
        QuestionTitle,
        DeleteQuestion
    },
    props: {
        num: {
            type: Number,
            default: 0
        },
    },
    setup() {
        const appStore = useAppStore();
        const upload = ref<UploadInstance>()

        const headers = new Headers();

        const authHeader = async () => {
            const session = await Auth.currentSession()
            return session.getIdToken().getJwtToken()
        }

        const filelist = ref<UploadUserFile[]>([])

        authHeader().then((token) => {
            headers.append('Authorization', 'Bearer ' + token)
        })

        const handleExceed: UploadProps['onExceed'] = (files) => {
            upload.value!.clearFiles()
            const file = files[0] as UploadRawFile
            file.uid = genFileId()
            upload.value!.handleStart(file)
        }

        let uploadPercentage = ref(0)

        let showProgress = ref(false)

        let video_url = ref('')

        let showVideo = ref(false)

        let width = ref(500)

        let height = ref(300)

        const request = () => {
            const fd = new FormData();
            fd.append('file', filelist.value[0].raw as Blob);

            axios.post('/uploadVideo', fd, {
                headers: {
                    'Authorization': headers.get('Authorization') as string,
                    'Content-Type': 'multipart/form-data',
                    'Accept': 'application/json',
                },
                onUploadProgress: function (progressEvent) {
                    uploadPercentage.value = parseInt(Math.round((progressEvent.loaded * 100) / progressEvent.total));
                }.bind(this)
            }).then((response) => {
                showProgress.value = false
                video_url.value = response.data
                showVideo.value = true
                filelist.value = []
            }).catch((error) => {
                console.log(error)
            })
        }

        const submitUpload = () => {
            showProgress.value = true
            upload.value!.submit()
        }

        return {
            appStore,
            upload,
            handleExceed,
            submitUpload,
            headers,
            filelist,
            request,
            uploadPercentage,
            showProgress,
            video_url,
            showVideo,
            width,
            height,
        }
    },
    data() {
        return {
            saving: false,
        };
    },
    methods: {
        onDeleteQuestion(idx: number) {
            this.$emit('deleteQuestion', idx);
        },
        changeVideo() {
            this.showVideo = false
            this.video_url = ''
        },
        save() {
            let question = {
                "type": "panel",
                "name": "panel" + this.num,
                "elements": [
                    {
                        "type": "text",
                        "name": "question" + this.num,
                        "isRequired": this.$refs.deleteQuestion.required,
                        "title": this.$refs.questionTitle.question,
                    },
                    {
                        "type": "html",
                        "name": "video" + this.num,
                        "html": "<video width=\"" + this.width + "\" height=\"" + this.height + "\" controls controlsList='nodownload' autoplay>\n  <source src=\"" + this.video_url + "\" type=\"video/mp4\">\n</video>"
                    }
                ]
            }

            if (this.appStore.findElement(this.num - 1)) {
                this.appStore.updateElement(this.num - 1, question);
            } else {
                this.appStore.addQuestion("Page1", question);
            }

            this.saving = true;
            setTimeout(() => this.saving = false, 4000);
            this.$emit('saveChanges');
        }
    },
    computed: {
        dragOptions() {
            return {
                animation: 250,
                group: "people",
                disabled: false,
                ghostClass: "ghost"
            };
        },
    }
}
</script>

<style scoped>
.edit-container {
    display: inline-flex;
    justify-content: center;
}

.video-container {
    padding: 20px;
    justify-content: center;
}

.video {
    margin-bottom: 30px;
}

.videoUploadProgress {
    margin: 20px;
}

.videoUploadProgress:deep(.el-progress__text) {
    min-width: initial;
}

.upload-container {
    margin: 20px;
}

.upload-button {
    margin: 0 20px 0 20px;
}
</style>