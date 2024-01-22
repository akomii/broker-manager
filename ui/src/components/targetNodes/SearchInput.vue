<template>
    <span class="p-input-icon-right">
        <InputText
            @input="updateInput"
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
    methods: {
        updateInput(event: Event) {
            this.$emit(
                "update:input",
                (event.target as HTMLInputElement).value
            );
        },
    },
};
</script>
