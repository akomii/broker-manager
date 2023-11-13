<template>
    <Tag :value="state" :severity="tagColor" rounded/>
</template>
  
<script lang="ts">
import Tag from 'primevue/tag';

export default {
    components: {
        Tag,
    },
    props: {
        state: {
            type: String,
            required: true
        }
    },
    computed: {
        tagColor() {
            switch (this.state) {
                case 'DRAFT':
                    return 'warning';
                case 'ONLINE':
                    return 'success';
                case 'CLOSED':
                    return 'danger';
                case 'ARCHIVED':
                    return 'info';
                default:
                    return 'secondary';
            }
        }
    }
};
</script>
