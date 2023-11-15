<template>
  <div id="app">
    <Header />
    <main class="p-input-filled">
      <router-view></router-view>
    </main>
  </div>
</template>
          
<script setup lang="ts">
import Header from '@/components/Header.vue';
</script>

<style scoped>
main {
  margin: 0 1%;
}
</style>
