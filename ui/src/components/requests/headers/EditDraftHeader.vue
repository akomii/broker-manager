<template>
    <div class="mx-2">
        <div class="flex flex-wrap justify-content-between">
            <div class="flex flex-wrap justify-content-start align-items-center">
                <Button class="mx-2" icon="pi pi-chevron-left text-xl" outlined />
                <p class="text-2xl mx-2">
                    <template v-if="id !== undefined">[{{ id }}]</template>
                </p>
                <span class="p-float-label">
                    <InputText size="large" class="flex w-30rem text-2xl" v-model="localTitle" />
                    <label>Titel</label>
                </span>
                <div class="mx-2">
                    <RequestState class="text-lg" :state="state" />
                </div>
                <div class="flex flex-wrap max-w-30rem mx-2">
                    <TagList :tags="localTags" :removable="true" @update:tags="localTags = $event" />
                    <TagCreator :tags="localTags" @update:tags="localTags = $event" />
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
import TagList from '@/components/small/tags/TagList.vue';
import TagCreator from '@/components/small/tags/TagCreator.vue';
import MenuButton from '@/components/small/buttons/MenuButton.vue';
import InputText from 'primevue/inputtext';

export default {
    components: {
        Button,
        RequestState,
        TagList,
        Divider,
        MenuButton,
        InputText,
        TagCreator
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
            type: Set<string>,
            required: true
        }
    },
    data() {
        return {
            menu: [
                { label: 'Draft speichern' },
                { label: 'Abbrechen' },
            ],
            localTitle: this.title,
            localTags: this.tags,
        };
    },
    watch: {
        localTitle(newTitle) {
            this.$emit('update:title', newTitle);
        },
        localTags(updatedTags) {
            this.$emit('update:tags', updatedTags);
        },
    }
};
</script>
