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
                :maxSelectedLabels="4"
                :selectedItemsLabel="
                    $t('orgsSelected', { numOrgs: organizationsDummy.length })
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
    mounted() {
        this.fetchOrganizations();
    },
    methods: {
        // TODO remove the fetch from TestDataService
        async fetchOrganizations() {
            const data: Organization[] =
                await TestDataService.getOrganizations();
            this.allOrganizations = data.sort((a, b) =>
                a.name.localeCompare(b.name)
            );
            this.organizationsDummy = this.allOrganizations.filter((org) =>
                this.organizationIds.has(org.id)
            );
        },
        onOrganizationSelected() {
            this.organizationsDummy.sort((a, b) =>
                a.name.localeCompare(b.name)
            );
            this.$emit(
                "update:organizationIds",
                this.organizationsDummy.map((org) => org.id)
            );
        },
    },
};
</script>
