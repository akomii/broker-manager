<template>
    <Column field="requestId" :header="$t('id')" sortable>
        <template #body="slotProps">
            {{ slotProps.data.id }}
        </template>
    </Column>
</template>

<script lang="ts">
import Column from "primevue/column";

export default {
    components: {
        Column,
    },
};
</script>
