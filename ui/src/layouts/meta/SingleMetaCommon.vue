<template>
    <Fieldset :legend="$t('meta')">
        <div class="field grid mb-1">
            <div class="col-3 flex align-items-center text-lg">
                {{ $t("enums.requestType.label") }}
            </div>
            <div class="col-9 flex align-items-center gap-2">
                <RequestTypeLabel class="text-sm" :type="type" />
                <slot name="request-type-extra"></slot>
            </div>
        </div>
        <div class="field grid mb-1" style="min-height: 40px">
            <div class="col-3 flex align-items-center text-lg">
                {{ $t("enums.executionState.label") }}
            </div>
            <div class="col-3 flex align-items-center gap-2">
                <ExecutionStateLabel
                    v-if="execution.executionState"
                    class="text-sm"
                    :state="execution.executionState"
                />
                <i v-else class="pi pi-minus text-lg" />
            </div>
            <div class="col-3 flex align-items-center text-lg">
                {{ $t("externalId") }}
            </div>
            <div class="col-3 flex align-items-center gap-2">
                <p v-if="execution.externalId">{{ execution.externalId }}</p>
                <i v-else class="pi pi-minus text-lg" />
            </div>
        </div>

        <div class="flex justify-content-center column-gap-3">
            <div class="w-6 mt-3">
                <div class="grid row-gap-3">
                    <slot name="left-section"></slot>
                </div>
            </div>
            <Divider layout="vertical" />
            <div class="w-6 mt-3">
                <div class="grid row-gap-3">
                    <slot name="right-section"></slot>
                </div>
            </div>
        </div>
    </Fieldset>
</template>

<script lang="ts">
import Fieldset from "primevue/fieldset";
import Divider from "primevue/divider";
import RequestTypeLabel from "@/components/labels/RequestTypeLabel.vue";
import ExecutionStateLabel from "@/components/labels/ExecutionStateLabel.vue";
import { RequestExecution } from "@/utils/Types";
import { RequestType } from "@/utils/Enums.ts";
import MomentWrapper from "@/utils/MomentWrapper";
import { SingleExecution } from "@/utils/Types.ts";

export default {
    components: {
        Fieldset,
        Divider,
        RequestTypeLabel,
        ExecutionStateLabel,
    },
    props: {
        type: {
            type: String as () => keyof typeof RequestType,
            required: true,
        },
        execution: {
            type: Object as () => RequestExecution,
            required: true,
        },
        querySchedule: {
            type: Object as () => SingleExecution,
            required: true,
        },
    },
    data() {
        return {
            dummyReferenceStart: MomentWrapper.addDurationToDate(
                this.execution.referenceDate,
                this.querySchedule.duration
            ),
        };
    },
};
</script>
