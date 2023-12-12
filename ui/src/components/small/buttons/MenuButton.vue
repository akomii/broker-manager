<template>
    <Button :icon="icon" @click="toggle"/>
    <Menu ref="menu" :model="menu" :popup="true" class="mt-1"/>
</template>

<script lang="ts">
import { PropType, Vue } from 'vue';
import Button from 'primevue/button';
import Menu from 'primevue/menu';

interface MenuItem {
    label: string;
    icon?: string;
    command?: () => void;
    items?: MenuItem[];
}

export default {
    components: {
        Button,
        Menu
    },
    props: {
        icon: {
            type: String,
            required: true
        },
        menu: {
            type: Array as PropType<MenuItem[]>,
            required: true,
            validator: (menuItems: MenuItem[]) => {
                const isValidMenuItem = (item: MenuItem): boolean => {
                    if (!item.hasOwnProperty('label')) return false;
                    if (item.icon && typeof item.icon !== 'string') return false;
                    if (item.command && typeof item.command !== 'function') return false;
                    if (item.items) {
                        if (!Array.isArray(item.items)) return false;
                        return item.items.every(isValidMenuItem);
                    }
                    return true;
                };
                return menuItems.every(isValidMenuItem);
            }
        },
    },
    methods: {
        toggle(event: MouseEvent) {
            const menu = this.$refs.menu as Vue;
            menu.toggle(event);
        }
    }
};
</script>
