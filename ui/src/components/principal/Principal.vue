<template>
    <div>
        <Fieldset legend="Antragssteller" :toggleable="true" class="flex w-25rem m-2">
            <PrincipalField label="Name" icon="pi pi-user" :value="principal.name" :editable="editable" />
            <PrincipalField label=" Organization" icon="pi pi-building" :value="principal.organization"
                :editable="editable" />
            <PrincipalField label="E-Mail" icon="pi pi-envelope" :value="principal.email" :editable="editable" />
            <PrincipalField label="Telefon" icon="pi pi-phone" :value="principal.phone" :editable="editable" />
        </Fieldset>
    </div>

    <!--TODO principal to real model -->
    <!--TODO input validation -->
</template>

<script lang="ts">
import Fieldset from 'primevue/fieldset';
import PrincipalField from '@/components/principal/PrincipalField.vue'
export default {
    components: {
        Fieldset,
        PrincipalField,
    },
    props: {
        principal: { type: Object, required: true, },
        editable: { type: Boolean, default: false, },
    }
};
</script>
