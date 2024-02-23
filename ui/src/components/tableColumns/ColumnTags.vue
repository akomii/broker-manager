<template>
    <Column :header="$t('tags')" sortable>
        <template #body="slotProps">
            <EditableTagListView
                v-if="slotProps.data.tags.length"
                :tags="slotProps.data.tags"
            />
            <NotAvailableIcon v-else />
        </template>
    </Column>
</template>

<script lang="ts">
import Column from "primevue/column";
import EditableTagListView from "@/components/tags/EditableTagListView.vue";
import NotAvailableIcon from "@/components/icons/NotAvailableIcon.vue";

// TODO refactor and add docs
export default {
    components: {
        Column,
        EditableTagListView,
        NotAvailableIcon,
    },
};
</script>
