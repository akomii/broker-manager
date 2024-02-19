<template>
    <Button
        icon="pi pi-chevron-left text-xl"
        outlined
        v-tooltip.bottom="$t('back')"
        @click="goBack"
    />
</template>

<script lang="ts">
import Button from "primevue/button";
import { useRouter } from "vue-router";

export default {
    components: {
        Button,
    },
    setup() {
        const router = useRouter();
        function goBack() {
            router.go(-1);
        }
        return { goBack };
    },
};
</script>
