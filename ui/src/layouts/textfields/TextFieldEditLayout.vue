<template>
    <Fieldset :legend="label" :class="fieldSetHeight">
        <!-- TODO SQL Validator -->
        <!-- TODO Text nur ASCII -->
        <!-- TODO BlockUI instead of disabled -->
        <Textarea
            v-model="contentDummy"
            @input="updateContent"
            class="w-full"
            :class="contentHeight"
            :disabled="disabled"
        />
    </Fieldset>
</template>

<script lang="ts">
import Fieldset from "primevue/fieldset";
import Textarea from "primevue/textarea";
import TextFieldCommon from "./TextFieldCommon.vue";

// TODO refactor and add docs
export default {
    components: {
        Fieldset,
        Textarea,
    },
    mixins: [TextFieldCommon],
    props: {
        disabled: {
            type: Boolean,
            default: false,
        },
    },
    data() {
        return {
            contentDummy: this.content as string,
        };
    },
    methods: {
        updateContent() {
            this.$emit("update:content", this.contentDummy);
        },
    },
};
</script>
