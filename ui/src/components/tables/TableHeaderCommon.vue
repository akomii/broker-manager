<template>
    <div class="flex flex-wrap justify-content-between">
        <div class="flex flex-wrap align-items-center gap-3">
            <h2 class="text-700">{{ title }}</h2>
            <div>
                <Checkbox
                    v-if="showCheckBox1"
                    v-model="toggle1"
                    :binary="true"
                    inputId="checkBox1"
                    @update:modelValue="emitToggle1"
                />
                <label for="checkBox1" class="ml-2">
                    {{ checkBox1Title }}
                </label>
            </div>
            <div>
                <Checkbox
                    v-if="showCheckBox2"
                    v-model="toggle2"
                    :binary="true"
                    inputId="checkBox2"
                    @update:modelValue="emitToggle2"
                />
                <label for="checkBox2" class="ml-2">
                    {{ checkBox2Title }}
                </label>
            </div>
        </div>
        <div class="flex flex-wrap align-items-center gap-3">
            <SearchInput @update:search="$emit('search', $event)" />
            <slot name="new-item-actions"></slot>
        </div>
    </div>
</template>

<script lang="ts">
import Checkbox from "primevue/checkbox";
import SearchInput from "@/components/tables/SearchInput.vue";

// TODO refactor and add docs
export default {
    components: {
        Checkbox,
        SearchInput,
    },
    props: {
        title: String,
        showCheckBox1: Boolean,
        showCheckBox2: Boolean,
        checkBox1Title: String,
        checkBox2Title: String,
    },
    data() {
        return {
            toggle1: false,
            toggle2: false,
        };
    },
    methods: {
        emitToggle1(value: boolean) {
            this.$emit("toggle:checkBox1", value);
        },
        emitToggle2(value: boolean) {
            this.$emit("toggle:checkBox2", value);
        },
    },
};
</script>
