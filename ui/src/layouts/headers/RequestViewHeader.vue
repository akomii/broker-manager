<template>
    <HeaderCommon :id="id" :title="title" :tags="tags">
        <template #enumLabelSection>
            <RequestStateLabel class="text-lg" :state="state" />
        </template>
        <template #menuButtonSection>
            <MenuButton v-if="menu" :icon="'pi pi-chevron-down'" :menu="menu" />
        </template>
    </HeaderCommon>
</template>

<script lang="ts">
import { PropType } from "vue";
import HeaderCommon from "./HeaderCommon.vue";
import RequestStateLabel from "@/components/labels/RequestStateLabel.vue";
import MenuButton from "@/components/buttons/MenuButton.vue";
import { RequestState } from "@/utils/Enums";
import { MenuItem } from "@/components/buttons/MenuButton.vue";

// TODO refactor and add docs
export default {
    components: {
        HeaderCommon,
        RequestStateLabel,
        MenuButton,
    },
    mixins: [HeaderCommon],
    props: {
        state: {
            type: String as PropType<keyof typeof RequestState>,
            required: true,
        },
        menu: {
            type: Array as PropType<MenuItem[]>,
            required: true,
        },
    },
};
</script>
