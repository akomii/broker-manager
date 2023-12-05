<template>
    <Chip class="py-1 px-2 m-1">
        <span class="pi pi-tag"></span>
        <span class="ml-1 text-sm">
            <slot></slot>
        </span>
    </Chip>
</template>

<script lang="ts">
import Chip from 'primevue/chip';

export default {
    components: {
        Chip,
    },
}
</script>
