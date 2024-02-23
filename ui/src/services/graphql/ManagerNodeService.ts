import { nodes } from "@/services/MockDB";
import { ManagerNode } from "@/utils/Types";
import ManagerNodeDataHandler from "@/services/handler/ManagerNodeDataHandler";

// TODO replace with actual graphql calls
class ManagerNodeService {
    private static localStorageKey = "managerNodes";

    private static initializeLocalStorage() {
        if (!localStorage.getItem(this.localStorageKey)) {
            const nodesData = JSON.stringify(nodes);
            localStorage.setItem(this.localStorageKey, nodesData);
        }
    }

    private static getNodesFromLocalStorage(): ManagerNode[] {
        this.initializeLocalStorage();
        const nodesData = localStorage.getItem(this.localStorageKey);
        return nodesData ? JSON.parse(nodesData) : [];
    }

    private static saveNodesToLocalStorage(nodes: ManagerNode[]) {
        localStorage.setItem(this.localStorageKey, JSON.stringify(nodes));
    }

    static async getNodes(): Promise<ManagerNode[]> {
        return new Promise((resolve) => {
            setTimeout(() => {
                const nodes = this.getNodesFromLocalStorage();
                resolve(ManagerNodeDataHandler.parseMultiple(nodes));
            }, 450);
        });
    }

    static async getNodesByIds(ids: number[]): Promise<ManagerNode[]> {
        return new Promise((resolve) => {
            setTimeout(() => {
                const nodes = this.getNodesFromLocalStorage();
                const parsedNodes = ManagerNodeDataHandler.parseMultiple(nodes);
                resolve(
                    parsedNodes.filter((node: ManagerNode) =>
                        ids.includes(node.id)
                    )
                );
            }, 450);
        });
    }

    static async getNodeById(id: number): Promise<ManagerNode> {
        return new Promise((resolve) => {
            setTimeout(() => {
                const nodes = this.getNodesFromLocalStorage();
                const parsedNodes = ManagerNodeDataHandler.parseMultiple(nodes);
                resolve(
                    parsedNodes.find(
                        (node: ManagerNode) => node.id === Number(id)
                    ) || null
                );
            }, 450);
        });
    }

    static async createNode(newNode: ManagerNode): Promise<void> {
        const nodes = this.getNodesFromLocalStorage();
        nodes.push(newNode);
        this.saveNodesToLocalStorage(nodes);
    }

    static async updateNode(updatedNode: ManagerNode): Promise<void> {
        const nodes = this.getNodesFromLocalStorage();
        const nodeIndex = nodes.findIndex((node) => node.id === updatedNode.id);
        if (nodeIndex !== -1) {
            nodes[nodeIndex] = updatedNode;
            this.saveNodesToLocalStorage(nodes);
        } else {
            console.error("Node not found, cannot update.");
        }
    }

    static async unsubscribeNode(nodeId: number): Promise<void> {
        const nodes = this.getNodesFromLocalStorage();
        const nodeIndex = nodes.findIndex((node) => node.id === nodeId);
        if (nodeIndex !== -1) {
            nodes[nodeIndex].apiKey = null;
            this.saveNodesToLocalStorage(nodes);
        } else {
            console.error("Node not found, cannot set API key to null.");
        }
    }
}

export default ManagerNodeService;
