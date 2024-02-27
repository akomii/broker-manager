<template>
    <MenuButton icon="pi pi-user" :menu="userMenu" />
    <ConfirmDialog :group="groupId" />
    <Toast :group="groupId" />
</template>

<script lang="ts">
import ConfirmDialog from "primevue/confirmdialog";
import Toast from "primevue/toast";
import MenuButton from "./MenuButtonCommon.vue";
import LoggedInUserService from "@/services/LoggedInUserService";
import { MenuItem } from "./MenuButtonCommon.vue";

/**
 * ToDo update Doc when finished ToDos
 * A Vue component that integrates a user menu with logout and settings options
 * and confirmation dialog. It dynamically builds the user menu based on the
 * logged-in user's name, provides a mechanism for user logout with
 * confirmation, and displays toast messages for actions. The component utilizes
 * PrimeVue components and a custom MenuButton component for the menu display.
 */
export default {
    components: {
        MenuButton,
        ConfirmDialog,
        Toast,
    },
    data() {
        return {
            userMenu: [] as MenuItem[],
            groupId: "usermenu",
        };
    },
    created() {
        const username = LoggedInUserService.getUsername();
        this.userMenu = this.buildUserMenu(username);
    },
    methods: {
        buildUserMenu(username: string): MenuItem[] {
            return [
                {
                    label: username,
                    items: [
                        {
                            label: this.$t("settings"),
                            icon: "pi pi-cog",
                            command: () => this.redirectToUserSettings(),
                        },
                        {
                            label: this.$t("logout"),
                            icon: "pi pi-sign-out",
                            command: () => this.confirmLogout(),
                        },
                    ],
                },
            ];
        },
        redirectToUserSettings() {
            /**
             * ToDo add routing to Keycloak user settings
             */
            this.openToast("ToDo Routing to Keycloak User settings");
        },
        confirmLogout() {
            this.$confirm.require({
                group: this.groupId,
                message: this.$t("confirmUserLogout"),
                header: this.$t("confirmation"),
                icon: "pi pi-exclamation-circle",
                rejectClass: "p-button-secondary p-button-outlined",
                acceptLabel: this.$t("logout"),
                rejectLabel: this.$t("cancel"),
                accept: () => this.logoutUser(),
            });
        },
        logoutUser() {
            /**
             * ToDo add logout functionality
             */
            this.openToast("ToDo logout from Keycloak");
        },
        openToast(toastMessage: string) {
            this.$toast.add({
                group: this.groupId,
                severity: "contrast",
                detail: toastMessage,
                life: 3000,
            });
        },
    },
};
</script>
