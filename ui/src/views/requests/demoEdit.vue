<template>
    <div v-if="request">
        <RequestHeader
            :id="request.id"
            :title="request.query.title"
            :state="request.requestState"
            :tags="request.tags"
            :editable="editable"
            @update:tags="request.tags = $event"
            @update:title="request.query.title = $event"
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
                        <PrincipalEdit
                            v-if="isDraft()"
                            :principal="request.query.principal"
                            @update:principal="request.query.principal = $event"
                        />
                        <PrincipalView
                            v-else
                            :principal="request.query.principal"
                        />
                        <OrganizationEdit
                            :organizationIds="request.authorizedOrgs"
                            @update:organizationIds="
                                request.authorizedOrgs = new Set($event)
                            "
                        />
                    </div>
                    <div class="col-12">
                        <Textfield
                            class="ml-3"
                            :content="request.query.description"
                            :label="$t('description')"
                            :state="request.requestState"
                            :fieldSetHeight="'h-22rem'"
                            :editable="editable"
                            @update:content="request.query.description = $event"
                        />
                    </div>
                </div>
            </div>
            <div class="col-5">
                <RequestTargetNodes
                    class="mr-3"
                    :targetNodeIds="request.targetNodes"
                    :execution="request.executions[0]"
                    :requestState="request.requestState"
                    :requestType="request.requestType"
                    :fieldSetHeight="'h-48-4rem'"
                    :editable="editable"
                    @update:targetNodeIds="
                        request.targetNodes = new Set($event)
                    "
                />
            </div>
            <div class="col-12">
                <Textfield
                    class="mx-3 mb-3"
                    :content="request.query.sql"
                    :label="$t('sql')"
                    :state="request.requestState"
                    :fieldSetHeight="'h-22rem'"
                    :editable="editable"
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
import Textfield from "@/components/textareas/Textfield.vue";
import RequestTargetNodes from "@/components/targetNodes/RequestTargetNodes.vue";
import Button from "primevue/button";
import ProgressSpinner from "primevue/progressspinner";
import Divider from "primevue/divider";

import { TestDataService } from "@/services/TestDataService";
import { Request } from "@/utils/Types";
import RequestHeader from "@/components/headers/RequestHeader.vue";
import SingleMeta from "@/components/meta/SingleMeta.vue";
import { RequestState } from "@/utils/Enums";

export default {
    components: {
        PrincipalView,
        PrincipalEdit,
        OrganizationEdit,
        Button,
        RequestTargetNodes,
        ProgressSpinner,
        Divider,
        Textfield,
        RequestHeader,
        SingleMeta,
    },
    data() {
        return {
            request: null as Request | null,
        };
    },
    mounted() {
        const requestId = this.$route.params.id;
        console.log(requestId);
        console.log(typeof requestId);
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
    },
};
</script>
