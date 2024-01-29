<template>
    <RequestHeaderCommon :id="id" :state="state" :menu="menu">
        <template v-slot:title>
            <p v-if="isTitleEditingDisabled" class="text-2xl">
                {{ title }}
            </p>
            <span v-else class="p-float-label">
                <!-- TODO increase text width-->
                <InputText size="large" class="text-2xl" v-model="dummyTitle" />
                <label>{{ $t("title") }}</label>
            </span>
        </template>
        <template v-slot:tags>
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
        dummyTitle(updatedTitle: string) {
            this.$emit("update:title", updatedTitle);
        },
        dummyTags(updatedTags: Set<string>) {
            this.$emit("update:tags", updatedTags);
        },
    },
};
</script>
