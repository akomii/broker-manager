<template>
    <Column :header="$t('tags')" sortable>
        <template #body="slotProps">
            <EditableTagListView :tags="slotProps.data.tags" />
        </template>
    </Column>
</template>

<script lang="ts">
import Column from "primevue/column";
import EditableTagListView from "@/components/tags/EditableTagListView.vue";

export default {
    components: {
        Column,
        EditableTagListView,
    },
};
</script>
