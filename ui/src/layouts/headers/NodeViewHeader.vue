<template>
    <HeaderCommon :id="id" :title="title" :tags="tags">
        <template #menuButtonSection>
            <MenuButton v-if="menu" :icon="'pi pi-chevron-down'" :menu="menu" />
        </template>
    </HeaderCommon>
</template>

<script lang="ts">
import { PropType } from "vue";
import HeaderCommon from "./HeaderCommon.vue";
import MenuButton from "@/components/buttons/MenuButton.vue";
import { MenuItem } from "@/components/buttons/MenuButton.vue";

// TODO refactor and add docs
export default {
    components: {
        HeaderCommon,
        MenuButton,
    },
    mixins: [HeaderCommon],
    props: {
        menu: {
            type: Array as PropType<MenuItem[]>,
        },
    },
};
</script>
