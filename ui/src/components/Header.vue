<template>
    <div class="card">
        <Menubar>
            <template #start>
                <div class="logo-container">
                    <img src="../assets/aktin_logo.png" class="logo-image" />
                    <h2>Broker Manager</h2>
                </div>
            </template>

            <template #end>
                <div class="menu-container">
                    <TabMenu :model="routing" class="tab-menu" />
                    <Divider layout="vertical" class="divider" />
                    <div class="user-menu">
                        <Button type="button" icon="pi pi-user" @click="toggle" aria-haspopup="true"
                            aria-controls="overlay_menu" />
                        <Menu ref="menu" id="overlay_menu" :model="usermenu" :popup="true" />
                    </div>
                </div>
            </template>
        </Menubar>
    </div>
</template>

<script lang="ts">
import { Vue } from 'vue';
import Menubar from 'primevue/menubar';
import TabMenu from 'primevue/tabmenu';
import Divider from 'primevue/divider';
import Button from 'primevue/button';
import Menu from 'primevue/menu';

export default {
    components: {
        Menubar,
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
            ]
        };
    },
    methods: {
        toggle(event: MouseEvent) {
            const menu = this.$refs.menu as Vue;
            menu.toggle(event);
        }
    }
};
</script>

<style scoped>
.logo-container {
    display: flex;
    align-items: center;
}

.logo-image {
    max-height: 60px;
    margin-right: 10px;
}

.menu-container {
    display: flex;
    align-items: center;
}

.tab-menu {
    margin-right: 10px;
}

.divider {
    margin: 0 15px;
}

.user-menu {
    display: flex;
    align-items: center;
}
</style>
