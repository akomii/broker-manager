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

    static async getNodes(): Promise<ManagerNode[]> {
        return this.newPromise<ManagerNode[]>(() => {
            const nodes = this.getNodesFromLocalStorage();
            return ManagerNodeDataHandler.parseMultiple(nodes);
        });
    }

    static async getNodesByIds(ids: number[]): Promise<ManagerNode[]> {
        return this.newPromise<ManagerNode[]>(() => {
            const nodes = this.getNodesFromLocalStorage();
            const parsedNodes = ManagerNodeDataHandler.parseMultiple(nodes);
            return parsedNodes.filter((node) => ids.includes(node.id));
        });
    }

    static async getNodeById(id: number): Promise<ManagerNode> {
        return this.newPromise<ManagerNode[]>(() => {
            const nodes = this.getNodesFromLocalStorage();
            const parsedNodes = ManagerNodeDataHandler.parseMultiple(nodes);
            return parsedNodes.find((node) => node.id === Number(id)) || null;
        });
    }

    static async createNode(newNode: ManagerNode): Promise<void> {
        return this.newPromise<void>(() => {
            const nodes = this.getNodesFromLocalStorage();
            nodes.push(newNode);
            this.saveNodesToLocalStorage(nodes);
        });
    }

    static async updateNode(updatedNode: ManagerNode): Promise<void> {
        return this.newPromise<void>(() => {
            const nodes = this.getNodesFromLocalStorage();
            const nodeIndex = nodes.findIndex(
                (node) => node.id === updatedNode.id
            );
            if (nodeIndex !== -1) {
                nodes[nodeIndex] = updatedNode;
                this.saveNodesToLocalStorage(nodes);
            } else {
                throw new Error("Node not found, cannot update.");
            }
        });
    }

    static async unsubscribeNode(nodeId: number): Promise<void> {
        return this.newPromise<void>(() => {
            const nodes = this.getNodesFromLocalStorage();
            const nodeIndex = nodes.findIndex((node) => node.id === nodeId);
            if (nodeIndex !== -1) {
                nodes[nodeIndex].apiKey = null;
                this.saveNodesToLocalStorage(nodes);
            } else {
                throw new Error("Node not found, cannot set API key to null.");
            }
        });
    }
}

export default ManagerNodeService;
