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

// TODO refactor and add docs
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
            this.$emit("update:search", newValue);
        },
    },
};
</script>
