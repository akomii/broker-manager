<template>
    <Chip v-for="chip in chips" :key="chip" class="py-1 px-2 m-1">
        <span class="text-sm"> {{ chip }} </span>
    </Chip>
</template>

<script lang="ts">
import Chip from "primevue/chip";

export default {
    components: {
        Chip,
    },
    props: {
        chips: {
            type: Array<string>,
            required: true,
        },
    },
};
</script>
