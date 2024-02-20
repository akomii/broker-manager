<template>{{ nodes }}</template>

<script lang="ts">
import { ManagerNode } from "@/util/Types.ts";

export default {
    components: {},
    props: {
        nodes: {
            type: Array as () => ManagerNode[],
            required: true,
        },
    },
    data() {
        return {};
    },
};
</script>
