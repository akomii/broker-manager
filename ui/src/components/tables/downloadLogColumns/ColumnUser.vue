<template>
    <Column field="user" :header="$t('user')" sortable />
</template>

<script lang="ts">
import Column from "primevue/column";

export default {
    components: {
        Column,
    },
};
</script>
