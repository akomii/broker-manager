<template>
  <div id="app" class="flex flex-column h-screen">
    <Header />
    <main class="p-input-filled my-2 border-solid border-200">
      <router-view></router-view>
    </main>
  </div>
</template>
          
<script setup lang="ts">
import Header from '@/components/Header.vue';
</script>
