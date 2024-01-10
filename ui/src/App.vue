<template>
    <div id="app" class="flex flex-column h-screen">
        <ApplicationHeader />
        <main class="p-input-filled my-2 border-solid border-200">
            <router-view></router-view>
        </main>
    </div>
</template>

<script setup lang="ts">
import ApplicationHeader from "@/components/headers/ApplicationHeader.vue";
</script>
