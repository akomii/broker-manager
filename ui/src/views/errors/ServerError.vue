<template>
    <ErrorCommon
        :code="'500'"
        :title="'Internal Server Error'"
        :kaomoji="'ヽ(￣д￣;)ノ=3=3=3'"
        :description="'Sorry, the server is currently having a bit of a meltdown'"
    />
</template>

<script lang="ts">
import ErrorCommon from "@/components/errors/ErrorCommon.vue";

export default {
    components: {
        ErrorCommon,
    },
};
</script>
