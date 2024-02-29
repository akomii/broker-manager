<template>
    <div class="flex text-sm">
        <BlockUI :blocked="disabled">
            <InputGroup>
                <template v-for="periodUnit in periodUnits">
                    <FloatLabel v-if="periodUnit.show" :key="periodUnit.id">
                        <InputNumber
                            :id="periodUnit.id"
                            :modelValue="periodUnit.value"
                            :min="periodUnit.min"
                            :max="periodUnit.max"
                            @update:modelValue="
                                (value) => updatePeriod(periodUnit.name, value)
                            "
                        />
                        <label :for="periodUnit.id">{{
                            periodUnit.label
                        }}</label>
                    </FloatLabel>
                </template>
            </InputGroup>
        </BlockUI>
    </div>
</template>

<script lang="ts">
import BlockUI from "primevue/blockui";
import InputGroup from "primevue/inputgroup";
import FloatLabel from "primevue/floatlabel";
import InputNumber from "primevue/inputnumber";
import MomentWrapper from "@/utils/MomentWrapper";
import { MomentDuration } from "@/utils/MomentWrapper";

/**
 * A Vue component to display and update time periods using input fields. It
 * allows adjusting years, months, days, and hours with validation.
 */
export default {
    components: {
        BlockUI,
        InputGroup,
        FloatLabel,
        InputNumber,
    },
    props: {
        /**
         * The current period to be modified, represented as a MomentDuration.
         * If not available, a default MomentDuration (with all zeros) is
         * displayed.
         */
        period: {
            type: Object as () => MomentDuration | null | undefined,
            default: () => null,
        },
        /**
         * Indicates if the UI should be disabled.
         */
        disabled: Boolean,
        /**
         * Visibility toggles for each time unit input.
         */
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
    emits: ["update:period"],
    computed: {
        /**
         * Computes period units to be displayed based on visibility toggles and
         * the period prop. Handles `null` or `undefined` period by defaulting
         * unit values to 0.
         * @returns {Array} The array of period units with their configuration,
         * including value, min, max, and visibility.
         */
        periodUnits() {
            const units = ["years", "months", "days", "hours"];
            const visibility = [
                "showYears",
                "showMonths",
                "showDays",
                "showHours",
            ];
            return units.map((unit, index) => ({
                name: unit,
                id: `${unit}-input`,
                value: this.period?.[unit]?.() || 0,
                min: 0,
                max:
                    unit === "months"
                        ? 12
                        : unit === "days"
                        ? 31
                        : unit === "hours"
                        ? 24
                        : 10,
                label: this.$t(`timeUnits.${unit}`),
                show: this[visibility[index]],
            }));
        },
    },
    methods: {
        /**
         * Updates the specified period unit with a new value. If the initial
         * period is `null` or `undefined`, creates a new period with default
         * values (0 for years, months, days, and hours) before applying the
         * update. This ensures the method always returns a valid
         * `MomentDuration`.
         * @param {String} unitName - The name of the period unit to update
         * (e.g., 'years', 'months').
         * @param {Number} value - The new value for the specified period unit.
         */
        updatePeriod(unitName: String, value: Number) {
            const basePeriod = this.period
                ? {
                      years: this.period.years(),
                      months: this.period.months(),
                      days: this.period.days(),
                      hours: this.period.hours(),
                  }
                : {
                      years: 0,
                      months: 0,
                      days: 0,
                      hours: 0,
                  };
            basePeriod[unitName] = value;
            const updatedPeriod = MomentWrapper.createDuration(basePeriod);
            this.$emit("update:period", updatedPeriod);
        },
    },
};
</script>
