import { organizations } from "@/services/MockDB";
import { Organization } from "@/utils/Types";
import OrganizationDataHandler from "@/services/handler/OrganizationDataHandler";

// TODO replace with actual graphql calls
class OrganizationService {
    static async getOrganizations(): Promise<Organization[]> {
        return new Promise((resolve) => {
            setTimeout(() => {
                resolve(OrganizationDataHandler.parseMultiple(organizations));
            }, 450);
        });
    }

    static async getOrganizationByIds(ids: number[]): Promise<Organization[]> {
        return new Promise((resolve) => {
            setTimeout(() => {
                const parsedOrganizations =
                    OrganizationDataHandler.parseMultiple(organizations);
                resolve(
                    parsedOrganizations.filter((org: Organization) =>
                        ids.includes(org.id)
                    )
                );
            }, 450);
        });
    }
}

export default OrganizationService;
