<template>
    <div v-if="request">
        <RequestResultsHeader :id="request.id" :title="request.query.title" />
        <RequestResultsTable :executions="request.executions" class="mx-3" />
    </div>
</template>

<script lang="ts">
import RequestResultsTable from "@/components/tables/resultsTable/RequestResultsTable.vue";
import RequestResultsHeader from "@/components/headers/RequestResultsHeader.vue";
import TestDataService from "@/services/TestDataService";

export default {
    components: {
        RequestResultsHeader,
        RequestResultsTable,
    },
    data() {
        return {
            request: null as Request | null,
        };
    },
    mounted() {
        const requestId = this.$route.params.id;
        TestDataService.getRequestById(requestId)
            .then((data: Request) => {
                this.request = data;
            })
            .catch((error) => {
                console.error("Error fetching request:", error);
            });
    },
};
</script>
