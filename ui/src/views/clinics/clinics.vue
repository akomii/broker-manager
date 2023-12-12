<template>
    <div v-if="request">
        <div class="flex justify-content-center flex-wrap mt-5 mb-8">
            <Button :label="editable ? 'Änderungen speichern' : 'Antragssteller editieren'" @click="click" />
        </div>

        <Header :id="request.id" :title="request.query.title" :state="'DRAFT'" :tags="request.tags" :editable="false" />

        <Header :id="request.id" :title="request.query.title" :state="'DRAFT'" :tags="request.tags" :editable="true"
            @update:tags="request.tags = $event" @update:title="request.query.title = $event" />

        <Header :id="request.id" :title="request.query.title" :state="'ONLINE'" :tags="request.tags" :editable="false" />

        <Header :id="request.id" :title="request.query.title" :state="'ONLINE'" :tags="request.tags" :editable="true"
            @update:tags="request.tags = $event" @update:title="request.query.title = $event" />

        <!--
        <div class="grid">
            <div class="col">
                <DraftTargetNodes v-model="request.targetNodes" :editable="editable" />
            </div>
            <Divider layout="vertical" />
            <div class="col">
                <RequestTargetNodes v-model="request.targetNodes" :execution="request.executions[2]" :editable="editable" />
            </div>
        </div>
            <Principal v-model="request.query.principal" :editable="editable" />
            <Organization v-model="request.authorizedOrgs" :editable="editable"></Organization>
            <SQL v-model="request.query.sql" :editable="editable"></SQL>
            <Description v-model="request.query.description" :editable="editable"></Description>
            -->
    </div>
    <div v-else class="flex justify-content-center flex-wrap py-4">
        <ProgressSpinner />
    </div>
</template>

<script lang="ts">
import Principal from '@/components/requests/principal/Principal.vue';
import Organization from '@/components/requests/Organization.vue';
import Description from '@/components/requests/Description.vue';
import SQL from '@/components/requests/Sql.vue';
import DraftTargetNodes from '@/components/requests/targetNodes/DraftTargetNodes.vue';
import RequestTargetNodes from '@/components/requests/targetNodes/RequestTargetNodes.vue';
import Button from 'primevue/button';
import ProgressSpinner from 'primevue/progressspinner';
import Divider from 'primevue/divider';

import { TestDataService } from '@/service/TestDataService';
import { Request } from '@/utils/Types';
import Header from '@/components/requests/Header.vue';

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
        Header
    },
    data() {
        return {
            editable: true,
            request: null as Request | null,
        };
    },
    mounted() {
        TestDataService.getRequests().then((data: Request[]) => {
            if (data.length > 0) {
                this.request = data[0] as Request;
            }
        }).catch(error => {
            console.error("Error fetching requests:", error);
        });
    },
    methods: {
        click() {
            this.editable = !this.editable;
        }
    }
};
</script>
