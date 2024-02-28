<template>
    <div class="flex flex-wrap justify-content-between mx-3">
        <div class="flex flex-wrap align-items-center gap-2 w-11">
            <GoBackButton />
            <p v-if="id" class="text-2xl">[{{ id }}]</p>
            <BlockUI
                v-if="editable"
                :blocked="isTitleEditingDisabled"
                class="w-5"
            >
                <span class="p-float-label text-2xl">
                    <InputText
                        class="w-full"
                        size="large"
                        :modelValue="title"
                        @update:modelValue="updateTitle"
                    />
                    <label>{{ $t("title") }}</label>
                </span>
            </BlockUI>
            <p v-else class="text-2xl">{{ title }}</p>
            <slot name="enumLabelSection"></slot>
            <BlockUI
                v-if="editable"
                :blocked="isTagEditingDisabled"
                class="w-5"
            >
                <EditableTagListView
                    :tags="tags"
                    :editable="true"
                    @update:tags="updateTags"
                />
            </BlockUI>
            <EditableTagListView v-else :tags="tags" />
        </div>
        <div class="flex flex-wrap align-items-center">
            <slot name="menuButtonSection"></slot>
        </div>
    </div>
</template>

<script lang="ts">
import InputText from "primevue/inputtext";
import BlockUI from "primevue/blockui";
import Divider from "primevue/divider";
import GoBackButton from "@/components/buttons/GoBackButton.vue";
import EditableTagListView from "@/components/tags/EditableTagListView.vue";

export default {
    components: {
        InputText,
        BlockUI,
        Divider,
        GoBackButton,
        EditableTagListView,
    },
    props: {
        id: Number,
        title: {
            type: String,
            required: true,
        },
        tags: {
            type: Array<string>,
            required: true,
        },
        editable: Boolean,
        isTitleEditingDisabled: Boolean,
        isTagEditingDisabled: Boolean,
    },
    emits: ["update:title", "update:tags"],
    methods: {
        updateTitle(title: string) {
            this.$emit("update:title", title);
        },
        updateTags(tags: string[]) {
            this.$emit("update:tags", tags);
        },
    },
};
</script>
