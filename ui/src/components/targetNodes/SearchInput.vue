<template>
    <span class="p-input-icon-right">
        <InputText
            v-model="inputValue"
            :placeholder="$t('searchPlaceholder')"
        />
        <i class="pi pi-search" />
    </span>
</template>

<script lang="ts">
import InputText from "primevue/inputtext";

export default {
    components: {
        InputText,
    },
    data() {
        return {
            inputValue: "",
        };
    },
    watch: {
        inputValue(newValue: string) {
            this.$emit("update:input", newValue);
        },
    },
};
</script>
