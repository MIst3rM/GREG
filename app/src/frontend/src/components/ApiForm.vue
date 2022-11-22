<template>
    <el-row class="form-container">
        <el-col :span="12">
            <el-form label-position="top" ref="ruleFormRef" :model="ruleForm" :rules="rules" size="large" status-icon>
                <el-form-item label="Api Function Name" prop="func_name">
                    <el-input class="api_form_input" v-model="ruleForm.func_name" />
                </el-form-item>
                <el-form-item label="Api Handler Name" prop="handler_name">
                    <el-input class="api_form_input" v-model="ruleForm.handler_name" />
                </el-form-item>
                <el-form-item label="Zip File Of Api Function" prop="funczip">
                    <el-upload :auto-upload="false" :show-file-list="false" accept=".zip" action="#"
                        :on-change="handleUpload" drag v-model:file-list="ruleForm.funczip">
                        <el-icon class="el-icon--upload">
                            <upload-filled />
                        </el-icon>
                        <div class="el-upload__text">
                            Drop file here or <em>click to upload</em>
                        </div>
                        <template #tip>
                            <div class="el-upload__tip">
                                zip file of python code only
                                <br />
                                zip files with a size less than 500kb
                            </div>
                        </template>
                    </el-upload>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="submitForm(ruleFormRef)">Create</el-button>
                    <el-button @click="resetForm(ruleFormRef)">Reset</el-button>
                </el-form-item>
            </el-form>
        </el-col>
        <el-col :span="12">
            <div class="image__placeholder">
                <div class="block">
                    <span class="code-alt">Code Preview</span>
                    <el-image :src="ruleForm.src" />
                </div>
            </div>
        </el-col>
    </el-row>

</template>

<script lang="ts" setup>
import { reactive, ref } from 'vue'
import type { FormInstance, FormRules, UploadProps } from 'element-plus'
import axios from "axios";
import { Auth } from 'aws-amplify';

const ruleFormRef = ref<FormInstance>()

const fileList = ref<File[]>([])

let code = ref([])

const ruleForm = reactive({
    func_name: '',
    handler_name: '',
    funczip: fileList,
    src: '',
})

const rules = reactive<FormRules>({
    func_name: [
        { required: true, message: 'A function name is required', trigger: 'blur' },
    ],
    handler_name: [
        {
            required: true,
            message: 'A handler name is required',
            trigger: 'blur',
        },
    ],
    funczip: [
        {
            required: true,
            message: 'A zip file is required',
            trigger: 'change',
        },
    ],
})

const handleUpload: UploadProps['beforeUpload'] = async (file: File) => {
    const f = new File([file.raw], file.name);
    const reader = new FileReader();
    reader.readAsArrayBuffer(f);
    reader.onloadend = (e) => {
        if (e.target.readyState == FileReader.DONE) {
            const arrayBuffer = e.target.result;
            const array = new Uint8Array(arrayBuffer as ArrayBuffer);
            for (let b of array) {
                code.value.push(b);
            }
        }
    }
}

const submitForm = async (formEl: FormInstance | undefined) => {
    if (!formEl) return
    await formEl.validate((valid, fields) => {
        if (valid) {
            Auth.currentSession().then((session) => {
                axios.defaults.headers.common['Authorization'] = `Bearer ${session.getAccessToken().getJwtToken()}`;
            });
            axios
                .post('/createLambdaFunction', {
                    funcName: ruleForm.func_name,
                    funcCode: code.value,
                    funcHandler: ruleForm.handler_name
                })
                .then((response) => {
                    console.log(response);
                }, (error) => {
                    console.log(error);
                });

        } else {
            console.log('error submit!', fields)
        }
    })
}

const resetForm = (formEl: FormInstance | undefined) => {
    if (!formEl) return
    formEl.resetFields()
}
</script>

<style scoped>
.api_form_input {
    width: 500px;
    border: 1px solid #dcdfe6;
    border-radius: var(--el-input-border-radius, var(--el-border-radius-base));
}

.el-form-item {
    margin-bottom: 30px;
}

.image__placeholder {
    height: 100%;
    width: 100%;
}

.image__placeholder .block {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 100%;
    width: 100%;
    color: var(--el-text-color-secondary);
    font-size: 14px;
    box-shadow: #dcdfe6 5px 3px 10px 10px;
    border-radius: 5px;
}

.image__placeholder .el-image {
    padding: 0 5px;
    max-width: 300px;
    max-height: 200px;
}
</style>