<template>
    <Column field="title" :header="$t('title')" sortable>
        <template #body="slotProps">
            {{ slotProps.data.query.title }}
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
