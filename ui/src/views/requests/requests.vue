<template>
    <div class="card">
        <DataTable :value="products" tableStyle="min-width: 50rem">
            <Column field="id" header="ID"></Column>
            <Column field="query.principal.name" header="Antragssteller"></Column>
            <Column field="query.title" header="Titel"></Column>
            <Column field="state" header="Status">
                <template #body="slotProps">
                    <RequestState :state="slotProps.data.state" />
                </template>
            </Column>
            <Column field="allowedOrgsToDownloadResults" header="Auswertestelle(n)"></Column>
            <Column field="" header="Aktuelles Veröffentlichungsdatum"></Column>
            <Column field="" header="Aktuelles Schließdatum"></Column>
        </DataTable>
    </div>
</template>

<script lang="ts">
import { TestDataService } from '@/service/TestDataService';

import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import ColumnGroup from 'primevue/columngroup';
import Row from 'primevue/row';
import RequestState from '@/components/RequestState.vue';

export default {
    components: {
        DataTable,
        Column,
        ColumnGroup,
        Row,
        RequestState
    },
    data() {
        return {
            products: null
        };
    },
    mounted() {
        TestDataService.getRequests().then((data) => (this.products = data));
    }
};
</script>
  
<style scoped></style>
  