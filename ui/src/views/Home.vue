<template>
    <div>
        <Header />
        <main>
            <h2>Home Page</h2>
        </main>
        <Footer />
    </div>
</template>
  
<script setup lang="ts">
import { defineComponent } from 'vue';
import Header from './Header.vue';
import Footer from './Footer.vue';

export default defineComponent({});
</script>
