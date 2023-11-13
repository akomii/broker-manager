<template>
  <div id="app">
    <Header />
    <main>
      <Card>
        <template #content>
          <router-view></router-view>
        </template>
      </Card>
    </main>
  </div>
</template>
          
<script setup lang="ts">
import Header from './components/Header.vue';
import Card from 'primevue/card';
</script>
