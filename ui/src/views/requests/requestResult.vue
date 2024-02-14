<template>
    <div v-if="request">
        <RequestResultHeader :id="request.id" :title="request.query.title" />
        <ResultTable :executions="request.executions" class="mx-3" />
    </div>
</template>

<script lang="ts">
import ResultTable from "@/components/tables/results/ResultTable.vue";
import RequestResultHeader from "@/components/headers/RequestResultHeader.vue";
import TestDataService from "@/services/TestDataService";

export default {
    components: {
        RequestResultHeader,
        ResultTable,
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
