<template>
    <div class="flex justify-content-end w-full">
        <span class="p-input-icon-right">
            <InputText v-model="localSearchTerm" @input="onSearchTermChange" placeholder="Suche..." class="w-15rem mb-2" />
            <i class="pi pi-search" />
        </span>
    </div>
</template>
  
<script lang="ts">
import InputText from 'primevue/inputtext';

export default {
    components: {
        InputText
    },
    props: {
        searchTerm: { type: String, required: true },
        filterMethod: { type: Function, required: true },
    },
    data() {
        return { localSearchTerm: this.searchTerm };
    },
    methods: {
        onSearchTermChange() {
            this.filterMethod(this.localSearchTerm);
        }
    }
};
</script>
