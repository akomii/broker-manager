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
            <ApplicationHeaderMenuButton />
        </div>
    </div>
</template>

<script lang="ts">
import TabMenu from "primevue/tabmenu";
import Divider from "primevue/divider";
import ApplicationHeaderMenuButton from "@/components/buttons/menuButtons/ApplicationHeaderMenuButton.vue";
import LoggedInUserService from "@/services/LoggedInUserService";
import { UserRole } from "@/utils/Enums.ts";

/**
 * Helper interface for defining navigation route configurations. Only used in
 * this component.
 */
interface RouteConfig {
    label: string;
    route: string;
}

/**
 * The Header Component for the Broker Manager application, designed to display
 * the application's logo, title, and a dynamic navigation menu. It includes
 * IT-specific navigation tabs that are conditionally rendered based on the
 * user's role, leveraging the TabMenu component for tabbed navigation.
 * Additionally, it features an ApplicationHeaderMenuButton for user-related
 * actions. The component dynamically constructs navigation routes for IT roles,
 * ensuring relevant options are accessible based on user permissions.
 */
export default {
    components: {
        TabMenu,
        Divider,
        ApplicationHeaderMenuButton,
    },
    data() {
        return {
            routing: [] as RouteConfig[],
        };
    },
    created() {
        this.routing = this.getNavigationTabRouting();
    },
    computed: {
        hasUserRoleIT(): boolean {
            return LoggedInUserService.hasRole(UserRole.IT);
        },
    },
    methods: {
        getNavigationTabRouting(): RouteConfig[] {
            return [
                {
                    label: this.$t("requests"),
                    route: "/requests",
                },
                { label: this.$t("nodes"), route: "/nodes" },
            ];
        },
    },
};
</script>
