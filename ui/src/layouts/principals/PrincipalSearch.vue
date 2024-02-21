<template>
    <div class="p-input-icon-right">
        <AutoComplete
            v-model="selectedPrincipal"
            :placeholder="$t('searchPlaceholder')"
            :suggestions="filteredPrincipals"
            :disabled="disabled"
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
    props: {
        disabled: {
            type: Boolean,
            default: false,
        },
    },
    data() {
        return {
            allPrincipals: [] as Principal[],
            filteredPrincipals: [] as Principal[],
            selectedPrincipal: {} as Principal,
        };
    },
    async mounted() {
        await this.fetchPrincipals();
    },
    methods: {
        // TODO remove the fetch from TestDataService
        async fetchPrincipals(): Promise<void> {
            this.allPrincipals = await TestDataService.getPrincipals();
        },
        searchPrincipals(event: AutoCompleteCompleteEvent) {
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
        onPrincipalSelected() {
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
