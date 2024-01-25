<template>
    <Fieldset :legend="$t('organizations')">
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
    </Fieldset>
</template>

<script lang="ts">
import Fieldset from "primevue/fieldset";
import MultiSelect from "primevue/multiselect";
import OrganizationCommon from "./OrganizationCommon.vue";

export default {
    components: {
        Fieldset,
        MultiSelect,
    },
    mixins: [OrganizationCommon],
    methods: {
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
