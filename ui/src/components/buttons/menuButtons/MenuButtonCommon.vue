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
 * complex menu creation with nested submenus. Each item can include a label, an
 * optional icon, an optional command function to be executed on click, and an
 * optional array of nested `MenuItem` objects for creating submenus.
 *
 * @interface @property {string} label - The text displayed for the menu item.
 * @property {string}
 * [icon] - Optional. The name of the icon to be displayed alongside the label.
 * @property {() => void}
 * [command] - Optional. A function to be executed when the menu item is clicked.
 * @property
 * {MenuItem[]} [items] - Optional. An array of `MenuItem` objects representing nested submenus.
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
