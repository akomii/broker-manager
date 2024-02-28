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
 * A Vue component that renders a button with an icon indicating a "go back"
 * action. On click, it navigates the user to the parent path of the current
 * route.
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
