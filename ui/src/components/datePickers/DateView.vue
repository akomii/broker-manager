<template>
    <div>
        <p class="my-0">
            {{ label }}
        </p>
        <p class="my-0">
            {{ formattedDate }}
        </p>
    </div>
</template>

<script lang="ts">
import MomentWrapper from "@/utils/MomentWrapper.ts";

export default {
    props: {
        label: {
            type: String,
            required: true,
        },
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
