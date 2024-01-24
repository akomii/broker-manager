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
            type: Array<string>,
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
                if (this.tags.includes(tag)) {
                    this.$toast.add({
                        severity: "info",
                        summary: "Info",
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
