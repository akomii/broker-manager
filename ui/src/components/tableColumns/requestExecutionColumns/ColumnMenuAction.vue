<template>
    <Column bodyStyle="text-align: center">
        <template #body="slotProps">
            <MenuButton
                :icon="'pi pi-ellipsis-v'"
                :outlined="true"
                :menu="menuData(slotProps.data.executionState)"
            />
        </template>
    </Column>
</template>

<script lang="ts">
import Column from "primevue/column";
import MenuButton from "@/components/buttons/MenuButton.vue";

// TODO refactor and add docs
export default {
    components: {
        Column,
        MenuButton,
    },
    props: {
        menuData: {
            type: Function,
            required: true,
        },
    },
};
</script>
