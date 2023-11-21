<template>
    <div class="field grid m-0">
        <label :for="id" class="col-fixed w-3rem">
            <i v-if="icon" :class="iconClass" class="text-2xl"></i>
            <strong v-if="label && !icon">{{ label }}</strong>
        </label>
        <div class="col" :id="id">
            {{ value }}
        </div>
    </div>
</template>

<script lang="ts">
export default {
    props: {
        label: String,
        icon: String,
        value: String,
    },
    computed: {
        id() {
            return this.label.toLowerCase();
        },
        iconClass() {
            return `${this.icon}`;
        },
    },
};
</script>
