<template>
    <div class="p-input-icon-right">
        <AutoComplete
            v-model="selectedPrincipal"
            :placeholder="$t('searchPlaceholder')"
            :suggestions="filteredPrincipals"
            optionLabel="name"
            loadingIcon="null"
            @complete="searchPrincipals"
            @item-select="onPrincipalSelected"
        />
        <i class="pi pi-search"></i>
    </div>
</template>

<script lang="ts">
import AutoComplete, { AutoCompleteCompleteEvent } from "primevue/autocomplete";
import { TestDataService } from "@/services/TestDataService";
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
        this.fetchPrincipals();
    },
    methods: {
        // TODO remove the fetch from TestDataService
        fetchPrincipals(): void {
            TestDataService.getPrincipals().then(
                (data: Principal[]) => (this.allPrincipals = data)
            );
        },
        searchPrincipals(event: AutoCompleteCompleteEvent): void {
            const query = event.query.toLowerCase();
            setTimeout(() => {
                this.filteredPrincipals = this.filterPrincipals(query);
            }, 250);
        },
        filterPrincipals(query: string): Principal[] {
            return this.allPrincipals.filter((principal) =>
                principal.name.toLowerCase().includes(query)
            );
        },
        onPrincipalSelected(): void {
            this.$emit("update:principal", { ...this.selectedPrincipal });
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
