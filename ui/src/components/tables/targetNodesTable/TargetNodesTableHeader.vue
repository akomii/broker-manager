<template>
    <TableHeaderCommon @search="$emit('search', $event)">
        <template #title>
            <span
                v-if="showProcessingStateInfo"
                class="font-semibold text-primary text-xl m-0"
            >
                {{
                    $t("nodeAcceptanceOfExecution", {
                        executionId: sequenceId,
                    })
                }}
                :
                {{
                    $t("XofY", {
                        x: completedNodeStatusCount,
                        y: targetNodesLength,
                    })
                }}
            </span>
        </template>
    </TableHeaderCommon>
</template>

<script lang="ts">
import TableHeaderCommon from "@/components/tables/TableHeaderCommon.vue";

export default {
    components: {
        TableHeaderCommon,
    },
    props: {
        targetNodesLength: Number,
        completedNodeStatusCount: Number,
        sequenceId: Number,
        showProcessingStateInfo: {
            type: Boolean,
            default: false,
        },
    },
};
</script>
