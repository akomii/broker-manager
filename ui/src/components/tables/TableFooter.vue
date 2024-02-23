<template>
    <div class="flex flex-wrap justify-content-between">
        <span>
            <slot name="count-message" />
        </span>
        <ExportTableButton class="mt-3" :dt="refDataTable" />
    </div>
</template>

<script lang="ts">
import ExportTableButton from "@/components/buttons/ExportTableButton.vue";

// TODO refactor and add docs
export default {
    components: {
        ExportTableButton,
    },
    props: {
        refDataTable: {
            type: Object,
            required: true,
        },
    },
};
</script>
