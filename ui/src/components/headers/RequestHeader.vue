<template>
    <div class="mx-2">
        <div class="flex justify-content-between">
            <div class="flex flex-wrap align-items-center">
                <GoBackButton class="mx-2" />

                <!-- TODO set somehow min-width to content and max-widht to 41.6667% -->
                <div class="flex align-items-center w-5">
                    <p v-if="id" class="text-2xl mx-2">[{{ id }}]</p>
                    <template v-if="editable && isDraft()">
                        <span class="p-float-label">
                            <!-- TODO increase text width-->
                            <InputText
                                size="large"
                                class="text-2xl"
                                v-model="localTitle"
                            />
                            <label>{{ $t("requestHeader.title") }}</label>
                        </span>
                    </template>
                    <p v-else class="text-2xl mx-1">{{ title }}</p>
                </div>

                <RequestStateLabel class="text-lg mx-1" :state="state" />

                <div class="flex flex-wrap w-5">
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
            </div>

            <div class="flex align-items-center">
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
                { label: this.$t("requestHeader.menu.draft.publish") },
                { label: this.$t("requestHeader.menu.draft.duplicate") },
                { label: this.$t("requestHeader.menu.draft.delete") },
                { label: this.$t("requestHeader.menu.draft.edit") },
            ],
            editDraftMenu: [
                { label: this.$t("requestHeader.menu.draft.save") },
                { label: this.$t("requestHeader.menu.cancel") },
            ],
            requestMenuIT: [
                { label: this.$t("requestHeader.menu.request.results") },
                { label: this.$t("requestHeader.menu.request.duplicateDraft") },
                { label: this.$t("requestHeader.menu.request.close") },
                { label: this.$t("requestHeader.menu.request.archive") },
                { label: this.$t("requestHeader.menu.request.edit") },
            ],
            requestMenuDAC: [
                { label: this.$t("requestHeader.menu.request.results") },
                { label: this.$t("requestHeader.menu.request.close") },
                { label: this.$t("requestHeader.menu.request.archive") },
            ],
            editRequestMenu: [
                { label: this.$t("requestHeader.menu.request.save") },
                { label: this.$t("requestHeader.menu.cancel") },
            ],
            localTitle: this.title,
            localTags: this.tags,
            userRole: UserRole.IT, // TODO: grab userRole from Keycloak response
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
        hasUserRoleIT(): boolean {
            return this.userRole === UserRole.IT;
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
