<template>
    <HeaderCommon :title="$t('resultsForRequest', { id: id, title: title })" />
</template>

<script lang="ts">
import HeaderCommon from "./HeaderCommon.vue";

/**
 * A Vue component that displays a header with a dynamically localized title. It
 * utilizes the HeaderCommon component for consistent header styling and
 * functionality across the application.
 */
export default {
    components: {
        HeaderCommon,
    },
    mixins: [HeaderCommon],
};
</script>
