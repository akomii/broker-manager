<template>
    {{ requests }}
</template>

<script lang="ts">
import DataTable from "primevue/datatable";
import Column from "primevue/column";
import { ManagerRequest } from "@/utils/Types";

export default {
    components: {
        DataTable,
        Column,
    },
    props: {
        requests: {
            type: Array as () => ManagerRequest[],
            required: true,
        },
    },
};
</script>
