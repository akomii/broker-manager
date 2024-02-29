<template>
    <MenuButtonCommmon icon="pi pi-chevron-down" :menu="requestEditMenu" />
    <ConfirmDialog :group="groupId" />
    <Toast :group="groupId" />
</template>

<script lang="ts">
import ConfirmDialog from "primevue/confirmdialog";
import MenuButtonCommmon from "./MenuButtonCommon.vue";
import { RequestState } from "@/utils/Enums";
import { MenuItem } from "./MenuButtonCommon.vue";
import Toast from "primevue/toast";

/**
 * ToDo add Docs
 * ToDo move Toast to MenuButtonCommon?
 */
export default {
    components: {
        MenuButtonCommmon,
        ConfirmDialog,
        Toast,
    },
    props: {
        state: {
            type: String as () => keyof typeof RequestState,
            required: true,
        },
    },
    data() {
        return {
            requestEditMenu: [] as MenuItem[],
            groupId: "requestEditMenu",
        };
    },
    created() {
        this.requestEditMenu = this.buildRequestEditMenu();
    },
    methods: {
        buildRequestEditMenu(): MenuItem[] {
            const items = [];
            if (this.state === RequestState.DRAFT) {
                items.push({
                    label: this.$t("saveDraft"),
                    command: () => this.updateRequest(),
                });
            } else {
                items.push({
                    label: this.$t("saveRequest"),
                    command: () => this.updateRequest(),
                });
            }
            items.push({
                label: this.$t("cancel"),
                command: () => this.confirmCancel(),
            });
            return items;
        },

        updateRequest() {
            /**
             * call service
             */
            this.openToast("ToDo Routing to Keycloak User settings");
        },
        confirmCancel() {
            this.$confirm.require({
                group: this.groupId,
                message: this.$t("confirmCancelEdit"),
                header: this.$t("attention"),
                icon: "pi pi-exclamation-circle",
                rejectClass: "p-button-outlined",
                acceptLabel: this.$t("cancelEditing"),
                rejectLabel: this.$t("cancel"),
                accept: () => this.cancelEdit(),
            });
        },
        cancelEdit() {
            const parentPath = this.$route.path.replace(/\/edit$/, "");
            this.$router.push(parentPath).then(() => {
                this.$toast.add({
                    severity: "info",
                    detail: this.$t("cancelEditing"), // TypeError: this.$t is not a function
                    life: 3000,
                });
            });
        },
        openToast(toastMessage: string) {
            this.$toast.add({
                severity: "info",
                detail: toastMessage,
                life: 3000,
            });
        },
    },
};
</script>

<!--

RequestViewMenuButtonIT(requestState)

DRAFT

{ label: `${localize("menu.draft.publish")}` },
{ label: `${localize("menu.draft.duplicate")}` },
{ label: `${localize("menu.draft.delete")}` },
{ label: `${localize("menu.draft.edit")}` },

ONLINE + IT

{ label: `${localize("menu.request.results")}` },
{ label: `${localize("menu.request.duplicateAsDraft" )}`},
{ label: `${localize("menu.request.close")}` },
{ label: `${localize("menu.request.archive")}` },
{ label: `${localize("menu.request.edit")}` },

CLOSED + IT

{ label: `${localize("menu.request.results")}` },
{ label: `${localize("menu.request.duplicateAsDraft")}`},
{ label: `${localize("menu.request.archive")}` },
{ label: `${localize("menu.request.edit")}` },

ARCHIVED + IT

{ label: `${localize("menu.request.results")}` },
{ label: `${localize("menu.request.duplicateAsDraft")}`},



RequestViewMenuButtonDAC(requestState)

ONLINE + DAC

{ label: `${localize("menu.request.results")}` },
{ label: `${localize("menu.request.close")}` },
{ label: `${localize("menu.request.archive")}` },

CLOSED + DAC

{ label: `${localize("menu.request.results")}` },
{ label: `${localize("menu.request.archive")}` },

ARCHIVED + DAC

{ label: `${localize("menu.request.results")}` },




-->
