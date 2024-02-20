<template>
    <span class="flex justify-content-center text-700 text-7xl font-bold mt-4">
        {{ code }}
    </span>
    <span class="flex justify-content-center text-700 text-xl font-bold">
        {{ title }}
    </span>
    <span class="flex justify-content-center text-700 text-lg mt-5 mb-3">
        {{ kaomoji }}
    </span>
    <span class="flex justify-content-center text-700 text-lg mb-5">
        {{ description }}
    </span>
</template>

<script lang="ts">
export default {
    props: {
        code: {
            type: String,
            required: true,
        },
        title: {
            type: String,
            required: true,
        },
        kaomoji: {
            type: String,
            required: true,
        },
        description: {
            type: String,
            required: true,
        },
    },
};
</script>
