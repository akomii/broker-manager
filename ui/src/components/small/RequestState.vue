<template>
    <Tag :value="state" :severity="tagColor" rounded />
</template>

<script lang="ts">
import Tag from 'primevue/tag';
import { RequestState } from '@/utils/Enums.ts';

export default {
    components: {
        Tag
    },
    props: {
        state: {
            type: String,
            required: true,
            validator: value => Object.values(RequestState).includes(value as RequestState)
        }
    },
    computed: {
        tagColor() {
            switch (this.state) {
                case RequestState.DRAFT:
                    return 'warning';
                case RequestState.ONLINE:
                    return 'success';
                case RequestState.CLOSED:
                    return 'danger';
                case RequestState.ARCHIVED:
                    return 'info';
                default:
                    return 'secondary';
            }
        }
    }
};
</script>
