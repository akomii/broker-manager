<template>
    <Button :icon="icon" @click="toggleMenu" />
    <Menu ref="menu" :model="menu" :popup="true" class="mt-2" />
</template>

<script lang="ts">
import { PropType } from "vue";
import Button from "primevue/button";
import Menu from "primevue/menu";

export interface MenuItem {
    label: string;
    icon?: string;
    command?: () => void;
    items?: MenuItem[];
}

const isValidMenuItem = (item: MenuItem): boolean => {
    return (
        item.hasOwnProperty("label") &&
        (!item.icon || typeof item.icon === "string") &&
        (!item.command || typeof item.command === "function") &&
        (!item.items ||
            (Array.isArray(item.items) && item.items.every(isValidMenuItem)))
    );
};

export default {
    components: {
        Button,
        Menu,
    },
    props: {
        icon: {
            type: String,
            required: true,
        },
        menu: {
            type: Array as PropType<MenuItem[]>,
            required: true,
            validator: (menuItems: MenuItem[]) =>
                menuItems.every(isValidMenuItem),
        },
    },
    methods: {
        toggleMenu(event: MouseEvent) {
            const menu = this.$refs.menu as Menu;
            if (menu) {
                menu.toggle(event);
            }
        },
    },
};
</script>
