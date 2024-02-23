<template>
    <HeaderCommon :id="id" :state="state" :menu="menu">
        <template #title>
            <p class="text-2xl">{{ title }}</p>
        </template>
        <template #tags>
            <EditableTagListView :tags="tags" />
        </template>
    </HeaderCommon>
</template>

<script lang="ts">
import EditableTagListView from "@/components/tags/EditableTagListView.vue";
import HeaderCommon from "./HeaderCommon.vue";

// TODO refactor and add docs
export default {
    components: {
        HeaderCommon,
        EditableTagListView,
    },
    mixins: [HeaderCommon],
    props: {
        title: {
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
