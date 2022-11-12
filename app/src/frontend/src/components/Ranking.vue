<template>
    <QuestionTitle :num="num" />
    <el-row class="rating-container">
        <ul class="list" :class="{ 'disabled': disabled }">
            <li :key="star" v-for="star in maxstars" @mouseover="hoverStar(star)" @mouseleave="mouseLeftStar"
                :class="[{ 'active': star <= stars }]" @click="rate(star)" class="star">
                <font-awesome-icon :class="`fa-${starsize}`" :icon="[star <= stars ? 'fas' : 'far', 'star']" />
            </li>
        </ul>
        <span class="description" v-if="hasdescription && star_desc" :class="star_desc.class">{{ star_desc.text
        }}</span>
        <span v-else-if="!star_desc" class="description">No Description</span>
    </el-row>
    <el-row>
        <el-tag v-for="tag in dynamicTags" :key="tag" class="mx-1" closable :disable-transitions="false"
            @close="handleClose(tag)">
            {{ tag }}
        </el-tag>
        <el-input v-if="inputVisible" ref="InputRef" v-model="inputValue" class="ml-1 w-20" size="small"
            @keyup.enter="handleInputConfirm" @blur="handleInputConfirm" />
        <el-button v-else class="button-new-tag ml-1" size="small" @click="showInput">
            + New Tag
        </el-button>
    </el-row>
    <DeleteQuestion :num="num" @deleteQuestion="onDeleteQuestion" />
</template>

<script lang="ts">
import QuestionTitle from './QuestionTitle.vue'
import DeleteQuestion from './DeleteQuestion.vue'
import { library } from "@fortawesome/fontawesome-svg-core";
import { fas } from "@fortawesome/free-solid-svg-icons";
import { far } from "@fortawesome/free-regular-svg-icons";
library.add(fas);
library.add(far);
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";

import { ref } from 'vue'
import { ElInput } from 'element-plus'

export default {
    name: 'Ranking',
    props: {
        num: {
            type: Number,
            required: true,
            default: 0
        },
    },
    data() {
        return {
            InputRef:ref<InstanceType<typeof ElInput>>(),
            dynamicTags: ['Tag 1', 'Tag 2', 'Tag 3'],
            inputValue: '',
            inputVisible: false,
            stars: this.star,
            star_desc: this.getRatingDesc(this.star),
            star: 0,
            maxstars: 5,
            hasdescription: true,
            ratingdescription: [
                {
                    text: "Poor",
                    class: "star-poor"
                },
                {
                    text: "Below Average",
                    class: "star-belowAverage"
                },
                {
                    text: "Average",
                    class: "star-average"
                },
                {
                    text: "Good",
                    class: "star-good"
                },
                {
                    text: "Excellent",
                    class: "star-excellent"
                }
            ],
            starsize: "1x",
            disabled: false,
        }
    },
    components: {
        QuestionTitle,
        DeleteQuestion,
        FontAwesomeIcon
    },
    methods: {
        handleClose(tag: string) {
            this.dynamicTags.splice(this.dynamicTags.indexOf(tag), 1)
        },
        showInput() {
            this.inputVisible = true
            this.$nextTick(() => {
                console.log(this.InputRef)
            })
        },
        handleInputConfirm() {
            if (this.inputValue) {
                this.dynamicTags.push(this.inputValue)
            }
            this.inputVisible = false
            this.inputValue = ''
        },
        onDeleteQuestion(idx: number) {
            this.$emit('deleteQuestion', idx)
        },
        rate(star: number) {
            if (this.disabled) {
                return;
            }
            if (star <= this.maxstars && star >= 0) {
                this.stars = this.stars === star ? star - 1 : star;
            }
        },
        hoverStar(star: number) {
            if (this.disabled) {
                return;
            }
            if (star <= this.maxstars && star >= 0) {
                this.star_desc = this.ratingdescription[star - 1];
            }
        },
        mouseLeftStar() {
            if (this.disabled) {
                return;
            }
            if (this.stars) {
                this.star_desc = this.ratingdescription[this.stars - 1];
                return this.star_desc;
            } else {
                this.star_desc = "";
            }
        },
        getRatingDesc(star: number) {
            if (star) {
                this.star_desc = this.ratingdescription[star - 1];
            }
            return this.star_desc;
        }
    }
}
</script>

<style lang="scss" scoped>
.rating-container {
    justify-content: center;
    align-items: center;
    margin-bottom: 10px;
}

$active-color: #f3d23e;
$white-color: #fff;
$poor-color: rgb(186, 186, 186);
$below-average-color: rgb(245, 195, 87);
$average-color: rgb(255, 172, 90);
$good-color: rgb(193, 215, 89);
$excellent-color: rgb(72, 150, 77);
$no-star-desc-color: rgb(00, 00, 00);

.description {
    width: 130px;
    padding: 5px;
    border-radius: 2px;
    font-size: 13px;
    text-align: center;
    font-weight: bold;
    transition: 0.2s;
    line-height: 25px;
}

ul.list li,
span {
    display: inline-block;
    margin: 2px;
}

.list {
    padding: 0;
    list-style-type: none;

    &:hover {
        .star {
            color: $active-color;
        }
    }
}

.list.disabled {
    &:hover {
        .star {
            color: black;
            cursor: default;
        }

        .star.active {
            color: $active-color;
        }
    }
}

.star {
    cursor: pointer;

    &:hover {
        &~.star {
            &:not(.active) {
                color: inherit;
            }
        }
    }
}

.active {
    color: $active-color;
}

.star-poor {
    background: $poor-color;
}

.star-belowAverage {
    background: $below-average-color;
}

.star-average {
    background: $active-color;
}

.star-good {
    background: $good-color;
}

.star-excellent {
    background: $excellent-color;
}
</style>