<template>
    <div class="card flex justify-content-center mb-3 mt-0">
        <span class="p-input-icon-right">
            <AutoComplete
                placeholder="Suche..."
                v-model="selectedPrincipal"
                optionLabel="name"
                :suggestions="filteredPrincipals"
                @complete="searchPrincipals"
                @item-select="onPrincipalSelected"
                emptySearchMessage="Keine Ergebnisse gefunden"
                loadingIcon="null"
            />
            <i class="pi pi-search"></i>
        </span>
    </div>
</template>

<script lang="ts">
import AutoComplete from "primevue/autocomplete";
import type { AutoCompleteCompleteEvent } from "primevue/autocomplete";
import { TestDataService } from "@/service/TestDataService";
import { Principal } from "@/utils/Types";

export default {
    components: {
        AutoComplete,
    },
    data() {
        return {
            allPrincipals: [] as Principal[],
            filteredPrincipals: [] as Principal[],
            selectedPrincipal: {} as Principal,
        };
    },
    mounted() {
        TestDataService.getPrincipals().then(
            (data: Principal[]) => (this.allPrincipals = data)
        );
    },
    methods: {
        searchPrincipals(event: AutoCompleteCompleteEvent) {
            const query = event.query.toLowerCase();
            setTimeout(() => {
                this.filteredPrincipals = this.allPrincipals.filter(
                    (principal) => principal.name.toLowerCase().includes(query)
                );
            }, 250);
        },
        onPrincipalSelected() {
            this.$emit("principal-select", { ...this.selectedPrincipal });
        },
    },
};
</script>

<style>
.p-autocomplete-empty-message {
    margin: auto;
    padding: 1rem;
}
</style>
