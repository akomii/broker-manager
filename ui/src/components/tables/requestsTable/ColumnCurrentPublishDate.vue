<template>
    <Column :header="$t('dates.currentPublishDate')" sortable>
        <template #body="slotProps">
            <div
                v-if="
                    slotProps.data.currentExecution &&
                    slotProps.data.currentExecution.publishedDate !== null
                "
            >
                <DateView
                    :date="slotProps.data.currentExecution.publishedDate"
                />
            </div>
            <NotAvailableIcon v-else />
        </template>
    </Column>
</template>

<script lang="ts">
import Column from "primevue/column";
import DateView from "@/components/timeWidgets/DateView.vue";
import NotAvailableIcon from "@/components/icons/NotAvailableIcon.vue";

export default {
    components: {
        Column,
        DateView,
        NotAvailableIcon,
    },
};
</script>
