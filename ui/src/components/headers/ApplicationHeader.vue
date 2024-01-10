<template>
    <div
        class="flex justify-content-between flex-wrap border-solid border-200 p-3"
    >
        <div class="flex align-items-center">
            <img src="@/assets/aktin_logo.png" class="max-h-4rem mr-2" />
            <h2 class="text-700">Broker Manager</h2>
        </div>
        <div class="flex align-items-center">
            <div v-if="hasUserRoleIT" class="flex">
                <TabMenu :model="routing" class="mr-2" />
                <Divider layout="vertical" class="ml-2" />
            </div>
            <MenuButton :icon="'pi pi-user'" :menu="usermenu" />
        </div>
    </div>
</template>

<script lang="ts">
import TabMenu from "primevue/tabmenu";
import Divider from "primevue/divider";
import MenuButton from "@/components/buttons/MenuButton.vue";
import { UserRole } from "@/utils/Enums.ts";

export default {
    components: {
        TabMenu,
        Divider,
        MenuButton,
    },
    data() {
        return {
            usermenu: [
                {
                    label: "<username>",
                    items: [
                        {
                            label: "Settings",
                            icon: "pi pi-cog",
                        },
                        {
                            label: "Logout",
                            icon: "pi pi-sign-out",
                        },
                    ],
                },
            ],
            routing: [
                { label: "Anfragen", to: "/requests" },
                { label: "Kliniken", to: "/clinics" },
            ],
            userRole: UserRole.IT, // TODO: grab userRole from Keycloak response
        };
    },
    computed: {
        hasUserRoleIT() {
            return this.userRole === UserRole.IT;
        },
    },
};
</script>
