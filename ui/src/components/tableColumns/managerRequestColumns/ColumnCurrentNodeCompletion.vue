<template>
    <Column :header="$t('currentAcceptance')" sortable>
        <template #body="slotProps">
            <div
                v-if="
                    getNodeCompletionOfMostCurrentExecution(
                        slotProps.data.executions
                    ) !== null
                "
            >
                {{
                    getNodeCompletionOfMostCurrentExecution(
                        slotProps.data.executions
                    )
                }}
            </div>
            <NotAvailableIcon v-else />
        </template>
    </Column>
</template>

<script lang="ts">
import Column from "primevue/column";
import NotAvailableIcon from "@/components/icons/NotAvailableIcon.vue";
import { RequestExecution } from "@/utils/Types";
import { ExecutionState } from "@/utils/Enums";

export default {
    components: {
        Column,
        NotAvailableIcon,
    },
    methods: {
        getNodeCompletionOfMostCurrentExecution(
            executions: RequestExecution[]
        ): string | null {
            const latestExecution = executions.reduce(
                (acc: RequestExecution | null, cur: RequestExecution) => {
                    if (
                        cur.executionState !== ExecutionState.PUBLISHED ||
                        cur.publishedDate === null
                    ) {
                        return acc;
                    }
                    const curDate = new Date(cur.publishedDate);
                    const accDate = acc
                        ? new Date(acc.publishedDate as Date)
                        : null;
                    return !acc || curDate > accDate ? cur : acc;
                },
                null
            );
            if (latestExecution && latestExecution.nodeStatusInfos) {
                const currentNodeCompletion =
                    latestExecution.nodeStatusInfos.filter(
                        (nodeInfo) => nodeInfo.completed !== null
                    ).length;
                return this.$t("XofY", {
                    x: currentNodeCompletion,
                    y: latestExecution.nodeStatusInfos.length,
                });
            }
            return null;
        },
    },
};
</script>
