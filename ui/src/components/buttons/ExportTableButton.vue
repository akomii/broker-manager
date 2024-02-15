<template>
    <DownloadButton
        :tooltipLabel="$t('exportCurrentTable')"
        :action="exportCSV"
    />
</template>

<script lang="ts">
import DownloadButton from "@/components/buttons/DownloadButton.vue";

// TODO nested table components are not exported correctly
export default {
    components: {
        DownloadButton,
    },
    props: {
        dt: {
            type: Object, //must be a primvevue datatable
            required: true,
        },
    },
    methods: {
        exportCSV() {
            this.dt.exportCSV();
        },
    },
};
</script>
