<template>
    <span class="flex flex-wrap">
        <Chip
            v-for="tag in tags"
            :key="tag"
            class="py-1 px-2 m-1"
            :removable="editable"
            @remove="removeTag(tag)"
        >
            <i class="pi pi-tag mr-1" />
            <span class="text-sm"> {{ tag }} </span>
        </Chip>

        <div v-if="editable" class="p-input-icon-left">
            <i class="pi pi-tags" />
            <InputText
                class="w-9rem h-2rem"
                v-model="newTag"
                :placeholder="$t('newTagPlaceholder')"
                @keyup.enter="addTag"
            />
        </div>
    </span>
    <Toast :group="groupId" />
</template>

<script lang="ts">
import Chip from "primevue/chip";
import InputText from "primevue/inputtext";
import Toast from "primevue/toast";

/**
 * A Vue component for displaying and managing a list of tags as chips. The
 * component supports editable mode, allowing users to add new tags via an input
 * field, and remove existing tags.
 * ToDo add Toasts for removing and adding Tags?
 */
export default {
    components: {
        Chip,
        InputText,
        Toast,
    },
    props: {
        tags: {
            type: Array as () => string[],
            required: true,
        },
        editable: Boolean,
    },
    data() {
        return {
            newTag: "",
            groupId: "tags",
        };
    },
    emits: ["update:tags"],
    methods: {
        removeTag(tagToRemove: string) {
            const updatedTags = this.tags.filter((tag) => tag !== tagToRemove);
            this.$emit("update:tags", updatedTags);
        },
        addTag() {
            const tag = this.newTag.trim();
            if (tag && !this.tags.includes(tag)) {
                this.$emit("update:tags", [...this.tags, tag]);
                this.newTag = "";
            } else {
                this.$toast.add({
                    group: this.groupId,
                    severity: "warn",
                    detail: this.$t("tagExistsAlready", { tag }),
                    life: 3000,
                });
            }
        },
    },
};
</script>
