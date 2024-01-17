<template>
    <Fieldset :legend="$t('organizations')" :toggleable="true">
        <template v-if="editable">
            <!-- TODO display only the dropdown without the text input -->
            <MultiSelect
                class="w-full"
                v-model="organizationsDummy"
                optionLabel="name"
                filter
                :options="allOrganizations"
                :placeholder="$t('selectOrgsPlaceholder')"
                @change="onOrganizationSelected"
            />
        </template>
        <template v-else>
            <ScrollPanel class="max-h-11rem">
                <template v-for="organization in organizationsDummy">
                    <p class="hover:surface-100 text-xl p-2 m-auto">
                        {{ organization.name }}
                    </p>
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
