<script lang="ts">
export default {
    props: {
        content: {
            type: String as () => string,
            required: true,
        },
        label: {
            type: String as () => string,
            required: true,
        },
        fieldSetHeight: {
            type: String as () => string,
            default: "h-22rem",
        },
    },
    computed: {
        contentHeight() {
            const heightNumber = Number(
                this.fieldSetHeight.replace("h-", "").replace("rem", "")
            );
            return `h-${heightNumber - 5}rem`;
        },
    },
};
</script>
