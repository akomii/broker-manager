<template>
    <RequestHeaderCommon :id="id" :state="state" :menu="menu">
        <template v-slot:title>
            <p class="text-2xl">{{ title }}</p>
        </template>
        <template v-slot:tags>
            <EditableTagListView :tags="tags" />
        </template>
    </RequestHeaderCommon>
</template>

<script lang="ts">
import EditableTagListView from "@/components/tags/EditableTagListView.vue";
import RequestHeaderCommon from "./RequestHeaderCommon.vue";

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
