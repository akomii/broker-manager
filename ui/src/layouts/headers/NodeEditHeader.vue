<template>
    <NodeHeaderCommon :id="id" :menu="menu">
        <template #title>
            <span class="p-float-label w-9">
                <InputText
                    class="text-2xl w-12"
                    size="large"
                    v-model="dummyCommonName"
                />
                <label>{{ $t("title") }}</label>
            </span>
        </template>
        <template #tags>
            <EditableTagListView
                :tags="dummyTags"
                :editable="true"
                @update:tags="dummyTags = $event"
            />
        </template>
    </NodeHeaderCommon>
</template>

<!-- TODO menu logic to Header -->
<script lang="ts">
import InputText from "primevue/inputtext";
import EditableTagListView from "@/components/tags/EditableTagListView.vue";
import NodeHeaderCommon from "./NodeHeaderCommon.vue";

// TODO refactor and add docs
export default {
    components: {
        NodeHeaderCommon,
        InputText,
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
    data() {
        return {
            dummyCommonName: this.commonName,
            dummyTags: this.tags,
        };
    },
    watch: {
        dummycommonName: function (updatedCommonName: string) {
            this.$emit("update:commonName", updatedCommonName);
        },
        dummyTags: function (updatedTags: string[]) {
            this.$emit("update:tags", updatedTags);
        },
    },
};
</script>
