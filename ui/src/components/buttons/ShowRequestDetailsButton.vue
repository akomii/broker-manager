<template>
    <Button
        icon="pi pi-chevron-right"
        outlined
        v-tooltip.bottom="$t('goToDetailsViewOfRequest')"
        @click="goToRequestDetails"
    />
</template>

<script lang="ts">
import Button from "primevue/button";
import { useRouter } from "vue-router";

export default {
    components: {
        Button,
    },
    props: {
        requestId: {
            type: Number,
            required: true,
        },
    },
    setup(props) {
        const router = useRouter();
        const goToRequestDetails = () => {
            router.push({ path: `/requests/${props.requestId}` });
        };
        return { goToRequestDetails };
    },
};
</script>
