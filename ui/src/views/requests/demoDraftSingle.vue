<template>
    <div v-if="request">
        <div class="flex justify-content-center flex-wrap mt-5 mb-8">
            <Button
                :label="
                    editable
                        ? 'Änderungen speichern'
                        : 'Antragssteller editieren'
                "
                @click="click"
            />
        </div>

        <RequestHeader
            :id="request.id"
            :title="request.query.title"
            :state="request.requestState"
            :tags="request.tags"
            :editable="editable"
            @update:tags="request.tags = $event"
            @update:title="request.query.title = $event"
        />

        <div class="grid">
            <div class="col-4 border-solid">
                <SingleMeta
                    :execution="request.executions[0]"
                    :querySchedule="request.query.singleExecution"
                    :editable="editable"
                    @update:execution="request.executions[0] = $event"
                    @update:querySchedule="
                        request.query.singleExecution = $event
                    "
                />
            </div>
            <div class="col-3 border-solid"></div>
            <div class="col-5 border-solid"></div>
        </div>

        <!--

                    <div class="grid">
            <div class="col-3">
                <SingleMeta
                    :execution="request.executions[0]"
                    @update:execution="request.executions[0] = $event"
                    :querySchedule="request.query.singleExecution"
                    @update:querySchedule="
                        request.query.singleExecution.duration = $event
                    "
                    :editable="editable"
                />
            </div>

            <div class="col-3 border-solid">
                <Principal class="border-solid"
                    v-model="request.query.principal"
                    :editable="editable"
                />
                <Organization
                    class="w-26rem "
                    v-model="request.authorizedOrgs"
                    :editable="editable"
                ></Organization>
            </div>

            <div class="col-6 border-solid">
                    <RequestTargetNodes
                        v-model="request.targetNodes"
                        :execution="request.executions[0]"
                        :editable="editable"
                    />
            </div>

            <div class="col-12"></div>
        </div>



        <Header :id="request.id" :title="request.query.title" :state="'ONLINE'" :tags="request.tags" :editable="editable"
        @update:tags="request.tags = $event" @update:title="request.query.title = $event" />

        <div class="grid">
            <div class="col">
                <DraftTargetNodes v-model="request.targetNodes" :editable="editable" />
            </div>
            <Divider layout="vertical" />
            <div class="col">
                <RequestTargetNodes v-model="request.targetNodes" :execution="request.executions[2]" :editable="editable" />
            </div>
        </div>


        <SQL v-model="request.query.sql" :editable="editable"></SQL>
        <Description v-model="request.query.description" :editable="editable"></Description>
    --></div>
    <div v-else class="flex justify-content-center flex-wrap py-4">
        <ProgressSpinner />
    </div>
</template>

<script lang="ts">
import Principal from "@/components/principals/Principal.vue";
import Organization from "@/components/organizations/Organization.vue";
import Description from "@/components/textareas/Description.vue";
import SQL from "@/components/textareas/Sql.vue";
import DraftTargetNodes from "@/components/targetNodes/DraftTargetNodes.vue";
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
        Principal,
        Button,
        Organization,
        Description,
        DraftTargetNodes,
        RequestTargetNodes,
        ProgressSpinner,
        Divider,
        SQL,
        RequestHeader,
        SingleMeta,
    },
    data() {
        return {
            editable: true,
            request: null as Request | null,
        };
    },
    mounted() {
        TestDataService.getRequests()
            .then((data: Request[]) => {
                if (data.length > 0) {
                    this.request = data[1] as Request;
                    this.request.requestState = RequestState.DRAFT;
                }
            })
            .catch((error) => {
                console.error("Error fetching requests:", error);
            });
    },
    methods: {
        click() {
            this.editable = !this.editable;
        },
    },
};
</script>
