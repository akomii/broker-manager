<template>
  <div id="app">
    <Header />
    <main>
      <h2>Home Page</h2>
    </main>
    <Footer />
  </div>

    <div class="card flex justify-content-center">
        <Button label="Check" icon="pi pi-check" />
    </div>


<i class="pi pi-check" style="font-size: 1rem"></i>
<i class="pi pi-times" style="font-size: 1.5rem"></i>
<i class="pi pi-search" style="font-size: 2rem"></i>
<i class="pi pi-user" style="font-size: 2.5rem"></i>

</template>
          
<script setup lang="ts">
import Header from './components/Header.vue';
import Footer from './components/Footer.vue';
</script>
        