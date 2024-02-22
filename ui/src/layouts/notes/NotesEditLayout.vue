<template>
    <!-- TODO add fixed height-->
    <Fieldset :legend="$t('notes')">
        <DataView :value="notes" dataKey="date">
            <template #list="slotProps">
                <div class="grid grid-nogutter">
                    <div
                        v-for="(item, index) in slotProps.items"
                        :key="index"
                        class="col-12"
                    >
                        <div
                            class="flex flex-row justify-content-between align-items-center flex-1"
                        >
                            <span>
                                <div>
                                    <DateView
                                        class="text-lg font-medium"
                                        :date="item.date"
                                    />
                                </div>
                                <p
                                    class="text-sm font-medium text-justify px-3"
                                >
                                    {{ item.content }}
                                </p>
                            </span>

                            <div class="flex align-items-end">
                                <Button
                                    v-tooltip.bottom="$t('deleteThisNote')"
                                    @click="deleteNote($event, index)"
                                    severity="danger"
                                    icon="pi pi-trash"
                                    outlined
                                />
                            </div>
                        </div>
                        <Divider />
                    </div>
                </div>
                <ConfirmPopup></ConfirmPopup>
            </template>
            <Toast></Toast>

            <template #empty>
                <p class="flex justify-content-center">
                    {{ $t("noNotesFound") }}
                </p>
            </template>
        </DataView>
    </Fieldset>
</template>

<script lang="ts">
import Fieldset from "primevue/fieldset";
import DataView from "primevue/dataview";
import Divider from "primevue/divider";
import { UserNote } from "@/utils/Types";
import Button from "primevue/button";
import DateView from "@/components/timeWidgets/DateView.vue";
import Toast from "primevue/toast";
import ConfirmPopup from "primevue/confirmpopup";

export default {
    components: {
        Fieldset,
        DataView,
        Divider,
        DateView,
        Button,
        Toast,
        ConfirmPopup,
    },
    props: {
        notes: {
            type: Array as () => UserNote[],
            required: true,
        },
    },
    methods: {
        deleteNote(event, index: number) {
            this.$confirm.require({
                target: event.currentTarget,
                message: this.$t("doYouWantToDeleteThisNote"),
                icon: "pi pi-info-circle",
                rejectClass: "p-button-outlined p-button-sm",
                acceptClass: "p-button-danger p-button-sm",
                rejectLabel: this.$t("cancel"),
                acceptLabel: this.$t("delete"),
                accept: () => {
                    this.notes.splice(index, 1);
                    this.$emit("update:notes", this.notes);
                    this.$toast.add({
                        severity: "success",
                        detail: this.$t("noteWasDeleted"),
                        life: 3000,
                    });
                },
            });
        },
    },
};
</script>
