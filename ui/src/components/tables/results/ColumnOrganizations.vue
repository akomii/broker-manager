<template>
    <Column :header="$t('organizations')" field="userOrgs" sortable>
        <template #body="slotProps">
            <SimpleChipList :chips="slotProps.data.userOrgs" />
        </template>
    </Column>
</template>

<script lang="ts">
import Column from "primevue/column";
import SimpleChipList from "@/components/tags/SimpleChipList.vue";

export default {
    components: {
        Column,
        SimpleChipList,
    },
};
</script>
