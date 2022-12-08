<template>
    <h3 class="user-title">Your Profile</h3>
    <el-divider />
    <h4 :style="{ margin: '20px' }">Update User Information</h4>
    <h5 class="subtitle1">You can change your information below</h5>
    <h5 class="subtitle2">Emails cannot be changed</h5>
    <el-form scroll-to-error ref="userFormRef" :model="userForm" :rules="rules" label-width="120px" class="user-form"
        status-icon>
        <el-form-item label="Firstname" prop="firstname">
            <el-input class="userInfo" v-model="userForm.firstname" />
        </el-form-item>
        <el-form-item label="Lastname" prop="lastname">
            <el-input class="userInfo" v-model="userForm.lastname" />
        </el-form-item>
        <el-form-item label="Email" prop="email">
            <el-input readonly class="userInfo" v-model="userForm.email" type="email" />
        </el-form-item>
        <el-form-item label="Phone" prop="phone">
            <el-input class="userInfo" v-model="userForm.phone" />
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click="submitForm(userFormRef)">Update</el-button>
            <el-button @click="resetForm(userFormRef)">Cancel</el-button>
        </el-form-item>
    </el-form>
    <h4 :style="{ margin: '40px 0 0 20px' }">Change Password</h4>
    <h5 class="subtitle1">If you forgot your password or need to change it, fill out the form below</h5>
    <el-form scroll-to-error ref="passwordFormRef" :model="passwordForm" :rules="passwordRules" label-width="150px" class="password-form"
        status-icon>
        <el-form-item label="Old Password" prop="oldPassword">
            <el-input class="userInfo" v-model="passwordForm.oldPassword" type="password" show-password/>
        </el-form-item>
        <el-form-item label="New Password" prop="newPassword">
            <el-input class="userInfo" v-model="passwordForm.newPassword" type="password" show-password/>
        </el-form-item>
        <el-form-item label="Confirm Password" prop="confirmPassword">
            <el-input class="userInfo" v-model="passwordForm.confirmPassword" type="password" show-password/>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click="submitForm(passwordFormRef)">Update</el-button>
            <el-button @click="resetForm(passwordFormRef)">Cancel</el-button>
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
        const passwordFormRef = ref<FormInstance>()
        const userForm = reactive({
            firstname: '',
            lastname: '',
            email: '',
            phone: '',
        })

        const passwordForm = reactive({
            oldPassword: '',
            newPassword: '',
            confirmPassword: '',
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
            phone: [
                { required: true, message: 'Please input your phone number', trigger: 'blur' },
            ],
        })

        const validatePass2 = (rule: any, value: any, callback: any) => {
            if (value === '') {
                callback(new Error('Please input the password again'))
            } else if (value !== ruleForm.pass) {
                callback(new Error("Two inputs don't match!"))
            } else {
                callback()
            }
        }

        const validateConfirmPass = (rule: any, value: any, callback: any) => {
            if (value === '') {
                callback(new Error('Please input the password'))
            } else {
                if (passwordForm.confirmPassword !== '') {
                    if (!passwordFormRef.value) return
                    passwordFormRef.value.validateField('confirmPassword', () => null)
                }
                callback()
            }
        }

        const passwordRules = reactive<FormRules>({
            oldPassword: [
                { 
                    required: true,
                    message: 'Please input your old password', 
                    trigger: 'blur' },
            ],
            newPassword: [
                { required: true, message: 'Please input your new password', trigger: 'blur' },
            ],
            confirmPassword: [
                { required: true, message: 'Please input your confirm password', trigger: 'blur' },
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
            passwordForm,
            passwordRules,
            passwordFormRef,
        }
    },
    data() {
        return {
        }
    },
    methods:{
        forgotPassword(){
            Auth.changePassword(Auth.currentAuthenticatedUser(), 'oldPassword', 'newPassword')
        }
    }
}

</script>

<style scoped>
.user-title {
    margin: 30px 0 0 30px;
}

.user-form, .password-form {
    width: 50%;
}

.userInfo {
    border: 1px solid #ebeef5;
}

.subtitle1 {
    margin: 20px;
    color: #77797f;
}

.subtitle2 {
    margin: 20px;
    color: rgba(255, 0, 0, 0.707);
}
</style>