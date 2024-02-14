<template>
    <div class="flex flex-wrap justify-content-between">
        <span>
            {{ executionsCountMessage }}
        </span>
        <ExportTableButton class="mt-3" :dt="refDataTable" />
    </div>
</template>

<script lang="ts">
import ExportTableButton from "@/components/common/ExportTableButton.vue";
import { RequestExecution } from "@/utils/Types";

export default {
    components: {
        ExportTableButton,
    },
    props: {
        executions: {
            type: Array as () => RequestExecution[],
            required: true,
        },
        refDataTable: {
            type: Object,
            required: true,
        },
    },
    computed: {
        executionsCountMessage() {
            const count = this.executions.length;
            return count === 1
                ? this.$t("oneExecution")
                : this.$t("xExecutions", { numExecutions: count });
        },
    },
};
</script>
