<template>
    <QuestionTitle :num="num" />
    <el-row class="image-picker">
        <draggable :list="fileList" tag="transition-group" :component-data="{
            tag: 'ul',
            class: 'parent-grid el-upload-list el-upload-list--picture-card',
            type: 'transition-group',
            name: 'fade',
        }" @start="drag = true" @end="drag = false" item-key="id">
            <template #item="{ element, index }">
                <li class="el-upload-list__item" :class="(element.name, element.status === 'success' ? 'is-success': '')" tabindex="0"
                    @click="selectImage(element)">
                    <img :src="element.url" class="el-upload-list__item-thumbnail" alt="" />
                    <label class="el-upload-list__item-status-label">
                        <el-icon class="el-icon--upload-success el-icon--check">
                            <Check />
                        </el-icon>
                    </label>
                    <span class="el-upload-list__item-actions">
                        <span class="el-upload-list__item-delete" @click="handleRemove(element, fileList)">
                            <el-icon>
                                <Delete />
                            </el-icon>
                        </span>
                    </span>
                </li>
            </template>
        </draggable>
        <el-col class="uploader-container">
            <ul class="uploader-grid">
                <li class="el-upload el-upload--picture-card" @click="$refs.fileInput.click()">
                    <div class="uploader">
                        <el-icon>
                            <Plus />
                        </el-icon>
                        <input class="el-upload__input" name="file" accept="image/png, image/jpeg" type="file"
                            ref="fileInput" />
                    </div>
                </li>
            </ul>
        </el-col>
    </el-row>
    <DeleteQuestion :num="num" @deleteQuestion="onDeleteQuestion" />
</template>

<script lang="ts" setup>
import { ref, defineProps, defineEmits } from 'vue'
import draggable from 'vuedraggable'
import QuestionTitle from './QuestionTitle.vue'
import DeleteQuestion from './DeleteQuestion.vue'
import { Check, Delete, Plus } from '@element-plus/icons-vue'

import type { UploadProps, UploadUserFile } from 'element-plus'

const fileList = ref<UploadUserFile[]>([
    {
        name: 'food0',
        url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100',
        status: 'ready',
    },
    {
        name: 'food1',
        url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100',
        status: 'ready',
    },
    {
        name: 'food2',
        url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100',
        status: 'ready',
    },
    {
        name: 'food3',
        url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100',
        status: 'ready',
    },
])

const drag = ref(false)

const props = defineProps({
    num: {
        type: Number,
        required: true,
    },
})

const emits = defineEmits(['deleteQuestion'])

const selectImage = (image: UploadUserFile) => {
    fileList.value.find((item) => item.name === image.name).status = 'success'
}


const handleRemove: UploadProps['onRemove'] = (uploadFile, uploadFiles) => {
    uploadFiles.splice(uploadFiles.indexOf(uploadFile), 1)
}

const onDeleteQuestion = (idx: number) => {
    emits('deleteQuestion', idx)
}
</script>

<style scoped>
.parent-grid {
    display: grid;
    grid-template-columns: repeat(2, minmax(200px, 1fr));
    grid-template-rows: repeat(2, minmax(200px, 1fr));
    grid-column-gap: 10px;
    grid-row-gap: 10px;
    padding: 20px;
    --el-upload-list-picture-card-size: unset;
}

.uploader-grid {
    display: grid;
    grid-template-columns: 1fr;
    grid-template-rows: 1fr;
    grid-column-gap: 0px;
    grid-row-gap: 0px;
}

.uploader-container {
    display: flex;
    justify-content: center;
    align-items: center;
}

img {
    width: 200px !important;
    height: 100% !important;
    object-fit: unset !important;
}

.image-picker {
    justify-content: center;
    margin: 20px 0;
}

.food0 {
    grid-area: 1 / 1 / 2 / 2;
}

.food1 {
    grid-area: 1 / 2 / 2 / 3;
}

.food2 {
    grid-area: 2 / 1 / 3 / 2;
}

.food3 {
    grid-area: 2 / 2 / 3 / 3;
}
</style>