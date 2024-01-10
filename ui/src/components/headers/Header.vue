<template>
    <div class="mx-2">
        <div class="flex flex-wrap justify-content-between">
            <div
                class="flex flex-wrap justify-content-start align-items-center"
            >
                <div class="mx-2">
                    <GoBackButton />
                </div>

                <!-- Title Section -->
                <div
                    class="flex flex-wrap justify-content-start align-items-center"
                >
                    <p v-if="id !== undefined" class="text-2xl mx-2">
                        [{{ id }}]
                    </p>
                    <template v-if="editable && state === 'DRAFT'">
                        <span class="p-float-label">
                            <InputText
                                size="large"
                                class="flex w-30rem text-2xl"
                                v-model="localTitle"
                            />
                            <label>Titel</label>
                        </span>
                    </template>
                    <p v-else class="text-2xl mx-1">{{ title }}</p>
                </div>

                <!-- Request State -->
                <div class="mx-2">
                    <EnumState class="text-lg" :state="state" :stateColorMap="requestStateColorMap" />
                </div>

                <!-- Tags Section -->
                <!-- TODO ARCHIVED CANNOT BE CHANGED ANYMORE-->
                <div class="flex flex-wrap max-w-30rem mx-2">
                    <TagList
                        :tags="editable ? localTags : tags"
                        :removable="editable"
                        @update:tags="localTags = $event"
                    />
                    <TagCreator
                        v-if="editable"
                        :tags="localTags"
                        @update:tags="localTags = $event"
                    />
                </div>
            </div>

            <!-- Menu Button -->
            <div class="flex flex-wrap justify-content-end align-items-center">
                <div class="mx-2">
                    <div v-if="editable">
                        <MenuButton
                            :icon="'pi pi-chevron-down'"
                            :menu="
                                state === 'DRAFT'
                                    ? editDraftMenu
                                    : editRequestMenu
                            "
                        />
                    </div>
                    <div v-else>
                        <MenuButton
                            :icon="'pi pi-chevron-down'"
                            :menu="
                                state === 'DRAFT'
                                    ? draftMenu
                                    : hasUserRoleIT
                                    ? requestMenuIT
                                    : requestMenuDAC
                            "
                        />
                    </div>
                </div>
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
import MenuButton from "@/components/buttons/MenuButton.vue";
import GoBackButton from "@/components/buttons/GoBackButton.vue";
import EnumState from "@/components/states/EnumState.vue";
import { requestStateColorMap } from "@/utils/Constants.ts";
import { UserRole } from "@/utils/Enums.ts";

export default {
    components: {
        Divider,
        InputText,
        TagList,
        TagCreator,
        MenuButton,
        GoBackButton,
        EnumState,
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
            type: String,
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
                { label: "Draft als Anfrage veröffentlichen" },
                { label: "Draft duplizieren" },
                { label: "Draft löschen" },
                { label: "Draft bearbeiten" },
            ],
            editDraftMenu: [
                { label: "Draft speichern" },
                { label: "Abbrechen" },
            ],
            requestMenuIT: [
                { label: "Ergebnisübersicht" },
                { label: "Als Draft duplizieren" },
                { label: "Anfrage schließen" },
                { label: "Anfrage archivieren" },
                { label: "Anfrage bearbeiten" },
            ],
            requestMenuDAC: [
                { label: "Ergebnisübersicht" },
                { label: "Anfrage schließen" },
                { label: "Anfrage archivieren" },
            ],
            editRequestMenu: [
                { label: "Anfrage speichern" },
                { label: "Abbrechen" },
            ],
            localTitle: this.title,
            localTags: this.tags,
            userRole: UserRole.IT, // TODO: grab userRole from Keycloak response
        };
    },
    computed: {
        hasUserRoleIT() {
            return this.userRole === UserRole.IT;
        },
    },
    watch: {
        localTitle(newTitle) {
            this.$emit("update:title", newTitle);
        },
        localTags(updatedTags) {
            this.$emit("update:tags", updatedTags);
        },
    },
};
</script>
