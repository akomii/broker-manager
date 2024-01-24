<template>
    <Fieldset :legend="$t('organizations')" :toggleable="true">
        <template v-if="editable">
            <MultiSelect
                class="w-full"
                v-model="organizationsDummy"
                filter
                :filterPlaceholder="$t('searchPlaceholder')"
                optionLabel="name"
                :options="allOrganizations"
                :maxSelectedLabels="3"
                :selectedItemsLabel="
                    $t('xOrgsSelected', { numOrgs: organizationsDummy.length })
                "
                :placeholder="$t('selectOrgsPlaceholder')"
                @change="onOrganizationSelected"
            />
        </template>
        <template v-else>
            <ScrollPanel class="max-h-8rem custom-scrollbar">
                <template
                    v-for="organization in organizationsDummy"
                    v-if="organizationsDummy.length"
                >
                    <div class="flex align-items-center hover:surface-100">
                        <i class="pi pi-angle-double-right mr-2 text-sm" />
                        <p>{{ organization.name }}</p>
                    </div>
                </template>
                <template v-else>
                    <p>{{ $t("noOrgsSelected") }}</p>
                </template>
            </ScrollPanel>
        </template>
    </Fieldset>
</template>

<script lang="ts">
import Fieldset from "primevue/fieldset";
import ScrollPanel from "primevue/scrollpanel";
import MultiSelect from "primevue/multiselect";
import { Organization } from "@/utils/Types";
import { TestDataService } from "@/services/TestDataService";

export default {
    components: {
        Fieldset,
        ScrollPanel,
        MultiSelect,
    },
    props: {
        organizationIds: {
            type: Set<number>,
            required: true,
        },
        editable: {
            type: Boolean,
            default: false,
        },
    },
    data() {
        return {
            allOrganizations: [] as Organization[],
            organizationsDummy: [] as Organization[],
        };
    },
    async mounted() {
        await this.fetchAndSortOrganizations();
    },
    methods: {
        // TODO remove the fetch from TestDataService
        async fetchAndSortOrganizations(): Promise<void> {
            const data: Organization[] =
                await TestDataService.getOrganizations();
            this.allOrganizations = this.sortOrganizationsByName(data);
            this.organizationsDummy = this.filterOrganizationsById(
                this.allOrganizations
            );
        },
        sortOrganizationsByName(organizations: Organization[]): Organization[] {
            return organizations.sort((a, b) => a.name.localeCompare(b.name));
        },
        filterOrganizationsById(organizations: Organization[]): Organization[] {
            return organizations.filter((org) =>
                this.organizationIds.has(org.id)
            );
        },
        onOrganizationSelected(): void {
            this.organizationsDummy = this.sortOrganizationsByName(
                this.organizationsDummy
            );
            this.$emit(
                "update:organizationIds",
                this.organizationsDummy.map((org) => org.id)
            );
        },
    },
};
</script>
