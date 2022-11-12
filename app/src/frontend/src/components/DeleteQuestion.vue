<template>
    <el-row class="buttons-container">
        <el-col :span="1">
            <span class="material-icons-outlined trash" v-on:click="$emit('deleteQuestion', num - 1)">
                delete_forever
            </span>
        </el-col>
    </el-row>
</template>

<script lang="ts">
export default {
    name: 'DeleteQuestion',
    emits: ['deleteQuestion'],
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
.buttons-container {
    justify-content: flex-end;
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