<template>
    <div
        class="flex flex-wrap justify-content-between border-solid border-200 py-1"
    >
        <div
            class="flex align-items-center mx-2 gap-1"
            style="user-select: none"
        >
            <img src="@/assets/aktin_logo.png" class="max-h-4rem" />
            <h2 class="text-700">Broker Manager</h2>
        </div>
        <div class="flex align-items-center mx-2 gap-1">
            <div v-if="hasUserRoleIT" class="flex">
                <TabMenu :model="routing">
                    <template #item="{ item, props }">
                        <router-link v-slot="{ navigate }" :to="item.route">
                            <a v-bind="props.action" @click="navigate">
                                {{ item.label }}
                            </a>
                        </router-link>
                    </template>
                </TabMenu>
                <Divider layout="vertical" />
            </div>
            <MenuButton icon="pi pi-user" :menu="userMenu" />
        </div>
    </div>
</template>

<script lang="ts">
import TabMenu from "primevue/tabmenu";
import Divider from "primevue/divider";
import MenuButton from "@/components/buttons/MenuButton.vue";
import MenuService from "@/services/MenuService";
import LoggedInUserService from "@/services/LoggedInUserService";
import { UserRole } from "@/utils/Enums.ts";
import { MenuItem } from "@/services/MenuService";

/**
 * Helper interface for defining navigation route configurations. Only used in
 * this component.
 */
interface RouteConfig {
    label: string;
    route: string;
}

/**
 * Header Component for Broker Manager App.
 *
 * Renders the header with the app logo, title, user menu, and IT-specific
 * navigation tabs. The user menu adjusts based on user role, supporting
 * settings and logout functionality.
 */
export default {
    components: {
        TabMenu,
        Divider,
        MenuButton,
    },
    data() {
        return {
            userMenu: [] as MenuItem[],
            routing: [] as RouteConfig[],
        };
    },
    created() {
        this.initComponent();
    },
    computed: {
        hasUserRoleIT(): boolean {
            return LoggedInUserService.hasRole(UserRole.IT);
        },
    },
    methods: {
        initComponent() {
            const username = LoggedInUserService.getUsername();
            this.userMenu = MenuService.getUserMenu(this.$t, username);
            this.routing = this.getNavigationTabRouting();
        },
        getNavigationTabRouting(): RouteConfig[] {
            return [
                {
                    label: this.$t("navigation.requests"),
                    route: "/requests",
                },
                { label: this.$t("navigation.nodes"), route: "/nodes" },
            ];
        },
    },
};
</script>
