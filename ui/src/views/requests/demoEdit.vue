<template>
    <div v-if="request">
        <RequestHeaderEdit
            :id="request.id"
            :title="request.query.title"
            :state="request.requestState"
            :tags="request.tags"
            :menu="isDraft() ? editDraftMenu : editRequestMenu"
            :isTitleEditingDisabled="!isDraft()"
            :isTagEditingDisabled="isArchived()"
            @update:tags="request.tags = $event"
            @update:title="request.query.title = $event"
        />

        <div class="grid nested-grid">
            <div class="col-7">
                <div class="grid">
                    <div class="col-7">
                        <SingleMetaEdit
                            v-if="isSingleRequest()"
                            class="ml-3 h-25-3rem"
                            :type="request.requestType"
                            :execution="request.executions[0]"
                            :querySchedule="request.query.singleExecution"
                            :disabled="!isDraft()"
                            @update:execution="request.executions[0] = $event"
                            @update:querySchedule="
                                request.query.singleExecution = $event
                            "
                        />
                        <SeriesMetaEdit
                            v-if="!isSingleRequest()"
                            class="ml-3 h-25-3rem"
                            :type="request.requestType"
                            :querySchedule="request.query.repeatedExecution"
                            :isAutoPublishing="request.isAutoPublishing"
                            :seriesClosingDate="request.seriesClosingDate"
                            :seriesArchiveDate="request.seriesArchiveDate"
                            :disabled="!isDraft()"
                            @update:querySchedule="
                                request.query.repeatedExecution = $event
                            "
                            @update:isAutoPublishing="
                                request.isAutoPublishing = $event
                            "
                            @update:seriesClosingDate="
                                request.seriesClosingDate = $event
                            "
                            @update:seriesArchiveDate="
                                request.seriesArchiveDate = $event
                            "
                        />
                    </div>
                    <div class="col-5">
                        <PrincipalEdit
                            :principal="request.query.principal"
                            :disabled="!isDraft()"
                            @update:principal="request.query.principal = $event"
                        />
                        <OrganizationEdit
                            :organizationIds="request.authorizedOrgs"
                            :disabled="isArchived()"
                            @update:organizationIds="
                                request.authorizedOrgs = new Set($event)
                            "
                        />
                    </div>
                    <div class="col-12">
                        <TextFieldEdit
                            class="ml-3"
                            :content="request.query.description"
                            :label="$t('description')"
                            :fieldSetHeight="'h-22rem'"
                            :disabled="!isDraft()"
                            @update:content="request.query.description = $event"
                        />
                    </div>
                </div>
            </div>
            <div class="col-5">
                <TargetNodesEdit
                    v-if="isDraft() || isRequestSingleAndStillPending()"
                    class="mr-3"
                    :targetNodeIds="request.targetNodes"
                    :fieldSetHeight="'h-48-4rem'"
                    @update:targetNodeIds="
                        request.targetNodes = new Set($event)
                    "
                />
                <p v-else>ToDo disabled</p>
            </div>
            <div class="col-12">
                <TextFieldEdit
                    class="mx-3 mb-3"
                    :content="request.query.sql"
                    :label="$t('sql')"
                    :fieldSetHeight="'h-22rem'"
                    :disabled="!isDraft()"
                    @update:content="request.query.sql = $event"
                />
            </div>

            <div class="col-12" v-if="!isSingleRequest()">
                <ExecutionTableEdit
                    class="mx-3 mb-3"
                    :executions="request.executions"
                    :anchoredSequenceIdRef="request.anchoredSequenceIdRef"
                    @update:executions="request.executions = $event"
                    @update:anchoredSequenceIdRef="
                        request.anchoredSequenceIdRef = $event
                    "
                />
            </div>
        </div>
    </div>
    <div v-else class="flex justify-content-center flex-wrap py-4">
        <ProgressSpinner />
    </div>
</template>

<script lang="ts">
import PrincipalEdit from "@/components/principals/PrincipalEdit.vue";
import OrganizationEdit from "@/components/organizations/OrganizationEdit.vue";
import TextFieldEdit from "@/components/textfields/TextFieldEdit.vue";
import TargetNodesEdit from "@/components/targetNodes/TargetNodesEdit.vue";
import TargetNodesView from "@/components/targetNodes/TargetNodesView.vue";
import Button from "primevue/button";
import ProgressSpinner from "primevue/progressspinner";
import Divider from "primevue/divider";

import { TestDataService } from "@/services/TestDataService";
import { Request } from "@/utils/Types";
import RequestHeaderEdit from "@/components/headers/RequestHeaderEdit.vue";
import SingleMetaEdit from "@/components/meta/SingleMetaEdit.vue";
import SeriesMetaEdit from "@/components/meta/SeriesMetaEdit.vue";
import { RequestState, RequestType, ExecutionState } from "@/utils/Enums";
import ExecutionTableEdit from "@/components/tables/ExecutionTableEdit.vue";

export default {
    components: {
        PrincipalEdit,
        OrganizationEdit,
        Button,
        TargetNodesEdit,
        TargetNodesView,
        ProgressSpinner,
        Divider,
        TextFieldEdit,
        RequestHeaderEdit,
        SingleMetaEdit,
        SeriesMetaEdit,
        ExecutionTableEdit,
    },
    data() {
        return {
            request: null as Request | null,
            // TODO: add routing and services
            editDraftMenu: [
                { label: this.$t("menu.draft.save") },
                { label: this.$t("cancel") },
            ],
            editRequestMenu: [
                { label: this.$t("menu.request.save") },
                { label: this.$t("cancel") },
            ],
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
    methods: {
        isDraft(): boolean {
            return this.request?.requestState === RequestState.DRAFT;
        },
        isArchived(): boolean {
            return this.request?.requestState === RequestState.ARCHIVED;
        },
        isSingleRequest(): boolean {
            return this.request?.requestType === RequestType.SINGLE;
        },
        isRequestSingleAndStillPending(): boolean {
            if (this.request?.requestType === RequestType.SINGLE) {
                const actualExecution = this.getMostActualRequestExecution();
                if (actualExecution.executionState === ExecutionState.PENDING) {
                    return true;
                }
            }
            return false;
        },
        getMostActualRequestExecution(): RequestExecution | undefined {
            const executions = this.request?.executions;
            if (executions.length === 0) {
                return undefined;
            }
            let mostActualExecution = executions[0];
            for (const execution of executions) {
                if (execution.sequenceId > mostActualExecution.sequenceId) {
                    mostActualExecution = execution;
                }
            }
            return mostActualExecution;
        },
    },
};
</script>
