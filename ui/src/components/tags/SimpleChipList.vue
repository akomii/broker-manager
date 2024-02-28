<template>
    <Chip v-for="chip in chips" :key="chip" class="py-1 px-2 m-1">
        <span class="text-sm"> {{ chip }} </span>
    </Chip>
</template>

<script lang="ts">
import Chip from "primevue/chip";

/**
 * A Vue component that renders a collection strings as chips, each displaying a
 * keyword.
 */
export default {
    components: {
        Chip,
    },
    props: {
        chips: {
            type: Array as () => string[],
            required: true,
        },
    },
};
</script>
