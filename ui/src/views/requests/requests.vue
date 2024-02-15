<template>
    <RequestTable :requests="allManagerRequests" />
</template>

<script lang="ts">
import { TestDataService } from "@/services/TestDataService";
import { ManagerRequest } from "@/utils/Types";
import RequestTable from "@/components/tables/requests/RequestTable.vue";

export default {
    components: {
        RequestTable,
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
