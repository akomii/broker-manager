<template>
    <div>
        <Fieldset
            legend="Auswertestelle(n)"
            :toggleable="true"
            class="flex w-25rem m-2"
        >
            <template v-if="editable">
                <MultiSelect
                    v-model="localModelValue"
                    @change="updateModelValue"
                    :options="allOrganizations"
                    filter
                    optionLabel="name"
                    placeholder="Auswertestellen auswählen"
                    class="w-24rem"
                />
            </template>
            <template v-else>
                <ScrollPanel class="w-24rem max-h-11rem">
                    <template v-for="organization in localModelValue">
                        <div class="hover:surface-100">
                            <p class="text-left text-xl p-2 m-auto">
                                {{ organization.name }}
                            </p>
                        </div>
                    </template>
                </ScrollPanel>
            </template>
        </Fieldset>
    </div>
    <!-- TODO display only the dropdown without the text input -->
</template>

<script lang="ts">
import Fieldset from "primevue/fieldset";
import ScrollPanel from "primevue/scrollpanel";
import MultiSelect from "primevue/multiselect";
import { Organization } from "@/utils/Types";
import { TestDataService } from "@/service/TestDataService";

export default {
    components: {
        Fieldset,
        ScrollPanel,
        MultiSelect,
    },
    props: {
        modelValue: { type: Array as () => number[], required: true },
        editable: { type: Boolean, default: false },
    },
    data() {
        return {
            allOrganizations: [] as Organization[],
            localModelValue: [] as Organization[],
        };
    },
    mounted() {
        TestDataService.getOrganizations().then((data: Organization[]) => {
            this.allOrganizations = data.sort((a, b) =>
                a.name.localeCompare(b.name)
            );
            this.localModelValue = this.allOrganizations.filter((org) =>
                this.modelValue.includes(org.id)
            );
        });
    },
    watch: {
        modelValue: {
            immediate: true,
            handler(newValue) {
                if (newValue && newValue.length > 0) {
                    if (this.allOrganizations.length > 0) {
                        this.localModelValue = this.allOrganizations.filter(
                            (org) => this.modelValue.includes(org.id)
                        );
                    }
                }
            },
        },
    },
    methods: {
        updateModelValue() {
            this.localModelValue.sort((a, b) => a.name.localeCompare(b.name));
            this.$emit(
                "update:modelValue",
                this.localModelValue.map((org) => org.id)
            );
        },
    },
};
</script>
