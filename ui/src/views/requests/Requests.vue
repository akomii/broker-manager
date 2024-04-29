<template>
    <RequestsTable :requests="allManagerRequests" class="mx-3" />
</template>

<script lang="ts">
import ManagerRequestService from "@/services/graphql/ManagerRequestService";
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
            try {
                this.allManagerRequests =
                    await ManagerRequestService.getRequests();
            } catch (error) {
                console.error(error);
                this.$router.push("/500");
            }
        },
    },
};
</script>


