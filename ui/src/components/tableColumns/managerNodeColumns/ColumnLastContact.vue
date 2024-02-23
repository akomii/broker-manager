<template>
    <Column :header="$t('lastContact')" sortable>
        <template #body="slotProps">
            <DateView
                v-if="slotProps.data.lastContact"
                :date="slotProps.data.lastContact"
            />
            <NotAvailableIcon v-else />
        </template>
    </Column>
</template>

<script lang="ts">
import Column from "primevue/column";
import DateView from "@/components/timeWidgets/DateView.vue";
import NotAvailableIcon from "@/components/icons/NotAvailableIcon.vue";

// TODO refactor and add docs
export default {
    components: {
        Column,
        DateView,
        NotAvailableIcon,
    },
};
</script>
