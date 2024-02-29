<template>
    <Button
        icon="pi pi-chevron-left text-xl"
        outlined
        v-tooltip.bottom="$t('back')"
        @click="goBack"
    />
</template>

<script lang="ts">
import Button from "primevue/button";

/**
 * A back navigation button component.
 * Uses PrimeVue's Button component to render a button that navigates the user
 * to the parent route when clicked. This is achieved by removing the last
 * segment of the current path and navigating to the modified path.
 */
export default {
    components: {
        Button,
    },
    methods: {
        goBack() {
            const parentPath = this.$route.path.replace(/\/[^\/]*$/, "");
            this.$router.push(parentPath);
        },
    },
};
</script>
