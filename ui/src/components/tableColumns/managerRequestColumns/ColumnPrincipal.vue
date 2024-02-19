<template>
    <Column field="principal" :header="$t('principal')" sortable>
        <template #body="slotProps">
            {{ slotProps.data.query.principal.name }}
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
