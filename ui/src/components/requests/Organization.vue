<template>
    <div>
        <Fieldset legend="Auswertestelle(n)" :toggleable="true" class="flex w-25rem m-2">
            <template v-if="editable">
                <h3 class="underline m-1 text-center ">Ausgewählte Auswertestellen</h3>
                <ScrollPanel class="w-24rem h-10rem">
                    <template v-for="organization in modelValue">
                        <div class="hover:bg-red-100 flex justify-content-start flex-wrap"
                            @click="removeOrganization(organization)">
                            <i class="pi pi-minus-circle p-3 mt-1"></i>
                            <p class="text-xl my-3">{{ organization }}</p>
                        </div>
                    </template>
                </ScrollPanel>
                <Divider />
                <h3 class="underline m-1 text-center">Verfügbare Auswertestellen</h3>
                <ScrollPanel class="w-24rem h-10rem">
                    <template v-for="organization in allOrganizations">
                        <div class="hover:bg-green-100 flex justify-content-start flex-wrap"
                            @click="addOrganization(organization)">
                            <i class="pi pi-plus-circle p-3 mt-1"></i>
                            <p class="text-xl my-3">{{ organization }}</p>
                        </div>
                    </template>
                </ScrollPanel>
            </template>
            <template v-else>
                <ScrollPanel class="w-24rem max-h-11rem">
                    <template v-for="organization in modelValue">
                        <div class="hover:surface-100">
                            <p class="text-left text-xl p-3 m-auto">{{ organization }}</p>
                        </div>
                    </template>
                </ScrollPanel>
            </template>
        </Fieldset>
    </div>
</template>

<script lang="ts">
import Fieldset from 'primevue/fieldset';
import ScrollPanel from 'primevue/scrollpanel';
import Divider from 'primevue/divider';

import { TestDataService } from '@/service/TestDataService';

export default {
    components: {
        Fieldset,
        ScrollPanel,
        Divider
    },
    props: {
        modelValue: { type: Array as () => string[], required: true },
        editable: { type: Boolean, default: false },
    },
    data() {
        return {
            allOrganizations: [] as string[],
        };
    },
    mounted() {
        TestDataService.getOrganizations().then((data: string[]) => {
            this.allOrganizations = data.filter(org => !this.modelValue.includes(org));
        });
    },
    methods: {
        addOrganization(organization: string) {
            this.allOrganizations = this.allOrganizations.filter(org => org !== organization);
            const updatedModelValue = [...this.modelValue, organization].sort();
            this.$emit('update:modelValue', updatedModelValue);
        },
        removeOrganization(organization: string) {
            const updatedModelValue = this.modelValue.filter(org => org !== organization).sort();
            this.allOrganizations = [...this.allOrganizations, organization].sort();
            this.$emit('update:modelValue', updatedModelValue);
        },
    }
};
</script>
