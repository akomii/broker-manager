<template>
    <Fieldset legend="Antragssteller" :toggleable="true">
        <div v-if="editable">
            <div class="flex justify-content-center mb-3">
                <PrincipalSearch @update:principal="onPrincipalSelected" />
            </div>
            <!--TODO input validation -->
            <div v-for="(field, index) in principalFields" :key="index">
                <InputGroup class="mb-2">
                    <InputGroupAddon>
                        <i :class="field.icon" class="text-xl" />
                    </InputGroupAddon>
                    <span class="p-float-label">
                        <InputText
                            size="small"
                            v-model="principal[field.key]"
                        />
                        <label>{{ field.label }}</label>
                    </span>
                </InputGroup>
            </div>
        </div>
        <template v-else>
            <div v-for="(field, index) in principalFields" :key="index">
                <div class="flex align-items-center mb-2 gap-2">
                    <i
                        :class="field.icon"
                        class="text-xl border-round surface-200 p-2 text-color-secondary"
                    />
                    {{ principal[field.key] }}
                </div>
            </div>
        </template>
    </Fieldset>
</template>

<script lang="ts">
import Fieldset from "primevue/fieldset";
import InputGroup from "primevue/inputgroup";
import InputGroupAddon from "primevue/inputgroupaddon";
import InputText from "primevue/inputtext";
import PrincipalSearch from "./PrincipalSearch.vue";
import { Principal } from "@/utils/Types";

export default {
    components: {
        Fieldset,
        InputGroup,
        InputGroupAddon,
        InputText,
        PrincipalSearch,
    },
    props: {
        principal: {
            type: Object as () => Principal,
            required: true,
        },
        editable: {
            type: Boolean,
            default: false,
        },
    },
    computed: {
        principalFields(): Array<{ label: string; icon: string; key: string }> {
            return [
                {
                    label: this.$t("name"),
                    icon: "pi pi-user",
                    key: "name",
                },
                {
                    label: this.$t("organization"),
                    icon: "pi pi-building",
                    key: "organization",
                },
                {
                    label: this.$t("email"),
                    icon: "pi pi-envelope",
                    key: "email",
                },
                {
                    label: this.$t("phone"),
                    icon: "pi pi-phone",
                    key: "phone",
                },
            ];
        },
    },
    methods: {
        onPrincipalSelected(selectedPrincipal: Principal): void {
            this.$emit("update:principal", { ...selectedPrincipal });
        },
    },
};
</script>
