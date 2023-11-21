<template>
    <div>
        <Fieldset legend="Antragssteller" :toggleable="true" class="flex w-25rem m-2">
            <PrincipalField label="Name" icon="pi pi-user" :value="principal.name" />
            <PrincipalField label="Organization" icon="pi pi-building" :value="principal.organization" />
            <PrincipalField label="Email" icon="pi pi-envelope" :value="principal.email" />
            <PrincipalField label="Phone" icon="pi pi-phone" :value="principal.phone" />
        </Fieldset>
    </div>
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
        principal: Object,
    },
};
</script>
