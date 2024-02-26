import { organizations } from "@/services/MockDB";
import { Organization } from "@/utils/Types";
import OrganizationDataHandler from "@/services/handler/OrganizationDataHandler";

// TODO replace with actual graphql calls
class OrganizationService {
    private static newPromise<T>(
        operation: () => T,
        delay: number = 450
    ): Promise<T> {
        return new Promise<T>((resolve) => {
            setTimeout(() => {
                resolve(operation());
            }, delay);
        });
    }

    static async getOrganizations(): Promise<Organization[]> {
        return this.newPromise<Organization[]>(() => {
            return OrganizationDataHandler.parseMultiple(organizations);
        });
    }

    static async getOrganizationByIds(ids: number[]): Promise<Organization[]> {
        return this.newPromise<Organization[]>(() => {
            const parsedOrganizations =
                OrganizationDataHandler.parseMultiple(organizations);
            return parsedOrganizations.filter((org) => ids.includes(org.id));
        });
    }
}

export default OrganizationService;
