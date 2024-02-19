<template>
    <RequestHeaderCommon :id="id" :state="state" :menu="menu">
        <template #title>
            <p class="text-2xl">{{ title }}</p>
        </template>
        <template #tags>
            <EditableTagListView :tags="tags" />
        </template>
    </RequestHeaderCommon>
</template>

<script lang="ts">
import EditableTagListView from "@/components/tags/EditableTagListView.vue";
import RequestHeaderCommon from "@/layouts/headers/RequestHeaderCommon.vue";

export default {
    components: {
        RequestHeaderCommon,
        EditableTagListView,
    },
    mixins: [RequestHeaderCommon],
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
