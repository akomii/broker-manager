<template>
    <Button
        icon="pi pi-download"
        :label="label"
        v-tooltip.bottom="tooltipLabel"
        :disabled="disabled"
        @click="action()"
        outlined
    />
</template>

<script lang="ts">
import Button from "primevue/button";

export default {
    components: {
        Button,
    },
    props: {
        label: String,
        tooltipLabel: String,
        disabled: {
            type: Boolean,
            default: false,
        },
        action: {
            type: Function,
            required: true,
        },
    },
};
</script>
