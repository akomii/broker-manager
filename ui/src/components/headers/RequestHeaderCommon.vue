<template>
    <div class="mx-3 flex flex-wrap align-items-center">
        <div class="w-6 flex flex-wrap align-items-center column-gap-2">
            <GoBackButton />
            <p v-if="id" class="text-2xl">[{{ id }}]</p>
            <slot name="title"></slot>
            <RequestStateLabel class="text-lg" :state="state" />
        </div>
        <div class="w-5">
            <slot name="tags"></slot>
        </div>
        <div class="w-1 flex flex-wrap justify-content-end">
            <MenuButton v-if="menu" :icon="'pi pi-chevron-down'" :menu="menu" />
        </div>
    </div>
    <Divider class="mt-0" />
</template>

<script lang="ts">
import { PropType } from "vue";
import Divider from "primevue/divider";
import RequestStateLabel from "@/components/labels/RequestStateLabel.vue";
import { RequestState } from "@/utils/Enums";
import GoBackButton from "./GoBackButton.vue";
import MenuButton from "@/components/buttons/MenuButton.vue";
import { MenuItem } from "@/components/buttons/MenuButton.vue";

export default {
    components: {
        Divider,
        MenuButton,
        GoBackButton,
        RequestStateLabel,
    },
    props: {
        id: {
            type: Number,
            required: true,
        },
        state: {
            type: String as PropType<keyof typeof RequestState>,
            required: true,
        },
        menu: {
            type: Array as PropType<MenuItem[]>,
        },
    },
};
</script>
