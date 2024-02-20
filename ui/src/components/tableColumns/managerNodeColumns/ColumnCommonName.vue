<template>
    <Column :header="$t('node')" sortable>
        <template #body="slotProps">
            <div class="flex gap-1">
                <span>
                    {{ slotProps.data.clientDN.CN }}
                </span>
                <HiddenNodeIcon v-if="!slotProps.data.apiKey" />
            </div>
        </template>
    </Column>
</template>

<script lang="ts">
import Column from "primevue/column";
import HiddenNodeIcon from "@/components/icons/HiddenNodeIcon.vue";

export default {
    components: {
        Column,
        HiddenNodeIcon,
    },
};
</script>
