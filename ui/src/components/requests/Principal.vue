<template>
    <div>
        <Fieldset legend="Antragssteller" :toggleable="true" class="flex w-25rem m-2">
            <template v-for="field in principalFields">
                <template v-if="editable">
                    <InputGroup class="mb-2 h-3rem">
                        <InputGroupAddon>
                            <i :class="field.icon" class="text-xl"></i>
                        </InputGroupAddon>
                        <span class="p-float-label">
                            <InputText :id="field.id" size="small" class="flex w-18rem" v-model="principal[field.id]" />
                            <label :for="field.id">{{ field.label }}</label>
                        </span>
                    </InputGroup>
                </template>

                <template v-else>
                    <div class="field grid m-0">
                        <label :for="field.id" class="col-fixed w-4rem">
                            <i :class="field.icon" class="text-xl border-round surface-200 p-2 text-color-secondary"></i>
                        </label>
                        <div class="col pt-2" :id="field.id">
                            {{ field.value }}
                        </div>
                    </div>
                </template>
            </template>
        </Fieldset>
    </div>

    <!--TODO principal to real model -->
    <!--TODO input validation -->
</template>

<script lang="ts">
import Fieldset from 'primevue/fieldset';
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';
import InputText from 'primevue/inputtext';

export default {
    components: {
        Fieldset,
        InputGroup,
        InputGroupAddon,
        InputText,
    },
    props: {
        principal: { type: Object, required: true, },
        editable: { type: Boolean, default: false, },
    },
    computed: {
        principalFields() {
            return [
                { label: 'Name', icon: 'pi pi-user', id: 'name', value: this.principal.name },
                { label: 'Organization', icon: 'pi pi-building', id: 'organization', value: this.principal.organization },
                { label: 'E-Mail', icon: 'pi pi-envelope', id: 'email', value: this.principal.email },
                { label: 'Telefon', icon: 'pi pi-phone', id: 'phone', value: this.principal.phone },
            ];
        },
    },
};
</script>
