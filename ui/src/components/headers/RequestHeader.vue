<template>
    <div class="mx-2">
        <div class="grid align-items-center">
            <div class="col">
                <GoBackButton class="mx-2" />
            </div>
            <!-- TODO clear empty space -->
            <div class="col-5 flex align-items-center">
                <p v-if="id" class="text-2xl mx-2">[{{ id }}]</p>
                <template v-if="editable && isDraft()">
                    <span class="p-float-label">
                        <!-- TODO increase text width-->
                        <InputText
                            size="large"
                            class="text-2xl"
                            v-model="localTitle"
                        />
                        <label>{{ $t("title") }}</label>
                    </span>
                </template>
                <p v-else class="text-2xl mx-1">{{ title }}</p>
                <RequestStateLabel class="text-lg mx-1" :state="state" />
            </div>

            <div class="col-5">
                <TagList
                    :tags="isEditableAndNotArchived() ? localTags : tags"
                    :removable="isEditableAndNotArchived()"
                    @update:tags="localTags = $event"
                />
                <TagCreator
                    v-if="isEditableAndNotArchived()"
                    :tags="localTags"
                    @update:tags="localTags = $event"
                />
            </div>

            <div class="col flex justify-content-end">
                <MenuButton :icon="'pi pi-chevron-down'" :menu="getMenu()" />
            </div>
        </div>
        <Divider class="mt-0" />
    </div>
</template>

<script lang="ts">
import Divider from "primevue/divider";
import InputText from "primevue/inputtext";
import TagList from "@/components/tags/TagList.vue";
import TagCreator from "@/components/tags/TagCreator.vue";
import MenuButton from "./MenuButton.vue";
import GoBackButton from "./GoBackButton.vue";
import RequestStateLabel from "@/components/states/RequestStateLabel.vue";
import { UserRole, RequestState } from "@/utils/Enums.ts";

export default {
    components: {
        Divider,
        InputText,
        TagList,
        TagCreator,
        MenuButton,
        GoBackButton,
        RequestStateLabel,
    },
    props: {
        id: {
            type: Number,
            required: true,
        },
        title: {
            type: String,
            required: true,
        },
        state: {
            type: String as () => keyof typeof RequestState,
            required: true,
        },
        tags: {
            type: Set<string>,
            required: true,
        },
        editable: {
            type: Boolean,
            default: false,
        },
    },
    data() {
        return {
            draftMenu: [
                // TODO: add routing and services
                { label: this.$t("menu.draft.publish") },
                { label: this.$t("menu.draft.duplicate") },
                { label: this.$t("menu.draft.delete") },
                { label: this.$t("menu.draft.edit") },
            ],
            editDraftMenu: [
                { label: this.$t("menu.draft.save") },
                { label: this.$t("menu.cancel") },
            ],
            requestMenuIT: [
                { label: this.$t("menu.request.results") },
                { label: this.$t("menu.request.duplicateDraft") },
                { label: this.$t("menu.request.close") },
                { label: this.$t("menu.request.archive") },
                { label: this.$t("menu.request.edit") },
            ],
            requestMenuDAC: [
                { label: this.$t("menu.request.results") },
                { label: this.$t("menu.request.close") },
                { label: this.$t("menu.request.archive") },
            ],
            editRequestMenu: [
                { label: this.$t("menu.request.save") },
                { label: this.$t("menu.cancel") },
            ],
            localTitle: this.title,
            localTags: this.tags,
        };
    },
    methods: {
        getMenu(): Array<any> {
            if (this.editable) {
                return this.isDraft()
                    ? this.editDraftMenu
                    : this.editRequestMenu;
            } else {
                return this.isDraft()
                    ? this.draftMenu
                    : this.hasUserRoleIT()
                    ? this.requestMenuIT
                    : this.requestMenuDAC;
            }
        },
        isDraft(): boolean {
            return this.state === RequestState.DRAFT;
        },
        isEditableAndNotArchived(): boolean {
            return this.editable && this.state != RequestState.ARCHIVED;
        },
        // TODO: grab userRole from Keycloak response
        hasUserRoleIT(): boolean {
            return this.$userRole === UserRole.IT;
        },
    },
    watch: {
        localTitle(newTitle: string): void {
            this.$emit("update:title", newTitle);
        },
        localTags(updatedTags: Set<string>): void {
            this.$emit("update:tags", updatedTags);
        },
    },
};
</script>
