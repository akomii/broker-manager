<template>
    <Column field="sequenceId" :header="$t('sequenceId')" sortable>
        <template #body="slotProps">
            <div class="flex gap-1">
                <span>
                    {{ slotProps.data.sequenceId }}
                </span>
            </div>
        </template>
    </Column>
</template>

<script lang="ts">
import Column from "primevue/column";

export default {
    components: {
        Column,
    },
};
</script>
