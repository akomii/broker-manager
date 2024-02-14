<template>
    <Column field="downloadDate" :header="$t('dates.downloadDate')" sortable>
        <template #body="slotProps">
            <DateView :date="slotProps.data.date" />
        </template>
    </Column>
    <Column field="user" :header="$t('user')" sortable />
    <Column field="userOrgs" :header="$t('organizations')" sortable>
        <template #body="slotProps">
            <SimpleChipList :chips="slotProps.data.userOrgs" />
        </template>
    </Column>
    <Column field="hashValue" :header="$t('hash')" />
    <Column field="hashAlgorithm" :header="$t('algorithm')" />
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
