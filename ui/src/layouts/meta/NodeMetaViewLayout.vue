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
                        <div class="p-3 m-1">
                            {{ apiKey }}
                        </div>
                        <div class="p-3 m-1">
                            {{ organization }}
                        </div>

                        <div class="p-3 m-1">
                            {{ location }}
                        </div>

                        <div class="p-3 m-1">
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
import DateView from "@/components/timeWidgets/DateView.vue";
import NotAvailableIcon from "@/components/icons/NotAvailableIcon.vue";

// TODO refactor and add docs
export default {
    components: {
        Fieldset,
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
};
</script>
