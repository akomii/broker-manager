<template>
    <Column field="clientDN.L" :header="$t('location')" sortable />
</template>

<script lang="ts">
import Column from "primevue/column";

// TODO refactor and add docs
export default {
    components: {
        Column,
    },
};
</script>
