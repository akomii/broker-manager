<template>
    <div class="mx-3">
        <div class="w-12 flex flex-wrap align-items-center column-gap-2">
            <GoBackButton />
            <p class="text-2xl">
                {{
                    $t("resultsHeaderWithIdAndTitle", { id: id, title: title })
                }}
            </p>
        </div>
    </div>
    <Divider class="mt-0" />
</template>

<script lang="ts">
import Divider from "primevue/divider";
import GoBackButton from "@/components/buttons/GoBackButton.vue";

// TODO refactor and add docs
export default {
    components: {
        Divider,
        GoBackButton,
    },
    props: {
        id: {
            type: Number,
            required: true,
        },
        title: {
            type: String,
            required: true,
        },
    },
};
</script>
