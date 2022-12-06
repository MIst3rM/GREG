<template>
    <h3 class="user-title">Your Profile</h3>
    <el-divider />
    <h4 :style="{ margin: '20px' }">Update User Information</h4>
    <el-form scroll-to-error ref="userForm" :model="userForm" :rules="rules" label-width="120px" class="user-form"
        status-icon>
        <el-form-item label="Firstname" prop="firstname">
            <el-input class="userInfo" v-model="userForm.firstname" />
        </el-form-item>
        <el-form-item label="Lastname" prop="lastname">
            <el-input class="userInfo" v-model="userForm.lastname" />
        </el-form-item>
        <el-form-item label="Email" prop="email">
            <el-input class="userInfo" v-model="userForm.email" type="email" />
        </el-form-item>
        <el-form-item label="Phone" prop="phone">
            <el-input class="userInfo" v-model="userForm.phone" />
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click="submitForm(userFormRef)">Update</el-button>
            <el-button @click="resetForm(userFormRef)">Cancel</el-button>
        </el-form-item>
    </el-form>
</template>

<script lang="ts">
import { reactive, ref } from 'vue'
import { Auth } from 'aws-amplify';
import type { FormInstance, FormRules } from 'element-plus'

export default {
    name: 'User',
    setup() {
        const userFormRef = ref<FormInstance>()
        const userForm = reactive({
            firstname: '',
            lastname: '',
            email: '',
            phone: '',
        })

        const rules = reactive<FormRules>({
            firstname: [
                { required: true, message: 'Please input your firstname', trigger: 'blur' },
                { min: 3, max: 5, message: 'Length should be 3 to 5', trigger: 'blur' },
            ],
            lastname: [
                { required: true, message: 'Please input your lastname', trigger: 'blur' },
                { min: 3, max: 5, message: 'Length should be 3 to 5', trigger: 'blur' },
            ],
            email: [
                { required: true, message: 'Please input your email', trigger: 'blur' },
                { type: 'email', message: 'Please input correct email address', trigger: ['blur', 'change'] },
            ],
            phone: [
                { required: true, message: 'Please input your phone number', trigger: 'blur' },
            ],
        })
        const submitForm = async (formEl: FormInstance | undefined) => {
            if (!formEl) return
            await formEl.validate((valid, fields) => {
                if (valid) {
                    console.log('submit!')
                } else {
                    console.log('error submit!', fields)
                }
            })
        }

        const resetForm = (formEl: FormInstance | undefined) => {
            if (!formEl) return
            console.log('reset!')
            formEl.resetFields()
        }

        return {
            userFormRef,
            rules,
            submitForm,
            resetForm,
            userForm,
        }
    },
    data() {
        return {
        }
    },
}

</script>

<style scoped>
.user-title {
    margin: 30px 0 0 30px;
}

.user-form {
    width: 50%;
}

.userInfo {
    border: 1px solid #ebeef5;
}
</style>