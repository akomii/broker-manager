<template>
    <div class="mx-2">
        <div class="flex flex-wrap justify-content-between">
            <div class="flex flex-wrap justify-content-start align-items-center">
                <Button class="mx-2" icon="pi pi-chevron-left text-xl" outlined />
                <div class="flex flex-wrap mx-2">
                    <p class="text-2xl">[{{ id }}] {{ title }}</p>
                </div>
                <div class="mx-2">
                    <RequestState class="text-lg" :state="state" />
                </div>
                <div class="flex flex-wrap max-w-30rem mx-2">
                    <TagChip v-for="tag in tags">
                        {{ tag }}
                    </TagChip>
                </div>
            </div>
            <div class="flex flex-wrap justify-content-end align-items-center">
                <div class="mx-2">
                    <MenuButton :icon="'pi pi-chevron-down'" :menu="menu" />
                </div>
            </div>
        </div>
        <Divider class="mt-0" />
    </div>
</template>

<script lang="ts">
import Button from 'primevue/button';
import Divider from 'primevue/divider';
import RequestState from '@/components/small/RequestState.vue';
import TagChip from '@/components/small/tags/TagChip.vue';
import MenuButton from '@/components/small/buttons/MenuButton.vue';

export default {
    components: {
        Button,
        RequestState,
        TagChip,
        Divider,
        MenuButton,
    },
    props: {
        id: {
            type: Number,
            required: true
        },
        title: {
            type: String,
            required: true
        },
        state: {
            type: String,
            required: true,
        },
        tags: {
            type: Array as () => string[]
        }
    },
    data() {
        return {
            menu: [
                { label: 'Draft als Anfrage veröffentlichen' },
                { label: 'Draft duplizieren' },
                { label: 'Draft löschen' },
                { label: 'Draft bearbeiten' },
            ],
        };
    },
};
</script>
