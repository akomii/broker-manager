<template>
    <Fieldset :legend="$t('meta')">
        <div class="grid nested-grid">
            <div class="col-12">
                <div class="grid">
                    <div class="col-4">
                        <div class="p-3 m-1 font-bold">
                            {{ $t("apiKey") }}
                        </div>
                        <div class="p-3 m-1 font-bold">
                            {{ $t("organization") }}
                        </div>
                        <div class="p-3 m-1 font-bold">
                            {{ $t("location") }}
                        </div>
                        <div class="p-3 m-1 font-bold">
                            {{ $t("lastContact") }}
                        </div>
                    </div>
                    <div class="col-8">
                        <div class="px-3 pt-1 m-1">
                            <InputText v-model="dummyApiKey" />
                        </div>
                        <div class="px-3 pt-1 m-1">
                            <InputText v-model="dummyOrganization" />
                        </div>
                        <div class="px-3 pt-1 m-1">
                            <InputText v-model="dummyLocation" />
                        </div>

                        <div class="px-3 py-4 m-1">
                            <DateView v-if="lastContact" :date="lastContact" />
                            <NotAvailableIcon v-else />
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </Fieldset>
</template>

<script lang="ts">
import Fieldset from "primevue/fieldset";
import InputText from "primevue/inputtext";
import DateView from "@/components/timeWidgets/DateView.vue";
import NotAvailableIcon from "@/components/icons/NotAvailableIcon.vue";

// TODO refactor and add docs
export default {
    components: {
        Fieldset,
        InputText,
        DateView,
        NotAvailableIcon,
    },
    props: {
        apiKey: {
            type: String,
            required: true,
        },
        organization: {
            type: String,
            required: true,
        },
        location: {
            type: String,
            required: true,
        },
        lastContact: {
            type: Date,
            required: true,
        },
    },
    data() {
        return {
            dummyApiKey: this.apiKey,
            dummyOrganization: this.organization,
            dummyLocation: this.location,
        };
    },
    watch: {
        dummyApiKey(val) {
            this.$emit("update:apiKey", val);
        },
        dummyOrganization(val) {
            this.$emit("update:organization", val);
        },
        dummyLocation(val) {
            this.$emit("update:location", val);
        },
    },
};
</script>
