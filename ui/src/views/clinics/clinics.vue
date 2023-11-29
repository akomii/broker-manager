<template>
    <div v-if="request">
        <div class="flex justify-content-center flex-wrap mt-5">
            <Button :label="editable ? 'Änderungen speichern' : 'Antragssteller editieren'" @click="click" />
        </div>
        <Divider/>
        <TargetNodes v-model="request.targetNodes" :editable="editable"></TargetNodes>
        <Divider/>
        <SQL v-model="request.query.sql" :editable="editable"></SQL>
        <Divider/>
        <Description v-model="request.query.description" :editable="editable"></Description>
        <Divider/>
        <Organization v-model="request.authorizedOrgs" :editable="editable"></Organization>
        <Divider/>
        <Principal v-model="request.query.principal" :editable="editable" />
        <Divider/>
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
import TargetNodes from '@/components/requests/targetNodes/DraftTargetNodes.vue';
import Button from 'primevue/button';
import ProgressSpinner from 'primevue/progressspinner';
import Divider from 'primevue/divider';

import { TestDataService } from '@/service/TestDataService';
import { Request } from '@/utils/Types';

export default {
    components: {
        Principal,
        Button,
        Organization,
        Description,
        TargetNodes,
        ProgressSpinner,
        Divider,
        SQL
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
        },
    }
};
</script>
