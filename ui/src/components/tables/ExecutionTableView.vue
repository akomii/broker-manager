import ScheduledDateViewVue from '../datePickers/ScheduledDateView.vue';
<template>
    <Fieldset :legend="$t('executions')">
        <DataTable :value="executions">
            <Column field="sequenceId" :header="$t('sequenceId')" />
            <Column field="externalId" :header="$t('externalId')" />
            <Column field="creator" :header="$t('creator')" />
            <Column
                field="executionState"
                :header="$t('enums.executionState.label')"
            >
                <template #body="slotProps">
                    <ExecutionStateLabel
                        :state="slotProps.data.executionState"
                    />
                </template>
            </Column>
            <Column field="createdDate" :header="$t('dates.createdDate')">
                <template #body="slotProps">
                    <DateView :date="slotProps.data.createdDate" />
                </template>
            </Column>
            <Column field="referenceDate" :header="$t('dates.referenceDate')">
                <template #body="slotProps">
                    <DateView :date="slotProps.data.referenceDate" />
                </template>
            </Column>
            <Column field="publishDate" :header="$t('dates.publishDate')">
                <template #body="slotProps">
                    <ScheduledDateView
                        :tooltipLabel="$t('dates.publishDate')"
                        :scheduledDate="slotProps.data.scheduledPublishDate"
                        :actualDate="slotProps.data.publishedDate"
                    />
                </template>
            </Column>
            <Column field="executionDate" :header="$t('dates.executionDate')">
                <template #body="slotProps">
                    <DateView :date="slotProps.data.executionDate" />
                </template>
            </Column>
            <Column field="closingDate" :header="$t('dates.closingDate')">
                <template #body="slotProps">
                    <ScheduledDateView
                        :tooltipLabel="$t('dates.closingDate')"
                        :scheduledDate="slotProps.data.scheduledClosingDate"
                        :actualDate="slotProps.data.closedDate"
                    />
                </template>
            </Column>
            <Column field="archiveDate" :header="$t('dates.archiveDate')">
                <template #body="slotProps">
                    <ScheduledDateView
                        :tooltipLabel="$t('dates.archiveDate')"
                        :scheduledDate="slotProps.data.scheduledArchiveDate"
                        :actualDate="slotProps.data.archivedDate"
                    />
                </template>
            </Column>
            <!--TODO Show TargetNodeView in Popup-->
            <Column field="acceptance" :header="$t('acceptance')">
                <template #body="slotProps">
                    {{
                        $t("XofY", {
                            x: countCompletedNodes(
                                slotProps.data.nodeStatusInfos
                            ),
                            y: slotProps.data.nodeStatusInfos.length,
                        })
                    }}
                </template>
            </Column>
            <Column field="actions">
                <template #body="slotProps">
                    <MenuButton
                        :icon="'pi pi-ellipsis-v'"
                        :outlined="true"
                        :menu="exeuctionMenu"
                    />
                </template>
            </Column>
        </DataTable>
    </Fieldset>
</template>

<script lang="ts">
import Fieldset from "primevue/fieldset";
import DataTable from "primevue/datatable";
import Column from "primevue/column";
import { RequestExecution, NodeStatusInfo } from "@/utils/Types";
import ExecutionStateLabel from "@/components/labels/ExecutionStateLabel.vue";
import DateView from "@/components/datePickers/DateView.vue";
import ScheduledDateView from "@/components/datePickers/ScheduledDateView.vue";
import MenuButton from "@/components/common/MenuButton.vue";

export default {
    components: {
        Fieldset,
        DataTable,
        Column,
        ExecutionStateLabel,
        DateView,
        ScheduledDateView,
        MenuButton,
    },
    props: {
        executions: {
            type: Array as () => RequestExecution[],
            required: true,
        },
    },
    data() {
        return {
            exeuctionMenu: [
                { label: this.$t("closeExecution") },
                { label: this.$t("archiveExecution") },
                { label: this.$t("goToResults") },
            ],
        };
    },
    methods: {
        countCompletedNodes(nodeInfos: NodeStatusInfo[]): number {
            return nodeInfos.filter((node) => node.completed !== null).length;
        },
    },
};
</script>
