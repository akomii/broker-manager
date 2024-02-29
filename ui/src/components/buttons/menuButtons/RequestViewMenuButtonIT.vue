<template>
    <MenuButtonCommmon icon="pi pi-chevron-down" :menu="requestViewMenu" />
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
            requestViewMenu: [] as MenuItem[],
            groupId: "requestViewMenuIT",
        };
    },
    created() {
        this.requestViewMenu = this.buildRequestViewMenu();
    },
    methods: {
        buildRequestViewMenu(): MenuItem[] {
            if (this.state === RequestState.DRAFT) {
                return [
                    { label: this.$t("menu.draft.publish") },
                    { label: this.$t("menu.draft.duplicate") },
                    { label: this.$t("menu.draft.delete") },
                    { label: this.$t("menu.draft.edit") },
                ];
            } else if (this.state === RequestState.ONLINE) {
                return [
                    { label: this.$t("menu.request.results") },
                    { label: this.$t("menu.request.duplicateAsDraft") },
                    { label: this.$t("menu.request.close") },
                    { label: this.$t("menu.request.archive") },
                    { label: this.$t("menu.request.edit") },
                ];
            } else if (this.state === RequestState.CLOSED) {
                return [
                    { label: this.$t("menu.request.results") },
                    { label: this.$t("menu.request.duplicateAsDraft") },
                    { label: this.$t("menu.request.archive") },
                    { label: this.$t("menu.request.edit") },
                ];
            } else if (this.state === RequestState.ARCHIVED) {
                return [
                    { label: this.$t("menu.request.results") },
                    { label: this.$t("menu.request.duplicateAsDraft") },
                ];
            } else {
                return [];
            }
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
