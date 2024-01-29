<template>
    <div class="mx-3">
        <div class="grid align-items-center">
            <div class="col">
                <GoBackButton />
            </div>
            <!-- TODO RequestStatelabel is placed under Tags when window too small-->
            <!-- TODO clear empty space -->
            <div class="col-5 flex align-items-center">
                <p v-if="id" class="text-2xl mx-2">[{{ id }}]</p>
                <slot name="title"></slot>
                <RequestStateLabel class="text-lg mx-2" :state="state" />
            </div>
            <div class="col-5">
                <slot name="tags"></slot>
            </div>
            <div class="col flex justify-content-end">
                <MenuButton
                    v-if="menu"
                    :icon="'pi pi-chevron-down'"
                    :menu="menu"
                />
            </div>
        </div>
        <Divider class="mt-0" />
    </div>
</template>

<script lang="ts">
import { PropType } from "vue";
import Divider from "primevue/divider";
import RequestStateLabel from "@/components/labels/RequestStateLabel.vue";
import { RequestState } from "@/utils/Enums";
import GoBackButton from "./GoBackButton.vue";
import MenuButton from "./MenuButton.vue";
import { MenuItem } from "./MenuButton.vue";

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
            type: String as () => keyof typeof RequestState,
            required: true,
        },
        menu: {
            type: Array as PropType<MenuItem[]>,
        },
    },
};
</script>
