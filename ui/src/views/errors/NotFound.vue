<template>
    <ErrorCommon
        :code="'404'"
        :title="'Not Found'"
        :kaomoji="' ┐(￣～￣)┌'"
        :description="'This page is playing hide-and-seek and it\'s winning'"
    />
</template>

<script lang="ts">
import ErrorCommon from "@/layouts/errors/ErrorCommon.vue";

export default {
    components: {
        ErrorCommon,
    },
};
</script>
