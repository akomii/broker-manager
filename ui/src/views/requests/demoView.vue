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
                        <SingleMeta
                            class="ml-3 h-25-3rem"
                            :execution="request.executions[0]"
                            :querySchedule="request.query.singleExecution"
                            :editable="editable"
                            @update:execution="request.executions[0] = $event"
                            @update:querySchedule="
                                request.query.singleExecution = $event
                            "
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
                    :requestState="request.requestState"
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
        </div>
    </div>
    <div v-else class="flex justify-content-center flex-wrap py-4">
        <ProgressSpinner />
    </div>
</template>

<script lang="ts">
import PrincipalView from "@/components/principals/PrincipalView.vue";
import OrganizationView from "@/components/organizations/OrganizationView.vue";
import TextFieldView from "@/components/textfields/TextFieldView.vue";
import TargetNodesView from "@/components/targetNodes/TargetNodesView.vue";
import Button from "primevue/button";
import ProgressSpinner from "primevue/progressspinner";
import Divider from "primevue/divider";

import { TestDataService } from "@/services/TestDataService";
import { Request, RequestExecution } from "@/utils/Types";
import RequestHeaderView from "@/components/headers/RequestHeaderView.vue";
import SingleMeta from "@/components/meta/SingleMeta.vue";
import { UserRole, RequestState } from "@/utils/Enums";

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
        SingleMeta,
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
