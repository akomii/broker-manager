<template>
    <Chip
        v-for="tag in tags"
        :key="tag"
        class="py-1 px-2 m-1"
        :removable="editable"
        @remove="removeTag(tag)"
    >
        <span class="pi pi-tag"></span>
        <span class="ml-1 text-sm"> {{ tag }} </span>
    </Chip>

    <span v-if="editable" class="p-input-icon-left">
        <Toast />
        <i class="pi pi-tags" />
        <InputText
            class="w-10rem h-2rem"
            v-model="newTag"
            :placeholder="$t('newTagPlaceholder')"
            @keyup.enter="addTag"
        />
    </span>
</template>

<script lang="ts">
import Chip from "primevue/chip";
import InputText from "primevue/inputtext";
import Toast from "primevue/toast";

export default {
    components: {
        Chip,
        InputText,
        Toast,
    },
    props: {
        tags: {
            type: Array<string>,
            required: true,
        },
        editable: {
            type: Boolean,
            default: false,
        },
    },
    data() {
        return {
            newTag: "",
        };
    },
    methods: {
        removeTag(tagToRemove: string) {
            const updatedTags = this.tags.filter((tag) => tag !== tagToRemove);
            this.$emit("update:tags", updatedTags);
        },
        addTag() {
            const tag = this.newTag.trim();
            if (tag) {
                if (this.tags.includes(tag)) {
                    this.$toast.add({
                        severity: "warn",
                        detail: this.$t("tagExistsAlready", { tag: tag }),
                        life: 3000,
                    });
                } else {
                    this.tags.push(tag);
                    this.$emit("update:tags", this.tags);
                }
                this.newTag = "";
            }
        },
    },
};
</script>
