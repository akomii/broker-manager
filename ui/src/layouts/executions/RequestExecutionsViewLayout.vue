<template>
    <Fieldset :legend="$t('executions')">
        <RequestExecutionsTable
            :executions="executions"
            :anchoredSequenceIdRef="anchoredSequenceIdRef"
        />
    </Fieldset>
</template>

<script lang="ts">
import Fieldset from "primevue/fieldset";
import RequestExecutionsTable from "@/components/tables/requestExecutionsTable/RequestExecutionsTable.vue";
import { RequestExecution } from "@/utils/Types";

// TODO refactor and add docs
export default {
    components: {
        Fieldset,
        RequestExecutionsTable,
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
