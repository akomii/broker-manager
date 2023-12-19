<template>
    <DataTable v-model:filters="filters" :value="requests" ref="dt" dataKey="id" tableStyle="min-width: 50rem" paginator
        :rows="10" :rowsPerPageOptions="[10, 20, 50]"
        paginatorTemplate="RowsPerPageDropdown FirstPageLink PrevPageLink CurrentPageReport NextPageLink LastPageLink"
        currentPageReportTemplate="{first} bis {last} von {totalRecords}" removableSort sortField="id" :sortOrder="-1"
        :globalFilterFields="['id', 'query.title', 'state', 'allowedOrganisations', 'seriesClosingDate']">

        <!-- TODO spinning loader -->
        <!-- TODO lazy load -->
        <!-- TODO store selected Columns in Cookie-->

        <!-- TODO Column Toggle -->
        <!-- TODO nur eine column variable??? -->
        <!--statische column reihenfolge-->
        <!-- TODO FIX COLUMN ELEMENTS -->
        <!--TODO globalfilterFields-->
        <!-- global search nur auf die tabellen die eingeblendet sind -->

        <!-- TODOOOOOOO ROUTING IS BROKEN-->

        <!-- export always all columns-->

        <!-- Dropdown-->
        <!-- Ansicht -> Archivierte Anfragen anzeigen -->
        <template #header>
            <div class="flex justify-content-between flex-wrap border-solid">
                <div class="border-solid">
                    <h1 class="text-700">Anfragenübersicht</h1>
                    <Button type="button" icon="pi pi-cog" outlined @click="toggle" class="mb-1 mr-3" />
                    <OverlayPanel ref="columnToggle" showCloseIcon>
                        <h3 class="underline mt-auto mb-1">Ansicht</h3>
                        <div class="flex align-items-center">
                            <!--  <Checkbox v-model="allSelected" inputId="selectAll" />
                            <label for="selectAll" class="ml-2">Archivierte Anfragen anzeigen</label> -->
                            <Checkbox />
                            <label class="ml-2">Archivierte Anfragen anzeigen</label>
                        </div>

                        <h3 class="underline mt-auto mb-1 pt-3">Tabellenspalten</h3>
                        <div class="flex align-items-center" v-for="(column, index) in columns" :key="index">
                            <Checkbox :value="column" v-model="selectedColumnsComputed" :inputId="'column' + index" />
                            <label :for="'column' + index" class="ml-2">{{ column.header }}</label>
                        </div>
                        <Divider layout="horizontal" class="my-2" />
                        <div class="flex align-items-center">
                            <Checkbox v-model="allSelected" inputId="selectAll" />
                            <label for="selectAll" class="ml-2">Alle Spalten auswählen</label>
                        </div>
                    </OverlayPanel>
                    <span class="p-input-icon-left">
                        <i class="pi pi-search" />
                        <InputText v-model="filters['global'].value" placeholder="Suche..." />
                    </span>
                </div>
                <Button label="Neuen Draft erstellen" icon="pi pi-plus" severity="success" />
            </div>
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
    <Button icon="pi pi-external-link" outlined label="Tabelle exportieren" @click="exportCSV($event)" />


    <!-- TODO tag element with option to make removable -->

    <Chip class="py-1 px-2">
        <span class="pi pi-tag"></span>
        <span class="ml-1 font-small">tagging test</span>
    </Chip>
</template>

<script lang="ts">
import { TestDataService } from '@/service/TestDataService';
//import RequestState from '@/components/requests/common/RequestState.vue';

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
import Tag from 'primevue/tag';
import Divider from 'primevue/divider';
import Chip from 'primevue/chip';
import Accordion from 'primevue/accordion';
import AccordionTab from 'primevue/accordiontab';

export default {
    components: {
        DataTable,
        Column,
        Button,
        InputText,
        FilterMatchMode,
        Toolbar,
        ColumnGroup,
        Row,
        MultiSelect,
        OverlayPanel,
        Checkbox,
        Tag,
        Divider,
        Chip,
        Accordion,
        AccordionTab,
    },
    data() {
        return {
            requests: null,
            filters: null,
            loading: true,
            selectedColumns: null,
            columns: null,
            selectAll: false,
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
                return this.columns && this.selectedColumns && this.columns.length === this.selectedColumns.length;
            },
            set(value) {
                if (value) {
                    this.selectedColumns = this.columns.map(column => column);
                } else {
                    this.selectedColumns = [];
                }
            }
        }
    },
    created() {
        this.filters = {
            'global': { value: null, matchMode: FilterMatchMode.CONTAINS },
        };
        this.columns = [
            { field: 'id', header: 'ID' },
            { field: 'query.principal.name', header: 'Auftraggeber' },
            { field: 'query.title', header: 'Titel' },
            { field: 'type', header: 'Typ' },
            { field: 'tags', header: 'Tags' },
            { field: 'state', header: 'Status' },
            { field: 'allowedOrganisations', header: 'Auswertestelle(n)' },
            { field: '', header: 'Aktuellestes Veröffentlichungsdatum' },
            { field: 'seriesClosingDate', header: 'Aktuellestes Schließsdatum' },
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
            this.$refs.columnToggle.toggle(event);
        },
        exportCSV() {
            this.$refs.dt.exportCSV();
        }
    }
};
</script>
