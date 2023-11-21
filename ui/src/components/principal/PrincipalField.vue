<template>
    <div>
        <template v-if="editable">
            <InputGroup class="mb-2 h-3rem">
                <InputGroupAddon>
                    <i :class="iconClass" class="text-xl"></i>
                </InputGroupAddon>
                <span class="p-float-label">
                    <InputText :id="fieldId" size="small" class="flex w-18rem" :modelValue="value"/>
                    <label :for="fieldId">{{ label }}</label>
                </span>
            </InputGroup>
        </template>

        <template v-else>
            <div class="field grid m-0">
                <label :for="fieldId" class="col-fixed w-4rem">
                    <i :class="iconClass" class="text-xl border-round surface-200 p-2 text-color-secondary"></i>
                </label>
                <div class="col pt-2" :id="fieldId">
                    {{ value }}
                </div>
            </div>
        </template>
    </div>
</template>

<script lang="ts">
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';
import InputText from 'primevue/inputtext';

export default {
    components: {
        InputGroup,
        InputGroupAddon,
        InputText,
    },
    props: {
        label: { type: String, required: true, },
        icon: { type: String, required: true, },
        value: { type: String, required: true, },
        editable: { type: Boolean, default: false, },
    },
    computed: {
        fieldId() { return this.label.toLowerCase(); },
        iconClass() { return this.icon; }
    },

};
</script>
  