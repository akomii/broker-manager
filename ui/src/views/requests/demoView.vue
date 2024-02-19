<template>
    <div v-if="request">
        <RequestHeaderView
            :id="request.id"
            :title="request.query.title"
            :state="request.requestState"
            :tags="request.tags"
            :menu="getMenu()"
        />

        <div class="grid nested-grid">
            <div class="col-7">
                <div class="grid">
                    <div class="col-7">
                        <SingleMetaView
                            v-if="isSingleRequest()"
                            class="ml-3 h-25-3rem"
                            :type="request.requestType"
                            :execution="request.executions[0]"
                            :querySchedule="request.query.singleExecution"
                            :requestHistory="request.modificationHistory"
                        />
                        <SeriesMetaView
                            v-if="!isSingleRequest()"
                            class="ml-3 h-25-3rem"
                            :type="request.requestType"
                            :querySchedule="request.query.repeatedExecution"
                            :isAutoPublishing="request.isAutoPublishing"
                            :seriesClosingDate="request.seriesClosingDate"
                            :seriesArchiveDate="request.seriesArchiveDate"
                            :requestHistory="request.modificationHistory"
                        />
                    </div>
                    <div class="col-5">
                        <PrincipalView :principal="request.query.principal" />
                        <OrganizationView
                            :organizationIds="request.authorizedOrgs"
                            :scrollPanelHeight="'max-h-9rem'"
                        />
                    </div>
                    <div class="col-12">
                        <TextFieldView
                            class="ml-3"
                            :content="request.query.description"
                            :label="$t('description')"
                            :fieldSetHeight="'h-22rem'"
                        />
                    </div>
                </div>
            </div>
            <div class="col-5">
                <TargetNodesView
                    class="mr-3"
                    :targetNodeIds="request.targetNodes"
                    :fieldSetHeight="'h-48-4rem'"
                    :execution="getMostActualRequestExecution()"
                    :showProcessingStateInfo="!isDraft()"
                />
            </div>
            <div class="col-12">
                <TextFieldView
                    class="mx-3 mb-3"
                    :content="request.query.sql"
                    :label="$t('sql')"
                    :fieldSetHeight="'h-22rem'"
                />
            </div>

            <div class="col-12" v-if="!isSingleRequest()">
                <ExecutionsView
                    class="mx-3 mb-3"
                    :executions="request.executions"
                    :anchoredSequenceIdRef="request.anchoredSequenceIdRef"
                />
            </div>
        </div>
    </div>
    <div v-else class="flex justify-content-center flex-wrap py-4">
        <ProgressSpinner />
    </div>
</template>

<script lang="ts">
import PrincipalView from "@/layouts/principals/PrincipalView.vue";
import OrganizationView from "@/layouts/organizations/OrganizationView.vue";
import TextFieldView from "@/layouts/textfields/TextFieldView.vue";
import TargetNodesView from "@/layouts/targetNodes/TargetNodesView.vue";
import Button from "primevue/button";
import ProgressSpinner from "primevue/progressspinner";
import Divider from "primevue/divider";
import ExecutionsView from "@/layouts/executions/ExecutionsView.vue";

import { TestDataService } from "@/services/TestDataService";
import { Request, RequestExecution } from "@/utils/Types";
import RequestHeaderView from "@/layouts/headers/RequestHeaderView.vue";
import SingleMetaView from "@/layouts/meta/SingleMetaView.vue";
import SeriesMetaView from "@/layouts/meta/SeriesMetaView.vue";
import { UserRole, RequestState, RequestType } from "@/utils/Enums";

export default {
    components: {
        PrincipalView,
        Button,
        OrganizationView,
        TargetNodesView,
        ProgressSpinner,
        Divider,
        TextFieldView,
        RequestHeaderView,
        SingleMetaView,
        SeriesMetaView,
        ExecutionsView,
    },
    data() {
        return {
            request: null as Request | null,
            // TODO: add routing and services
            draftMenu: [
                { label: this.$t("menu.draft.publish") },
                { label: this.$t("menu.draft.duplicate") },
                { label: this.$t("menu.draft.delete") },
                { label: this.$t("menu.draft.edit") },
            ],
            requestMenuIT: [
                { label: this.$t("menu.request.results") },
                { label: this.$t("menu.request.duplicateDraft") },
                { label: this.$t("menu.request.close") },
                { label: this.$t("menu.request.archive") },
                { label: this.$t("menu.request.edit") },
            ],
            requestMenuDAC: [
                { label: this.$t("menu.request.results") },
                { label: this.$t("menu.request.close") },
                { label: this.$t("menu.request.archive") },
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
        hasUserRoleIT(): boolean {
            return this.$userRole === UserRole.IT;
        },
        isSingleRequest(): boolean {
            return this.request?.requestType === RequestType.SINGLE;
        },
        getMenu() {
            if (this.isArchived()) {
                return undefined;
            }
            return this.isDraft()
                ? this.draftMenu
                : this.hasUserRoleIT()
                ? this.requestMenuIT
                : this.requestMenuDAC;
        },
        // TODO: Get most actual execution that is not pending?
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
