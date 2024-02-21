<template>
    <Fieldset :legend="$t('executions')">
        <ExecutionsTable
            :executions="executions"
            :anchoredSequenceIdRef="anchoredSequenceIdRef"
        />
    </Fieldset>
</template>

<script lang="ts">
import Fieldset from "primevue/fieldset";
import ExecutionsTable from "@/components/tables/executionsTable/ExecutionsTable.vue";
import { RequestExecution } from "@/utils/Types";

export default {
    components: {
        Fieldset,
        ExecutionsTable,
    },
    props: {
        executions: {
            type: Array as () => RequestExecution[],
            required: true,
        },
        anchoredSequenceIdRef: {
            type: Number,
            required: true,
        },
    },
};
</script>
