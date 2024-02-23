<template>
    <Button
        v-tooltip.bottom="toolTipLabel"
        @click="confirmDelete"
        severity="danger"
        icon="pi pi-trash"
        outlined
    />
    <ConfirmPopup :group="groupId" />
    <Toast :group="groupId" />
</template>

<script lang="ts">
import Button from "primevue/button";
import ConfirmPopup from "primevue/confirmpopup";
import Toast from "primevue/toast";

// TODO refactor and add docs
export default {
    components: {
        Button,
        ConfirmPopup,
        Toast,
    },
    props: {
        itemId: {
            type: Number,
            required: true,
        },
        toolTipLabel: String,
        popupMessage: {
            type: String,
            required: true,
        },
        toastMessage: {
            type: String,
            required: true,
        },
    },
    data() {
        return {
            groupId: "deleteConfirmation" + this.itemId.toString(),
        };
    },
    emits: ["delete"],
    methods: {
        confirmDelete(event: any) {
            this.$confirm.require({
                target: event.currentTarget,
                group: this.groupId,
                message: this.popupMessage,
                icon: "pi pi-info-circle",
                rejectClass: "p-button-outlined p-button-sm",
                acceptClass: "p-button-danger p-button-sm",
                rejectLabel: this.$t("cancel"),
                acceptLabel: this.$t("delete"),
                accept: () => this.deleteItem(),
            });
        },
        deleteItem() {
            this.$emit("delete", this.itemId);
            this.$toast.add({
                group: this.groupId,
                severity: "success",
                detail: this.toastMessage,
                life: 3000,
            });
        },
    },
};
</script>
