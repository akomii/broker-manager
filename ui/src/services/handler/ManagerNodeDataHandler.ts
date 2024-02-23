import { ManagerNode, ClientDN, UserNote } from "@/utils/Types";

// TODO add validation before parsing?
class ManagerNodeDataHandler {
    static parse(managerNodeJson: any): ManagerNode {
        return {
            id: managerNodeJson.id,
            tags: [...managerNodeJson.tags],
            clientDN: {
                CN: managerNodeJson.clientDN.CN,
                O: managerNodeJson.clientDN.O,
                L: managerNodeJson.clientDN.L,
            } as ClientDN,
            lastContact: managerNodeJson.lastContact
                ? new Date(managerNodeJson.lastContact)
                : null,
            apiKey: managerNodeJson.apiKey,
            notes: Object.entries(managerNodeJson.notes).map(
                ([date, content]) =>
                    ({
                        date: new Date(date),
                        content: content,
                    } as UserNote)
            ),
        };
    }

    static parseMultiple(mangerNodesJson: any[]): ManagerNode[] {
        return mangerNodesJson.map((nodeJson) => this.parse(nodeJson));
    }
}

export default ManagerNodeDataHandler;
