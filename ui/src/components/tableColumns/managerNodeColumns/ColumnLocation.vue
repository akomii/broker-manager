<template>
    <Column field="clientDN.L" :header="$t('location')" sortable />
</template>

<script lang="ts">
import Column from "primevue/column";

export default {
    components: {
        Column,
    },
};
</script>
