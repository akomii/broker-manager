<template>
    <span class="p-input-icon-right">
        <InputText @input="onInput" placeholder="Suche..." class="w-15rem" />
        <i class="pi pi-search" />
    </span>
</template>

<script lang="ts">
import InputText from 'primevue/inputtext';

export default {
    components: {
        InputText
    },
    methods: {
        onInput(event) {
            this.$emit('inputChange', event.target.value);
        }
    }
};
</script>
