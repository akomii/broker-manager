<template>
    <ResultTable />
</template>

<script lang="ts">
import ResultTable from "@/components/results/ResultTable.vue";
import TestDataService from "@/services/TestDataService";

export default {
    components: {
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
