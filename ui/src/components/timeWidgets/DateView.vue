<template>
    {{ formattedDate }}
</template>

<script lang="ts">
import MomentWrapper from "@/utils/MomentWrapper.ts";

// TODO refactor and add docs
export default {
    props: {
        date: {
            type: Date,
            required: true,
        },
    },
    computed: {
        formattedDate(): string {
            return MomentWrapper.formatDateToGermanLocale(this.date);
        },
    },
};
</script>
