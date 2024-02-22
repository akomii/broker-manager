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
                                <DeleteListItemButton
                                    :itemId="index"
                                    :toolTipLabel="$t('deleteThisNote')"
                                    :popupMessage="
                                        $t('doYouWantToDeleteThisNote')
                                    "
                                    :toastMessage="$t('noteWasDeleted')"
                                    @delete="deleteNote"
                                />
                            </div>
                        </div>
                        <Divider />
                    </div>
                </div>
            </template>

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
import DateView from "@/components/timeWidgets/DateView.vue";
import DeleteListItemButton from "@/components/buttons/DeleteListItemButton.vue";

export default {
    components: {
        Fieldset,
        DataView,
        Divider,
        DateView,
        DeleteListItemButton,
    },
    props: {
        notes: {
            type: Array as () => UserNote[],
            required: true,
        },
    },
    methods: {
        deleteNote(index: Number) {
            this.notes.splice(index, 1);
            this.$emit("update:notes", this.notes);
        },
    },
};
</script>
