<template>
    <Button
        icon="pi pi-external-link"
        :label="$t('requestHistory')"
        outlined
        @click="showDialog = true"
    />
    <Dialog
        v-model:visible="showDialog"
        modal
        :dismissableMask="true"
        :closable="false"
        class="w-8"
    >
        <template #header>
            <span class="text-xl font-bold">{{ $t("requestHistory") }}</span>
            <span class="col" />
            <Button
                icon="pi pi-times"
                severity="primary"
                outlined
                @click="showDialog = false"
            />
        </template>
        <Accordion :multiple="true" :activeIndex="[0]">
            <AccordionTab
                v-for="historyItem in history"
                :key="historyItem.user"
            >
                <template #header>
                    <p>
                        <span class="font-bold">{{
                            formatDate(historyItem.date)
                        }}</span>
                        {{
                            $t("byUser", {
                                user: historyItem.user,
                            })
                        }}
                    </p>
                </template>
                {{ historyItem }}
            </AccordionTab>
        </Accordion>
    </Dialog>
</template>

<script lang="ts">
import Button from "primevue/button";
import Dialog from "primevue/dialog";
import Accordion from "primevue/accordion";
import AccordionTab from "primevue/accordiontab";
import MomentWrapper from "@/utils/MomentWrapper";
import { ModificationHistoryItem } from "@/utils/Types";

export default {
    components: {
        Button,
        Dialog,
        Accordion,
        AccordionTab,
    },
    props: {
        history: {
            type: Array as () => ModificationHistoryItem[],
            required: true,
        },
    },
    data() {
        return {
            showDialog: false,
        };
    },
    methods: {
        formatDate(date: Date): string {
            return MomentWrapper.formatDateToGermanLocale(date);
        },
    },
};
</script>
