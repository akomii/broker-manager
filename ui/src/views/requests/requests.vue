<template>
    <div class="card">
        <DataTable v-model:filters="filters" :value="requests" dataKey="id" stripedRows tableStyle="min-width: 50rem"
            paginator :rows="10" :rowsPerPageOptions="[10, 20, 50]"
            paginatorTemplate="RowsPerPageDropdown FirstPageLink PrevPageLink CurrentPageReport NextPageLink LastPageLink"
            currentPageReportTemplate="{first} bis {last} von {totalRecords}" removableSort sortField="id" :sortOrder="-1"
            :globalFilterFields="['id', 'query.title', 'state', 'allowedOrganisations', 'seriesClosingDate']">

            <!-- TODO spinning loader -->
            <!-- TODO lazy load -->
            <!-- TODO store selected Columns in Cookie-->

            <!-- TODO Column Toggle -->
            <!-- TODO FIX COLUMN ELEMENTS -->
            <!-- TODO PATH BREADCRUMBS -->

            <template #header>
                <Toolbar>
                    <template #start>

                        <div class="column-toggle">
                            <Button type="button" icon="pi pi-cog" @click="toggle" />

                            <OverlayPanel ref="opanel" appendTo="body" showCloseIcon>
                                <div class="card flex flex-wrap justify-content-center gap-3">
                                    <div class="flex align-items-center">
                                        <Checkbox v-model="allSelected" inputId="selectAll" />
                                        <label for="selectAll" class="ml-2">Select All</label>
                                    </div>
                                    <div class="flex align-items-center" v-for="(column, index) in columns" :key="index">
                                        <Checkbox :value="column" v-model="selectedColumnsComputed"
                                            :inputId="'column' + index" />
                                        <label :for="'column' + index" class="ml-2">{{ column.header }}</label>
                                    </div>
                                </div>
                            </OverlayPanel>
                        </div>

                        <span class="p-input-icon-left">
                            <i class="pi pi-search" />
                            <InputText v-model="filters['global'].value" placeholder="Suche..." />
                        </span>

                        <!--
                        <span class="p-float-label" style="margin-left: 1%">
                            <MultiSelect :modelValue="selectedColumns" :options="columns" optionLabel="header" scrollHeight="300px" hide="true"
                                @update:modelValue="onToggle" display="chip" placeholder="Spalten auswählen" >
                                <Button label="Neuen Draft erstellen" icon="pi pi-plus" severity="success" />
                            </MultiSelect>
                            <label>Tabellenspalten</label>
                        </span>
                        -->

                    </template>

                    <template #end>
                        <Button label="Neuen Draft erstellen" icon="pi pi-plus" severity="success" />
                    </template>
                </Toolbar>
            </template>







            <template #empty>Keine Anfragen gefunden</template>

            <Column v-for="(col, index) of selectedColumns" :field="col.field" :header="col.header"
                :key="col.field + '_' + index" sortable></Column>

            <!-- Column Toggle -->
            <!--
            <Column field="state" header="Status" sortable>
                <template #body="slotProps">
                    <RequestState :state="slotProps.data.state" />
                </template>
            </Column>


            <Column field="seriesClosingDate" header="Aktuellestes Veröffentlichungsdatum" sortable>
                <template #body="{ data }">
                    {{ formatToGermanDateTime(data.seriesClosingDate) }}
                </template>
            </Column>
        -->
            <Column headerStyle="width: 5rem; text-align: center" bodyStyle="text-align: center; overflow: visible">
                <template #body>
                    <Button type="button" icon="pi pi-search-plus" rounded outlined />
                </template>
            </Column>


            
        </DataTable>
    </div>
</template>

<script lang="ts">
import { TestDataService } from '@/service/TestDataService';
import RequestState from '@/components/RequestState.vue';

import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Button from 'primevue/button';
import InputText from 'primevue/inputtext';
import { FilterMatchMode } from 'primevue/api';
import Toolbar from 'primevue/toolbar';
import ColumnGroup from 'primevue/columngroup';
import Row from 'primevue/row';
import MultiSelect from 'primevue/multiselect';
import OverlayPanel from 'primevue/overlaypanel';
import Checkbox from 'primevue/checkbox';

export default {
    components: {
        DataTable,
        Column,
        Button,
        InputText,
        RequestState,
        FilterMatchMode,
        Toolbar,
        ColumnGroup,
        Row,
        MultiSelect,
        OverlayPanel,
        Checkbox
    },
    data() {
        return {
            requests: null,
            filters: null,
            loading: true,
            selectedColumns: null,
            columns: null,
        };
    },
    computed: {
        selectedColumnsComputed: {
            get() {
                return this.selectedColumns;
            },
            set(value) {
                this.selectedColumns = value;
            }
        },
        allSelected: {
            get() {
                const allChecked = this.columns.length === this.selectedColumns.length;
                console.log('All Selected:', allChecked); // Debugging
                return allChecked;
            },
            set(value) {
                this.selectedColumns = value ? this.columns : [];
            }
        }
    },
    created() {
        this.filters = {
            'global': { value: null, matchMode: FilterMatchMode.CONTAINS },
        };
        this.columns = [
            { field: 'id', header: 'ID' },
            { field: 'query.title', header: 'Titel' },
            { field: 'state', header: 'Status' },
            { field: 'allowedOrganisations', header: 'Auswertestelle(n)' },
            { field: 'seriesClosingDate', header: 'Aktuellestes Veröffentlichungsdatum' },
            { field: '', header: 'Aktuellestes Schließsdatum' },
            { field: '', header: 'Aktuelleste Zustimmung' },
        ];
        this.selectedColumns = this.columns.filter(column => ['id', 'query.title', 'state', 'allowedOrganisations'].includes(column.field));
    },
    mounted() {
        TestDataService.getRequests().then((data: any) => {
            this.requests = data
            this.loading = false;
        });
    },
    methods: {
        formatToGermanDateTime(dateTimeString: string) {
            if (!dateTimeString || isNaN(Date.parse(dateTimeString))) {
                return '-';
            }
            const date = new Date(dateTimeString);
            const options = { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit', hour12: false };
            return new Intl.DateTimeFormat('de-DE', options).format(date);
        },
        toggle(event) {
            this.$refs.opanel.toggle(event);
        }
    }
};
</script>

<style scoped>
.column-toggle {
    margin-right: 5%;
}
</style>