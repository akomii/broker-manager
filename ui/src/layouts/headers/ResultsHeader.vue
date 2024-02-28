<template>
    <HeaderCommon :title="$t('resultsForRequest', { id: id, title: title })" />
</template>

<script lang="ts">
import HeaderCommon from "./HeaderCommon.vue";

/**
 * A Vue component that utilizes a common header component to display a dynamic
 * title, specifically formatted for displaying results related to a request.
 */
export default {
    components: {
        HeaderCommon,
    },
    mixins: [HeaderCommon],
};
</script>
