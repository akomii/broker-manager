<template>
    <Column bodyStyle="text-align: center">
        <template #body="slotProps">
            <ShowNodeDetailsButton :nodeId="slotProps.data.id" />
        </template>
    </Column>
</template>

<script lang="ts">
import Column from "primevue/column";
import ShowNodeDetailsButton from "@/components/buttons/ShowNodeDetailsButton.vue";

export default {
    components: {
        Column,
        ShowNodeDetailsButton,
    },
};
</script>
