<template>
    <div>
        <Fieldset legend="Antragssteller" :toggleable="true" class="flex w-25rem m-2">
            <template v-if="editable">
                <PrincipalAutoComplete @principal-select="onPrincipalSelected" />
                <template v-for="field in principalFields">
                    <InputGroup class="mb-2 h-3rem">
                        <InputGroupAddon>
                            <i :class="field.icon" class="text-xl"></i>
                        </InputGroupAddon>
                        <span class="p-float-label">
                            <InputText size="small" class="flex w-18rem" v-model="modelValue[field.key]" />
                            <label>{{ field.label }}</label>
                        </span>
                    </InputGroup>
                </template>
            </template>
            <template v-else>
                <template v-for="field in principalFields">
                    <div class="field grid m-0">
                        <label class="col-fixed w-4rem">
                            <i :class="field.icon" class="text-xl border-round surface-200 p-2 text-color-secondary"></i>
                        </label>
                        <div class="col pt-2">
                            {{ modelValue[field.key] }}
                        </div>
                    </div>
                </template>
            </template>
        </Fieldset>
    </div>

    <!--TODO input validation -->
</template>

<script lang="ts">
import Fieldset from 'primevue/fieldset';
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';
import InputText from 'primevue/inputtext';
import PrincipalAutoComplete from './PrincipalAutoComplete.vue';
import { Principal } from '@/utils/Types';

export default {
    components: {
        Fieldset,
        InputGroup,
        InputGroupAddon,
        InputText,
        PrincipalAutoComplete
    },
    props: {
        modelValue: { type: Object as () => Principal, required: true },
        editable: { type: Boolean, default: false, },
    },
    computed: {
        principalFields() {
            return [
                { label: 'Name', icon: 'pi pi-user', key: 'name' },
                { label: 'Organization', icon: 'pi pi-building', key: 'organization' },
                { label: 'E-Mail', icon: 'pi pi-envelope', key: 'email' },
                { label: 'Telefon', icon: 'pi pi-phone', key: 'phone' },
            ];
        },
    },
    methods: {
        onPrincipalSelected(selectedPrincipal: Principal) {
            this.$emit('update:modelValue', { ...selectedPrincipal });
        }
    }
};
</script>
