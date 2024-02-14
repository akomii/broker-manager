<template>
    <Column field="externalId" :header="$t('externalId')" sortable>
        <template #body="slotProps">
            <span v-if="slotProps.data.externalId">
                {{ slotProps.data.externalId }}
            </span>
            <NotAvailableIcon v-else />
        </template>
    </Column>
</template>

<script lang="ts">
import Column from "primevue/column";
import NotAvailableIcon from "./NotAvailableIcon.vue";

export default {
    components: {
        Column,
        NotAvailableIcon,
    },
};
</script>
