<template>
    <Column :header="$t('user')" field="user" sortable />
</template>

<script lang="ts">
import Column from "primevue/column";

export default {
    components: {
        Column,
    },
};
</script>
