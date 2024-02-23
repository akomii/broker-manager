import { Organization } from "@/utils/Types";

// TODO add validation before parsing?
class OrganizationDataHandler {
    static parse(organizationJson: any): Organization {
        return {
            id: organizationJson.id,
            name: organizationJson.name,
        };
    }

    static parseMultiple(organizationsJson: any[]): Organization[] {
        return organizationsJson.map((orgJson) => this.parse(orgJson));
    }
}

export default OrganizationDataHandler;
