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
                    </div>
                    <div class="col-5">
                        <component
                            :is="isDraft() ? 'PrincipalEdit' : 'PrincipalView'"
                            :principal="request.query.principal"
                            @update:principal="request.query.principal = $event"
                        />
                        <OrganizationEdit
                            :organizationIds="request.authorizedOrgs"
                            @update:organizationIds="
                                request.authorizedOrgs = new Set($event)
                            "
                        />
                    </div>
                    <div class="col-12">
                        <component
                            :is="isDraft() ? 'TextFieldEdit' : 'TextFieldView'"
                            class="ml-3"
                            :content="request.query.description"
                            :label="$t('description')"
                            :fieldSetHeight="'h-22rem'"
                            @update:content="request.query.description = $event"
                        />
                    </div>
                </div>
            </div>
            <div class="col-5">
                <component
                    :is="
                        isDraft() || isRequestSingleAndStillPending()
                            ? 'TargetNodesEdit'
                            : 'TargetNodesView'
                    "
                    class="mr-3"
                    :targetNodeIds="request.targetNodes"
                    :fieldSetHeight="'h-48-4rem'"
                    :execution="getMostActualRequestExecution()"
                    :requestState="request.requestState"
                    :showProcessingStateInfo="!isDraft()"
                    @update:targetNodeIds="
                        request.targetNodes = new Set($event)
                    "
                />
            </div>
            <div class="col-12">
                <component
                    :is="isDraft() ? 'TextFieldEdit' : 'TextFieldView'"
                    class="mx-3 mb-3"
                    :content="request.query.sql"
                    :label="$t('sql')"
                    :fieldSetHeight="'h-22rem'"
                    @update:content="request.query.sql = $event"
                />
            </div>
        </div>
    </div>
    <div v-else class="flex justify-content-center flex-wrap py-4">
        <ProgressSpinner />
    </div>
</template>

<script lang="ts">
import PrincipalView from "@/components/principals/PrincipalView.vue";
import PrincipalEdit from "@/components/principals/PrincipalEdit.vue";
import OrganizationEdit from "@/components/organizations/OrganizationEdit.vue";
import TextFieldView from "@/components/textfields/TextFieldView.vue";
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
import { RequestState, RequestType, ExecutionState } from "@/utils/Enums";

export default {
    components: {
        PrincipalView,
        PrincipalEdit,
        OrganizationEdit,
        Button,
        TargetNodesEdit,
        TargetNodesView,
        ProgressSpinner,
        Divider,
        TextFieldView,
        TextFieldEdit,
        RequestHeaderEdit,
        SingleMetaEdit,
    },
    data() {
        return {
            request: null as Request | null,
            // TODO: add routing and services
            editDraftMenu: [
                { label: this.$t("menu.draft.save") },
                { label: this.$t("menu.cancel") },
            ],
            editRequestMenu: [
                { label: this.$t("menu.request.save") },
                { label: this.$t("menu.cancel") },
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
