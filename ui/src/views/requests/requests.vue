<template>
    <RequestsTable :requests="allManagerRequests" />
</template>

<script lang="ts">
import { TestDataService } from "@/services/TestDataService";
import { ManagerRequest } from "@/utils/Types";
import RequestsTable from "@/components/tables/requestsTable/RequestsTable.vue";

export default {
    components: {
        RequestsTable,
    },
    data() {
        return {
            allManagerRequests: [] as ManagerRequest[],
        };
    },
    async mounted() {
        await this.fetchManagerRequests();
    },
    methods: {
        async fetchManagerRequests(): Promise<void> {
            this.allManagerRequests = await TestDataService.getRequests();
        },
    },
};
</script>
