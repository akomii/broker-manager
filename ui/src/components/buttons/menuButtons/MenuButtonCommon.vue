<template>
    <Button
        :label="label"
        :icon="icon"
        @click="toggleMenu"
        :outlined="outlined"
    />
    <Menu ref="menu" :model="menu" :popup="true" class="mt-2" />
</template>

<script lang="ts">
import { PropType } from "vue";
import Button from "primevue/button";
import Menu from "primevue/menu";

/**
 * Represents a menu item for use in a hierarchical menu structure, allowing for
 * complex menu creation with nested submenus. Each item can have a label, an
 * optional icon, an optional command function to execute on click, and an
 * optional array of nested `MenuItem` objects for creating submenus.
 */
export interface MenuItem {
    label: string;
    icon?: string;
    command?: () => void;
    items?: MenuItem[];
}

/**
 * ToDo add Docs
 */
export default {
    components: {
        Button,
        Menu,
    },
    props: {
        label: String,
        icon: String,
        menu: {
            type: Array as PropType<MenuItem[]>,
            required: true,
            validator: (menuItems: MenuItem[]): boolean => {
                return menuItems.every((item) => {
                    return (
                        typeof item.label === "string" &&
                        (!item.icon || typeof item.icon === "string") &&
                        (!item.command || typeof item.command === "function") &&
                        (!item.items ||
                            item.items.every(
                                (subItem) =>
                                    typeof subItem === "object" && subItem.label
                            ))
                    );
                });
            },
        },
        outlined: Boolean,
    },
    methods: {
        toggleMenu(event: any) {
            const menu = this.$refs.menu as Menu;
            menu.toggle(event);
        },
    },
};
</script>
