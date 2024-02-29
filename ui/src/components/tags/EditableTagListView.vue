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
            <Toast :group="groupId" />
        </div>
    </span>
</template>

<script lang="ts">
import Chip from "primevue/chip";
import InputText from "primevue/inputtext";
import Toast from "primevue/toast";

/**
 * ToDo add Toasts for removing and adding Tags?
 * A tag management component allowing for the display, addition, and removal of
 * tags. It supports editable mode, which enables adding new tags via an input
 * field and removing existing tags. Toast notifications are utilized to provide
 * feedback for tag addition and removal actions.
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
            groupId: "tags", // Toast group ID for notifications.
        };
    },
    emits: ["update:tags"],
    methods: {
        removeTag(tagToRemove: string) {
            const updatedTags = this.tags.filter((tag) => tag !== tagToRemove);
            this.$emit("update:tags", updatedTags);
        },
        /**
         * Adds a new tag if it does not already exist in the list. Triggers a
         * warning toast if the tag already exists.
         */
        addTag() {
            const tag = this.newTag.trim();
            if (tag) {
                if (!this.tags.includes(tag)) {
                    this.$emit("update:tags", [...this.tags, tag]);
                    this.newTag = "";
                } else {
                    this.$toast.add({
                        group: this.groupId,
                        severity: "warn",
                        detail: this.$t("tagExistsAlready", { tag: tag }),
                        life: 3000,
                    });
                }
            }
        },
    },
};
</script>
