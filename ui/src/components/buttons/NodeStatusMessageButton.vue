<template>
    <Button
        v-if="statusMessage"
        @click="showStatusMessage()"
        icon="pi pi-exclamation-circle text-xl text-blue-600"
        v-tooltip.bottom="$t('showStatusMessage')"
        text
        rounded
    />
</template>

<script lang="ts">
import Button from "primevue/button";

// TODO remove v-if and put on parent or similar
export default {
    components: {
        Button,
    },
    props: {
        statusMessage: {
            type: String as () => string,
            required: true,
        },
    },
    methods: {
        showStatusMessage() {
            if (this.statusMessage) {
                const newWindow = window.open("", "_blank");
                if (newWindow) {
                    newWindow.document.write(
                        `<pre>${this.statusMessage}</pre>`
                    );
                    newWindow.document.title = this.$t("statusMessage");
                    newWindow.document.close();
                }
            }
        },
    },
};
</script>
