<template>
    <RequestHeaderCommon :id="id" :state="state" :menu="menu">
        <template #title>
            <p v-if="isTitleEditingDisabled" class="text-2xl">
                {{ title }}
            </p>
            <span v-else class="p-float-label w-9">
                <InputText
                    class="text-2xl w-12"
                    size="large"
                    v-model="dummyTitle"
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
    </RequestHeaderCommon>
</template>

<script lang="ts">
import InputText from "primevue/inputtext";
import EditableTagListView from "@/components/tags/EditableTagListView.vue";
import RequestHeaderCommon from "./RequestHeaderCommon.vue";

export default {
    components: {
        RequestHeaderCommon,
        InputText,
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
        isTitleEditingDisabled: {
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
