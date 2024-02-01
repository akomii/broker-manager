<template>
    <Button
        icon="pi pi-prime"
        :label="$t('convertRequest')"
        :disabled="disabled"
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
        disabled: {
            type: Boolean,
            default: false,
        },
    },
};
</script>
