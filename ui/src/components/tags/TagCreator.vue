<template>
    <Toast />
    <span class="p-input-icon-left">
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
import InputText from "primevue/inputtext";
import Toast from "primevue/toast";

export default {
    components: {
        InputText,
        Toast,
    },
    props: {
        tags: {
            type: Set<string>,
            required: true,
        },
    },
    data() {
        return {
            newTag: "",
        };
    },
    methods: {
        addTag(): void {
            const tag = this.newTag.trim();
            if (tag) {
                const updatedTags = new Set(this.tags);
                if (updatedTags.has(tag)) {
                    this.$toast.add({
                        severity: "info",
                        summary: "Info",
                        detail: this.$t("tagExistsAlready", { tag: tag }),
                        life: 3000,
                    });
                } else {
                    updatedTags.add(tag);
                    this.$emit("update:tags", updatedTags);
                }
                this.newTag = "";
            }
        },
    },
};
</script>
