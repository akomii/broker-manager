<template>
    <HeaderCommon :id="id" :state="state" :menu="menu">
        <template #title>
            <span class="p-float-label w-9">
                <InputText
                    class="text-2xl w-12"
                    size="large"
                    v-model="dummyTitle"
                    :disabled="isTitleEditingDisabled"
                />
                <label>{{ $t("title") }}</label>
            </span>
        </template>
        <template #tags>
            <EditableTagListView
                :tags="dummyTags"
                :editable="!isTagEditingDisabled"
                @update:tags="dummyTags = $event"
            />
        </template>
    </HeaderCommon>
</template>

<script lang="ts">
import InputText from "primevue/inputtext";
import EditableTagListView from "@/components/tags/EditableTagListView.vue";
import HeaderCommon from "./HeaderCommon.vue";

// TODO refactor and add docs
export default {
    components: {
        HeaderCommon,
        InputText,
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
        isTitleEditingDisabled: {
            type: Boolean,
            default: false,
        },
        isTagEditingDisabled: {
            type: Boolean,
            default: false,
        },
    },
    data() {
        return {
            dummyTitle: this.title,
            dummyTags: this.tags,
        };
    },
    watch: {
        dummyTitle: function (updatedTitle: string) {
            this.$emit("update:title", updatedTitle);
        },
        dummyTags: function (updatedTags: string[]) {
            this.$emit("update:tags", updatedTags);
        },
    },
};
</script>
