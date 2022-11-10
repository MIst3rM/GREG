<template>
    <div class="name-container prevent-select">
        <el-row>
            <el-col class="name" :span="24">
                <div class="S1" id="letter-object">S</div>
                <div class="U" id="letter-object">U</div>
                <div class="R1" id="letter-object">R</div>
                <div class="V1" id="letter-object">V</div>
                <div class="E" id="letter-object">E</div>
                <div class="Y" id="letter-object">Y</div>
            </el-col>
            <el-col class="name" :span="24">
                <div class="A" id="letter-object">A</div>
                <div class="D" id="letter-object">D</div>
                <div class="V2" id="letter-object">V</div>
                <div class="I" id="letter-object">I</div>
                <div class="S2" id="letter-object">S</div>
                <div class="O" id="letter-object">O</div>
                <div class="R2" id="letter-object">R</div>
            </el-col>
        </el-row>
    </div>
</template> 

<script lang="ts">
export default {
    name: 'Home',
    components: {
    },
    data() {
        return {
        }
    },
}
</script>

<style scoped>
.name-container {
    display: flex;
    align-content: center;
    justify-content: center;
    align-items: center;
    padding: 150px;
}

.prevent-select {
    -webkit-user-select: none;
    /* Safari */
    -ms-user-select: none;
    /* IE 10 and IE 11 */
    user-select: none;
    /* Standard syntax */
}

.name {
    display: flex;
    justify-content: center;
    align-items: center;
}

.S1 {
    -webkit-animation: text-pop-up-top .3s cubic-bezier(0.390, 0.575, 0.565, 1.000) both;
    animation: text-pop-up-top .3s cubic-bezier(0.390, 0.575, 0.565, 1.000) both
}

.U {
    -webkit-animation: text-pop-up-top .3s cubic-bezier(0.390, 0.575, 0.565, 1.000) 100ms both;
    animation: text-pop-up-top .3s cubic-bezier(0.390, 0.575, 0.565, 1.000) 100ms both
}

.R1 {
    -webkit-animation: text-pop-up-top .3s cubic-bezier(0.390, 0.575, 0.565, 1.000) 200ms both;
    animation: text-pop-up-top .3s cubic-bezier(0.390, 0.575, 0.565, 1.000) 200ms both
}

.V1 {
    -webkit-animation: text-pop-up-top .3s cubic-bezier(0.390, 0.575, 0.565, 1.000) 300ms both;
    animation: text-pop-up-top .3s cubic-bezier(0.390, 0.575, 0.565, 1.000) 300ms both
}

.E {
    -webkit-animation: text-pop-up-top .3s cubic-bezier(0.390, 0.575, 0.565, 1.000) 400ms both;
    animation: text-pop-up-top .3s cubic-bezier(0.390, 0.575, 0.565, 1.000) 400ms both
}

.Y {
    -webkit-animation: text-pop-up-top .3s cubic-bezier(0.390, 0.575, 0.565, 1.000) 500ms both;
    animation: text-pop-up-top .3s cubic-bezier(0.390, 0.575, 0.565, 1.000) 500ms both
}

.A {
    -webkit-animation: text-pop-up-top .3s cubic-bezier(0.390, 0.575, 0.565, 1.000) 600ms both;
    animation: text-pop-up-top .3s cubic-bezier(0.390, 0.575, 0.565, 1.000) 600ms both
}

.D {
    -webkit-animation: text-pop-up-top .3s cubic-bezier(0.390, 0.575, 0.565, 1.000) 700ms both;
    animation: text-pop-up-top .3s cubic-bezier(0.390, 0.575, 0.565, 1.000) 700ms both
}

.V2 {
    -webkit-animation: text-pop-up-top .3s cubic-bezier(0.390, 0.575, 0.565, 1.000) 800ms both;
    animation: text-pop-up-top .3s cubic-bezier(0.390, 0.575, 0.565, 1.000) 800ms both
}

.I {
    -webkit-animation: text-pop-up-top .3s cubic-bezier(0.390, 0.575, 0.565, 1.000) 900ms both;
    animation: text-pop-up-top .3s cubic-bezier(0.390, 0.575, 0.565, 1.000) 900ms both
}

.S2 {
    -webkit-animation: text-pop-up-top .3s cubic-bezier(0.390, 0.575, 0.565, 1.000) 1000ms both;
    animation: text-pop-up-top .3s cubic-bezier(0.390, 0.575, 0.565, 1.000) 1000ms both
}

.O {
    -webkit-animation: text-pop-up-top .3s cubic-bezier(0.390, 0.575, 0.565, 1.000) 1100ms both;
    animation: text-pop-up-top .3s cubic-bezier(0.390, 0.575, 0.565, 1.000) 1100ms both
}

.R2 {
    -webkit-animation: text-pop-up-top .3s cubic-bezier(0.390, 0.575, 0.565, 1.000) 1200ms both;
    animation: text-pop-up-top .3s cubic-bezier(0.390, 0.575, 0.565, 1.000) 1200ms both
}

@-webkit-keyframes text-pop-up-top {
    0% {
        -webkit-transform: translateY(0);
        transform: translateY(0);
        -webkit-transform-origin: 50% 50%;
        transform-origin: 50% 50%;
        text-shadow: none
    }

    100% {
        -webkit-transform: translateY(-50px);
        transform: translateY(-50px);
        -webkit-transform-origin: 50% 50%;
        transform-origin: 50% 50%;
        text-shadow: 0 1px 0 #ccc, 0 2px 0 #ccc, 0 3px 0 #ccc, 0 4px 0 #ccc, 0 5px 0 #ccc, 0 6px 0 #ccc, 0 7px 0 #ccc, 0 8px 0 #ccc, 0 9px 0 #ccc, 0 50px 30px rgba(0, 0, 0, .3)
    }
}

@keyframes text-pop-up-top {
    0% {
        -webkit-transform: translateY(0);
        transform: translateY(0);
        -webkit-transform-origin: 50% 50%;
        transform-origin: 50% 50%;
        text-shadow: none
    }

    100% {
        -webkit-transform: translateY(-50px);
        transform: translateY(-50px);
        -webkit-transform-origin: 50% 50%;
        transform-origin: 50% 50%;
        text-shadow: 0 1px 0 #ccc, 0 2px 0 #ccc, 0 3px 0 #ccc, 0 4px 0 #ccc, 0 5px 0 #ccc, 0 6px 0 #ccc, 0 7px 0 #ccc, 0 8px 0 #ccc, 0 9px 0 #ccc, 0 50px 30px rgba(0, 0, 0, .3)
    }
}

#letter-object {
    color: green;
    font: 200px/1 'TeXGyreAdventorBold';
    text-transform: none;
    text-align: center;
}
</style>