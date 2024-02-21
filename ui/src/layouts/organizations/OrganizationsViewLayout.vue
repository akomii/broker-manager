<template>
    <Fieldset :legend="$t('organizations')">
        <ScrollPanel class="custom-scrollbar" :class="scrollPanelHeight">
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
                <p class="flex justify-content-center">
                    {{ $t("noOrgsSelected") }}
                </p>
            </template>
        </ScrollPanel>
    </Fieldset>
</template>

<script lang="ts">
import Fieldset from "primevue/fieldset";
import ScrollPanel from "primevue/scrollpanel";
import OrganizationsCommon from "./OrganizationsCommon.vue";

export default {
    components: {
        Fieldset,
        ScrollPanel,
    },
    mixins: [OrganizationsCommon],
    props: {
        scrollPanelHeight: {
            type: String,
            default: "max-h-9rem",
        },
    },
    // TODO add Organization fetch only for the ones I need
};
</script>
