<template>
    <Column :header="$t('currentAcceptance')">
        <template #body="slotProps">
            <div
                v-if="
                    getNodeCompletion(slotProps.data.currentExecution) !== null
                "
            >
                {{ getNodeCompletion(slotProps.data.currentExecution) }}
            </div>
            <NotAvailableIcon v-else />
        </template>
    </Column>
</template>

<script lang="ts">
import Column from "primevue/column";
import NotAvailableIcon from "@/components/icons/NotAvailableIcon.vue";
import { RequestExecution } from "@/utils/Types";

// TODO refactor and add docs
export default {
    components: {
        Column,
        NotAvailableIcon,
    },
    methods: {
        getNodeCompletion(execution: RequestExecution): string | null {
            if (execution && execution.nodeStatusInfos) {
                const nodeCompletion = execution.nodeStatusInfos.filter(
                    (nodeInfo) => nodeInfo.completed !== null
                ).length;
                return this.$t("XofY", {
                    x: nodeCompletion,
                    y: execution.nodeStatusInfos.length,
                });
            }
            return null;
        },
    },
};
</script>
