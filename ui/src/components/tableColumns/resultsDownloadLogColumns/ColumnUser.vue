<template>
    <Column field="user" :header="$t('user')" sortable />
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
