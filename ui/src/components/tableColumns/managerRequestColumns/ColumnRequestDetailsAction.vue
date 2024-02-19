<template>
    <Column bodyStyle="text-align: center">
        <template #body="slotProps">
            <ShowRequestDetailsButton :requestId="slotProps.data.id"/>
        </template>
    </Column>
</template>

<script lang="ts">
import Column from "primevue/column";
import ShowRequestDetailsButton from "@/components/buttons/ShowRequestDetailsButton.vue";

export default {
    components: {
        Column,
        ShowRequestDetailsButton,
    },
};
</script>
