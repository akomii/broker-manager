<script lang="ts">
import { Principal } from "@/utils/Types";

export default {
    props: {
        principal: {
            type: Object as () => Principal,
            required: true,
        },
    },
    computed: {
        principalFields(): Array<{ label: string; icon: string; key: string }> {
            return [
                {
                    label: this.$t("name"),
                    icon: "pi pi-user",
                    key: "name",
                },
                {
                    label: this.$t("organization"),
                    icon: "pi pi-building",
                    key: "organization",
                },
                {
                    label: this.$t("email"),
                    icon: "pi pi-envelope",
                    key: "email",
                },
                {
                    label: this.$t("phone"),
                    icon: "pi pi-phone",
                    key: "phone",
                },
            ];
        },
    },
};
</script>
