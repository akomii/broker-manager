<template>
    <NodesTable :nodes="allManagerNodes" class="mx-3" />
</template>

<script lang="ts">
import { TestDataService } from "@/services/TestDataService";
import { ManagerNode } from "@/utils/Types";
import NodesTable from "@/components/tables/nodesTable/NodesTable.vue";

export default {
    components: {
        NodesTable,
    },
    data() {
        return {
            allManagerNodes: [] as ManagerNode[],
        };
    },
    async mounted() {
        await this.fetchManagerNodes();
    },
    methods: {
        async fetchManagerNodes(): Promise<void> {
            this.allManagerNodes = await TestDataService.getNodes();
        },
    },
};
</script>
