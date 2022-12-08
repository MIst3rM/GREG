<template>
    <el-row class="buttons-container">
        <el-col class="button-col" :span="1">
            <span v-if="!required" class="material-icons-outlined not_required" v-on:click="required = true">
                toggle_off
            </span>
            <span v-else class="material-icons-outlined required" v-on:click="required = false">
                toggle_on
            </span>
            <p>required</p>
            <lord-icon class="trash" v-on:click="$emit('deleteQuestion', num - 1)"
                src="https://cdn.lordicon.com/kfzfxczd.json" trigger="hover" style="width:28px;height:28px">
            </lord-icon>
        </el-col>
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
.button-col {
    display: contents;
}

p {
    display: unset;
    margin-block-start: unset;
    margin-block-end: unset;
    margin-inline-start: unset;
    margin-inline-end: unset;
    margin: 0 5px 0 5px;
}

.not_required {
    color: #4a4a4a;
    font-size: 28px;
    cursor: pointer;
}

.required {
    color: #13ce66;
    font-size: 28px;
    cursor: pointer;
}

.buttons-container {
    justify-content: flex-end;
    font-size: 12px;
    align-items: center;
    padding: 5px;
}

.trash {
    font-size: 25px;
    cursor: pointer;
}
</style>