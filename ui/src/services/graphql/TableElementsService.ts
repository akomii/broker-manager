import { NodeExecutionsTableElement } from "@/utils/TableElements";
import { requests } from "@/services/MockDB";
import { ManagerRequest } from "@/utils/Types";
import ManagerRequestDataHandler from "@/services/handler/ManagerRequestDataHandler";

// TODO replace with actual graphql calls
class TableElementsService {
    private static localStorageKey = "managerRequests";

    private static initializeLocalStorage() {
        if (!localStorage.getItem(this.localStorageKey)) {
            const requestsData = JSON.stringify(requests);
            localStorage.setItem(this.localStorageKey, requestsData);
        }
    }

    private static getRequestsFromLocalStorage(): ManagerRequest[] {
        this.initializeLocalStorage();
        const requestsData = localStorage.getItem(this.localStorageKey);
        return requestsData ? JSON.parse(requestsData) : [];
    }

    private static saveRequestsToLocalStorage(requests: ManagerRequest[]) {
        localStorage.setItem(this.localStorageKey, JSON.stringify(requests));
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

    static getNodeExecutionsTableElements(
        nodeId: number
    ): Promise<NodeExecutionsTableElement[]> {
        return this.newPromise<NodeExecutionsTableElement[]>(() => {
            const requests = this.getRequestsFromLocalStorage();
            const parsedRequests =
                ManagerRequestDataHandler.parseMultiple(requests);

            const nodeRequests = parsedRequests
                .map((request) => {
                    const filteredExecutions = request.executions
                        .filter((execution) =>
                            execution.nodeStatusInfos.some(
                                (status) => status.nodeId === nodeId
                            )
                        )
                        .map((execution) => ({
                            ...execution,
                            nodeStatusInfos: execution.nodeStatusInfos.filter(
                                (status) => status.nodeId === nodeId
                            ),
                        }));
                    return {
                        ...request,
                        executions: filteredExecutions,
                    };
                })
                .filter((request) => request.executions.length > 0);

            const nodeExecutionsTableElements: NodeExecutionsTableElement[] =
                [];
            nodeRequests.forEach((nodeRequest) => {
                nodeRequest.executions.forEach((execution) => {
                    if (
                        execution.nodeStatusInfos &&
                        execution.nodeStatusInfos.length > 0
                    ) {
                        execution.nodeStatusInfos.forEach((nodeStatusInfo) => {
                            nodeExecutionsTableElements.push({
                                id: nodeRequest.id,
                                sequenceId: execution.sequenceId,
                                externalId: execution.externalId,
                                executionState: execution.executionState,
                                query: {
                                    title: nodeRequest.query.title,
                                },
                                referenceDate: execution.referenceDate,
                                nodeStatusInfo: nodeStatusInfo,
                            });
                        });
                    }
                });
            });
            return nodeExecutionsTableElements;
        });
    }
}

export default TableElementsService;
