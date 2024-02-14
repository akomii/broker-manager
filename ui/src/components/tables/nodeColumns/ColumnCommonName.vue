<template>
    <Column field="clientDN.CN" :header="$t('node')" sortable />
</template>

<script lang="ts">
import Column from "primevue/column";

export default {
    components: {
        Column,
    },
};
</script>
