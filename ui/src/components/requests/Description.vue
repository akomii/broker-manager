<template>
    <div>
        <Fieldset
            legend="Beschreibung"
            :toggleable="true"
            class="flex w-auto m-2"
        >
            <template v-if="editable">
                <Textarea
                    class="h-30rem"
                    v-model="localModelValue"
                    @input="updateModelValue"
                />
            </template>
            <template v-else>
                <ScrollPanel class="max-h-30rem">
                    <p>{{ localModelValue }}</p>
                </ScrollPanel>
            </template>
        </Fieldset>
    </div>
    <!-- TODO Textarea widht -->
    <!-- TODO Text nur ASCII -->
</template>

<script lang="ts">
import Fieldset from "primevue/fieldset";
import Textarea from "primevue/textarea";
import ScrollPanel from "primevue/scrollpanel";

export default {
    components: {
        Fieldset,
        ScrollPanel,
        Textarea,
    },
    props: {
        modelValue: { type: String as () => string, required: true },
        editable: { type: Boolean, default: false },
    },
    data() {
        return {
            localModelValue: this.modelValue as string,
        };
    },
    watch: {
        modelValue: {
            immediate: true,
            handler(newValue) {
                this.localModelValue = newValue;
            },
        },
    },
    methods: {
        updateModelValue() {
            this.$emit("update:modelValue", this.localModelValue);
        },
    },
};
</script>

<style scoped>
p {
    white-space: pre-wrap;
    text-align: justify;
}

.p-inputtextarea {
    min-width: 370%;
    max-width: 785%;
}
</style>
