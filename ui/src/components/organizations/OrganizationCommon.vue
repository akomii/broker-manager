<script lang="ts">
import { Organization } from "@/utils/Types";
import { TestDataService } from "@/services/TestDataService";

export default {
    props: {
        organizationIds: {
            type: Set<number>,
            required: true,
        },
    },
    data() {
        return {
            allOrganizations: [] as Organization[],
            organizationsDummy: [] as Organization[],
        };
    },
    async mounted() {
        await this.fetchAndSortOrganizations();
    },
    methods: {
        async fetchAndSortOrganizations(): Promise<void> {
            const data: Organization[] =
                await TestDataService.getOrganizations();
            this.allOrganizations = this.sortOrganizationsByName(data);
            this.organizationsDummy = this.filterOrganizationsById(
                this.allOrganizations
            );
        },
        // TODO is this even necessary?
        sortOrganizationsByName(organizations: Organization[]): Organization[] {
            return organizations.sort((a, b) => a.name.localeCompare(b.name));
        },
        filterOrganizationsById(organizations: Organization[]): Organization[] {
            return organizations.filter((org) =>
                this.organizationIds.has(org.id)
            );
        },
    },
};
</script>
