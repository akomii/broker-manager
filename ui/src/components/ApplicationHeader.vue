<template>
    <div class="flex justify-content-between flex-wrap border-solid border-200 p-3">
        <div class="flex align-items-center">
            <img src="@/assets/aktin_logo.png" class="max-h-4rem mr-2" />
            <h2 class="text-700">Broker Manager</h2>
        </div>
        <div class="flex align-items-center">
            <div v-if="isTabMenuShown" class="flex">
                <TabMenu :model="routing" class="mr-2" />
                <Divider layout="vertical" class="ml-2" />
            </div>
            <div>
                <Button type="button" icon="pi pi-user" @click="toggle" aria-haspopup="true" aria-controls="overlay_menu" />
                <Menu ref="menu" id="overlay_menu" :model="usermenu" :popup="true" />
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { Vue } from 'vue';
import TabMenu from 'primevue/tabmenu';
import Divider from 'primevue/divider';
import Button from 'primevue/button';
import Menu from 'primevue/menu';
import  { UserRole } from '@/utils/Enums.ts';

export default {
    components: {
        TabMenu,
        Divider,
        Button,
        Menu
    },
    data() {
        return {
            usermenu: [
                {
                    label: '<username>',
                    items: [
                        { label: 'Settings' },
                        { label: 'Logout' },
                    ]
                }
            ],
            routing: [
                { label: 'Anfragen', to: '/requests' },
                { label: 'Kliniken', to: '/clinics' },
            ],
            userRole: UserRole.IT // TODO: grab userRole from Keycloak response
        };
    },
    computed: {
        isTabMenuShown() {
            return this.userRole === UserRole.IT;
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
