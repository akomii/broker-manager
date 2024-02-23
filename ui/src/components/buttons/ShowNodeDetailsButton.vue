<template>
    <Button
        icon="pi pi-chevron-right"
        outlined
        v-tooltip.bottom="$t('goToDetailsViewOfNode')"
        @click="goToNodeDetails"
    />
</template>

<script lang="ts">
import Button from "primevue/button";
import { useRouter } from "vue-router";

// TODO generify
// TODO refactor and add docs
export default {
    components: {
        Button,
    },
    props: {
        nodeId: {
            type: Number,
            required: true,
        },
    },
    setup(props) {
        const router = useRouter();
        const goToNodeDetails = () => {
            router.push({ path: `/nodes/${props.nodeId}` });
        };
        return { goToNodeDetails };
    },
};
</script>
