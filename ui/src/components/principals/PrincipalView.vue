<template>
    <Fieldset :legend="$t('principal')">
        <div v-for="(field, index) in principalFields" :key="index">
            <div class="flex align-items-center mb-2 gap-2">
                <i
                    :class="field.icon"
                    class="text-xl border-round surface-200 p-2 text-color-secondary"
                />
                {{ principal[field.key] }}
            </div>
        </div>
    </Fieldset>
</template>

<script lang="ts">
import Fieldset from "primevue/fieldset";
import PrincipalCommon from "./PrincipalCommon.vue";

export default {
    components: {
        Fieldset,
    },
    mixins: [PrincipalCommon],
};
</script>
