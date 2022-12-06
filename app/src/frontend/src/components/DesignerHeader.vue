<template>
    <el-row>
        <el-col :span="20" class="header-component">
            <el-input v-debounce:1000ms="setTitle" v-model="surveyName" class="surveyName" size="large"
                placeholder="Survey Title" />
        </el-col>
        <el-col :span="4">
            <el-popconfirm title="Are you sure you want to abandon your progress?" confirm-button-text="Yes, Toss It"
                cancel-button-text="Go Back" width="200px" @confirm="confirmEvent" @cancel="cancelEvent"
                icon="WarningFilled" icon-color="red">
                <template #reference>
                    <lord-icon class="survey-controls" src="https://cdn.lordicon.com/nhfyhmlt.json" trigger="hover" colors="primary:#121331"
                        state="hover-3" style="width:32px;height:32px;float:right">
                    </lord-icon>
                </template>
            </el-popconfirm>
            <el-popover placement="bottom" :width="200" trigger="click">
                <template #reference>
                    <lord-icon class="survey-controls" src="https://cdn.lordicon.com/hwuyodym.json" trigger="hover"
                        style="width:32px;height:32px;float:right">
                    </lord-icon>
                </template>
                <el-menu class="settings">
                    <el-menu-item index="1" @click="">Save Survey</el-menu-item>
                    <el-menu-item index="2" @click="">Publish Survey</el-menu-item>
                    <el-menu-item index="3" @click="">Preview Survey</el-menu-item>
                    <el-menu-item index="4" @click="">Export Survey</el-menu-item>
                </el-menu>
            </el-popover>
        </el-col>
    </el-row>
    <el-row>
        <el-col class="header-component" :span="14">
            <el-input v-debounce:1000ms="setDescription" v-model="surveyDesc" maxlength="25" show-word-limit type="text"
                class="surveyDesc" size="large" placeholder="description" />
        </el-col>
    </el-row>
    <el-divider class="divider" />

</template>

<script lang="ts">
import { useAppStore } from '../store/app';
export default {
    name: 'DesignerHeader',
    emits: ['reset'],
    setup() {
        const appStore = useAppStore();
        return {
            appStore
        }
    },
    data() {
        return {
            surveyName: "",
            surveyDesc: "",
        };
    },
    methods: {
        setTitle() {
            this.appStore.setTitle(this.surveyName);
        },
        setDescription() {
            this.appStore.setDescription(this.surveyDesc);
        },
        confirmEvent() {
            this.appStore.reset();
            this.$emit('reset');
        },
        cancelEvent() {

        }
    }
};
</script>

<style>
.header-component {
    margin-bottom: 10px;
}

.settings {
    border: none;
    display: flex;
    flex-direction: column;
    align-items: center;
}

.survey-controls{
    margin-right: 10px;
    cursor: pointer;
}
</style>