<template>
    <div>
        <Button
            :icon="buttonIcon"
            :label="buttonLabel"
            outlined
            @click="toggleDialog"
        />
        <Dialog
            v-model:visible="showDialog"
            modal
            :dismissableMask="true"
            :closable="false"
            class="w-8"
        >
            <template #header>
                <h2 class="text-xl font-bold">{{ dialogTitle }}</h2>
                <span class="col" />
                <Button
                    icon="pi pi-times"
                    severity="primary"
                    outlined
                    @click="toggleDialog"
                />
            </template>
            <slot name="dialog-body"></slot>
        </Dialog>
    </div>
</template>

<script lang="ts">
import Button from "primevue/button";
import Dialog from "primevue/dialog";

export default {
    components: {
        Button,
        Dialog,
    },
    props: {
        buttonIcon: String,
        buttonLabel: String,
        dialogTitle: {
            type: String,
            required: true,
        },
    },
    data() {
        return {
            showDialog: false,
        };
    },
    methods: {
        toggleDialog() {
            this.showDialog = !this.showDialog;
        },
    },
};
</script>
