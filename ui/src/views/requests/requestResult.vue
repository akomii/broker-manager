<template>
    <div v-if="request">
        <RequestResultsHeader :id="request.id" :title="request.query.title" />
        <ResultsTable :executions="request.executions" class="mx-3" />
    </div>
</template>

<script lang="ts">
import ResultsTable from "@/components/tables/resultsTable/ResultsTable.vue";
import RequestResultsHeader from "@/layouts/headers/RequestResultsHeader.vue";
import TestDataService from "@/services/TestDataService";

export default {
    components: {
        RequestResultsHeader,
        ResultsTable,
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
