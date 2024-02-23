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

        <InputText
            class="w-12"
            v-model="newNote"
            :placeholder="$t('newNotePlaceholder')"
            @keyup.enter="addNote"
        />
    </Fieldset>
</template>

<script lang="ts">
import Fieldset from "primevue/fieldset";
import DataView from "primevue/dataview";
import Divider from "primevue/divider";
import InputText from "primevue/inputtext";
import { UserNote } from "@/utils/Types";
import DateView from "@/components/timeWidgets/DateView.vue";

// TODO refactor and add docs
export default {
    components: {
        Fieldset,
        DataView,
        Divider,
        InputText,
        DateView,
    },
    props: {
        notes: {
            type: Array as () => UserNote[],
            required: true,
        },
    },
    data() {
        return {
            newNote: "",
        };
    },
    methods: {
        addNote() {
            if (this.newNote) {
                this.notes.push({
                    date: new Date(),
                    content: this.newNote,
                });
                this.$emit("update:notes", this.notes);
                this.newNote = "";
            }
        },
    },
};
</script>
