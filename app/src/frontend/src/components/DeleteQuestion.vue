<template>
    <el-row class="buttons-container">
        <el-col class="button-col" :span="1">
            <span v-if="!required" class="material-icons-outlined not_required" v-on:click="required = true">
                toggle_off
            </span>
            <span v-else class="material-icons-outlined required" v-on:click="required = false">
                toggle_on
            </span>
        </el-col>
        <p>required</p>
        <el-col class="button-col" :span="1">
            <span class="material-icons-outlined trash" v-on:click="$emit('deleteQuestion', num - 1)">
                delete_forever
            </span>
        </el-col>
        <p>delete</p>
    </el-row>
</template>

<script lang="ts">
export default {
    name: 'DeleteQuestion',
    emits: ['deleteQuestion'],
    data() {
        return {
            required: false
        }
    },
    props: {
        num: {
            type: Number,
            required: true,
            default: 0
        },
    },
}
</script>

<style scoped>
p {
    display: unset;
    margin-block-start: unset;
    margin-block-end: unset;
    margin-inline-start: unset;
    margin-inline-end: unset;
    margin-right: 10px;
}

.not_required {
    color: #4a4a4a;
}

.required {
    color: #13ce66;
}

.buttons-container {
    justify-content: flex-end;
    font-size: 12px;
    /* align-items: center; */
}

.trash {
    font-size: 25px;
    color: #F44336;
    cursor: pointer;
}

.trash:hover {
    transition: 0.5s ease-in-out;
    scale: 1.2;
}
</style>