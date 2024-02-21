<template>
    <Fieldset :legend="$t('principal')">
        <div class="flex justify-content-center mb-3">
            <PrincipalSearch
                @update:principal="onPrincipalSelected"
                :disabled="disabled"
            />
        </div>
        <!-- TODO input validation -->
        <div v-for="(field, index) in principalFields" :key="index">
            <InputGroup class="mb-2">
                <InputGroupAddon>
                    <i :class="field.icon" class="text-xl" />
                </InputGroupAddon>
                <span class="p-float-label">
                    <InputText
                        size="small"
                        v-model="principal[field.key]"
                        :disabled="disabled"
                    />
                    <label>{{ field.label }}</label>
                </span>
            </InputGroup>
        </div>
    </Fieldset>
</template>

<script lang="ts">
import Fieldset from "primevue/fieldset";
import InputGroup from "primevue/inputgroup";
import InputGroupAddon from "primevue/inputgroupaddon";
import InputText from "primevue/inputtext";
import { Principal } from "@/utils/Types";
import PrincipalSearch from "./PrincipalSearch.vue";
import PrincipalCommon from "./PrincipalCommon.vue";

export default {
    components: {
        Fieldset,
        InputGroup,
        InputGroupAddon,
        InputText,
        PrincipalSearch,
        PrincipalCommon,
    },
    mixins: [PrincipalCommon],
    props: {
        disabled: {
            type: Boolean,
            default: false,
        },
    },
    methods: {
        onPrincipalSelected(selectedPrincipal: Principal) {
            this.$emit("update:principal", { ...selectedPrincipal });
        },
    },
};
</script>
