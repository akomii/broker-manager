<template>
    <Column field="creator" :header="$t('creator')" sortable />
</template>

<script lang="ts">
import Column from "primevue/column";

export default {
    components: {
        Column,
    },
};
</script>
