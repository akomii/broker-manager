<template>
    <Fieldset :legend="label" :class="fieldSetHeight">
        <ScrollPanel class="custom-scrollbar" :class="contentHeight">
            <p>{{ content }}</p>
        </ScrollPanel>
    </Fieldset>
</template>

<script lang="ts">
import Fieldset from "primevue/fieldset";
import ScrollPanel from "primevue/scrollpanel";
import TextFieldCommon from "./TextFieldCommon.vue";

// TODO refactor and add docs
export default {
    components: {
        Fieldset,
        ScrollPanel,
    },
    mixins: [TextFieldCommon],
};
</script>

<style scoped>
p {
    white-space: pre-wrap;
    text-align: justify;
}
</style>
