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
                            <DateView :date="lastContact" />
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </Fieldset>
</template>

<script lang="ts">
import Fieldset from "primevue/fieldset";
import Divider from "primevue/divider";
import DateView from "@/components/timeWidgets/DateView.vue";

export default {
    components: {
        Fieldset,
        Divider,
        DateView,
    },
    props: {
        apiKey: String,
        organization: String,
        location: String,
        lastContact: Date,
    },
};
</script>
