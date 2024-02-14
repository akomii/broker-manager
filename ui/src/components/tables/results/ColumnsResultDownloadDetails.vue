<template>
    <Column field="downloadDate" :header="$t('dates.downloadDate')" sortable>
        <template #body="slotProps">
            <DateView :date="slotProps.data.date" />
        </template>
    </Column>
    <Column :header="$t('user')" field="user" sortable />
    <Column :header="$t('organizations')" field="userOrgs" sortable>
        <template #body="slotProps">
            <SimpleChipList :chips="slotProps.data.userOrgs" />
        </template>
    </Column>
    <Column :header="$t('hash')" field="hashValue" />
    <Column :header="$t('algorithm')" field="hashAlgorithm" />
</template>

<script lang="ts">
import Column from "primevue/column";
import DateView from "@/components/datePickers/DateView.vue";
import SimpleChipList from "@/components/tags/SimpleChipList.vue";

export default {
    components: {
        Column,
        DateView,
        SimpleChipList,
    },
};
</script>
