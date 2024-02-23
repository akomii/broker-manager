<template>
    <div class="flex text-sm w-12">
        <InputGroup>
            <template v-for="periodUnit in periodUnits">
                <FloatLabel v-if="periodUnit.show" :key="periodUnit.id">
                    <InputNumber
                        :id="periodUnit.id"
                        v-model="periodUnit.value"
                        :min="periodUnit.min"
                        :max="periodUnit.max"
                        @update:modelValue="
                            updatePeriod(periodUnit.name, periodUnit.value)
                        "
                        :disabled="disabled"
                    />
                    <label :for="periodUnit.id">{{ periodUnit.label }}</label>
                </FloatLabel>
            </template>
        </InputGroup>
    </div>
</template>

<script lang="ts">
import InputGroup from "primevue/inputgroup";
import InputNumber from "primevue/inputnumber";
import FloatLabel from "primevue/floatlabel";
import MomentWrapper from "@/utils/MomentWrapper";
import { MomentDuration } from "@/utils/MomentWrapper";

// TODO refactor and add docs
export default {
    components: {
        InputGroup,
        InputNumber,
        FloatLabel,
    },
    props: {
        period: {
            type: Object as () => MomentDuration,
            required: true,
        },
        disabled: {
            type: Boolean,
            default: false,
        },
        showYears: {
            type: Boolean,
            default: true,
        },
        showMonths: {
            type: Boolean,
            default: true,
        },
        showDays: {
            type: Boolean,
            default: true,
        },
        showHours: {
            type: Boolean,
            default: true,
        },
    },
    computed: {
        periodUnits() {
            return [
                {
                    name: "years",
                    id: "year-input",
                    value: this.period?.years(),
                    min: 0,
                    max: 10,
                    label: this.$t("years"),
                    show: this.showYears,
                },
                {
                    name: "months",
                    id: "month-input",
                    value: this.period?.months(),
                    min: 0,
                    max: 12,
                    label: this.$t("months"),
                    show: this.showMonths,
                },
                {
                    name: "days",
                    id: "day-input",
                    value: this.period?.days(),
                    min: 0,
                    max: 31,
                    label: this.$t("days"),
                    show: this.showDays,
                },
                {
                    name: "hours",
                    id: "hour-input",
                    value: this.period?.hours(),
                    min: 0,
                    max: 24,
                    label: this.$t("hours"),
                    show: this.showHours,
                },
            ];
        },
    },
    methods: {
        updatePeriod(periodUnitName: String, newValue: Number) {
            const clonedPeriod = {
                years: this.period.years(),
                months: this.period.months(),
                days: this.period.days(),
                hours: this.period.hours(),
            };
            clonedPeriod[periodUnitName] = newValue;
            const updatedPeriod = MomentWrapper.createDuration(clonedPeriod);
            this.$emit("update:period", updatedPeriod);
        },
    },
};
</script>
