<template>
    <Button
        @click="showStatusMessage()"
        icon="pi pi-exclamation-circle text-xl text-blue-600"
        text
        rounded
    />
</template>

<script lang="ts">
import Button from "primevue/button";

// TODO add Hover info
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
                    newWindow.document.title = this.$t("nodeStatusMessage", {
                        nodeId: "'TODOOOOO'",
                    });
                    newWindow.document.close();
                }
            }
        },
    },
};
</script>
