<template>
    <Fieldset :legend="label" :class="fieldSetHeight">
        <template v-if="editable && isDraft()">
            <Textarea
                v-model="contentDummy"
                @input="updateContent"
                class="w-full"
                :class="scrollPanelHeight"
            />
        </template>
        <template v-else>
            <ScrollPanel class="custom-scrollbar" :class="scrollPanelHeight">
                <p>{{ content }}</p>
            </ScrollPanel>
        </template>
    </Fieldset>

    <!-- TODO SQL Validator -->
    <!-- TODO Text nur ASCII -->
</template>

<script lang="ts">
import Fieldset from "primevue/fieldset";
import Textarea from "primevue/textarea";
import ScrollPanel from "primevue/scrollpanel";
import { RequestState } from "@/utils/Enums";

export default {
    components: {
        Fieldset,
        ScrollPanel,
        Textarea,
    },
    props: {
        content: {
            type: String as () => string,
            required: true,
        },
        label: {
            type: String as () => string,
            required: true,
        },
        fieldSetHeight: {
            type: String as () => string,
            default: "h-22rem",
        },
        state: {
            type: String as () => keyof typeof RequestState,
            required: true,
        },
        editable: {
            type: Boolean,
            default: false,
        },
    },
    computed: {
        scrollPanelHeight() {
            const heightNumber = Number(
                this.fieldSetHeight.replace("h-", "").replace("rem", "")
            );
            return `h-${heightNumber - 5}rem`;
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
        isDraft(): boolean {
            return this.state === RequestState.DRAFT;
        },
    },
};
</script>

<style scoped>
p {
    white-space: pre-wrap;
    text-align: justify;
}
</style>
