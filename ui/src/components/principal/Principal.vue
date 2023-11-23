<template>
    <div>
        <Fieldset legend="Antragssteller" :toggleable="true" class="flex w-25rem m-2">
            <template v-if="editable">
                <div class="card flex justify-content-center mb-3 mt-0">
                    <!--
                    <span class="p-float-label">
                        <AutoComplete v-model="selectedPrincipal" optionLabel="name" :suggestions="filteredPrincipals"
                        @complete="search" @item-select="handlePrincipalSelection"
                        emptySearchMessage="Keine Ergebnisse gefunden" />
                        <label>Suche...</label>
                    </span>
-->

                    <span class="p-input-icon-right">
                        <AutoComplete placeholder="Suche..." v-model="selectedPrincipal" optionLabel="name"
                            :suggestions="filteredPrincipals" @complete="search" @item-select="handlePrincipalSelection"
                            emptySearchMessage="Keine Ergebnisse gefunden" loadingIcon=null />
                        <i class="pi pi-search" />
                    </span>

                </div>
                <template v-for="field in principalFields">
                    <InputGroup class="mb-2 h-3rem">
                        <InputGroupAddon>
                            <i :class="field.icon" class="text-xl"></i>
                        </InputGroupAddon>
                        <span class="p-float-label">
                            <InputText size="small" class="flex w-18rem" v-model="principal[field.key]" />
                            <label>{{ field.label }}</label>
                        </span>
                    </InputGroup>
                </template>
            </template>
            <template v-else>
                <template v-for="field in principalFields">
                    <div class="field grid m-0">
                        <label class="col-fixed w-4rem">
                            <i :class="field.icon" class="text-xl border-round surface-200 p-2 text-color-secondary"></i>
                        </label>
                        <div class="col pt-2">
                            {{ principal[field.key] }}
                        </div>
                    </div>
                </template>
            </template>
        </Fieldset>
    </div>

    <!--TODO input validation -->
    <!--SPLIT INTO COMPONENTS -->
    <!--TODO principal to real model -->
</template>

<script lang="ts">
import Fieldset from 'primevue/fieldset';
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';
import InputText from 'primevue/inputtext';
import AutoComplete from 'primevue/autocomplete';

import { TestDataService } from '@/service/TestDataService';

export default {
    components: {
        Fieldset,
        InputGroup,
        InputGroupAddon,
        InputText,
        AutoComplete
    },
    props: {
        principal: { type: Object, required: true, },
        editable: { type: Boolean, default: false, },
    },
    computed: {
        principalFields() {
            return [
                { label: 'Name', icon: 'pi pi-user', key: 'name' },
                { label: 'Organization', icon: 'pi pi-building', key: 'organization' },
                { label: 'E-Mail', icon: 'pi pi-envelope', key: 'email' },
                { label: 'Telefon', icon: 'pi pi-phone', key: 'phone' },
            ];
        },
    },
    data() {
        return {
            principals: null,
            selectedPrincipal: null,
            filteredPrincipals: null
        };
    },
    mounted() {
        TestDataService.getPrincipals().then((data) => (this.principals = data));
    },
    methods: {
        search(event) {
            setTimeout(() => {
                if (!event.query.trim().length) {
                    this.filteredPrincipals = [...this.principals];
                } else {
                    this.filteredPrincipals = this.principals.filter((p) => {
                        return p.name.toLowerCase().includes(event.query.toLowerCase());
                    });
                }
            }, 250);
        },
        handlePrincipalSelection() {
            if (this.selectedPrincipal) {
                this.principal.name = this.selectedPrincipal.name;
                this.principal.organization = this.selectedPrincipal.organization;
                this.principal.email = this.selectedPrincipal.email;
                this.principal.phone = this.selectedPrincipal.phone;
            }
        }
    }
};
</script>

<style>
.p-autocomplete-empty-message {
    margin: auto;
    padding: 1rem;
}
</style>