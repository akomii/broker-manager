<template>
    <NodeHeaderCommon :id="id" :menu="menu">
        <template #title>
            <p class="text-2xl">{{ commonName }}</p>
        </template>
        <template #tags>
            <EditableTagListView :tags="tags" />
        </template>
    </NodeHeaderCommon>
</template>

<!-- TODO menu logic to Header -->
<script lang="ts">
import EditableTagListView from "@/components/tags/EditableTagListView.vue";
import NodeHeaderCommon from "./NodeHeaderCommon.vue";

export default {
    components: {
        NodeHeaderCommon,
        EditableTagListView,
    },
    mixins: [NodeHeaderCommon],
    props: {
        commonName: {
            type: String,
            required: true,
        },
        tags: {
            type: Array<string>,
            required: true,
        },
    },
};
</script>
