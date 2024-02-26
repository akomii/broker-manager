import { requests } from "@/services/MockDB";
import { ManagerRequest, Principal } from "@/utils/Types";
import { RequestState, ExecutionState } from "@/utils/Enums";
import ManagerRequestDataHandler from "@/services/handler/ManagerRequestDataHandler";

// TODO replace with actual graphql calls
class ManagerRequestService {
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

    static async getRequests(): Promise<ManagerRequest[]> {
        return new Promise((resolve) => {
            setTimeout(() => {
                const requests = this.getRequestsFromLocalStorage();
                resolve(ManagerRequestDataHandler.parseMultiple(requests));
            }, 450);
        });
    }

    static async getRequestsForNodeId(
        nodeId: number
    ): Promise<ManagerRequest[]> {
        return new Promise((resolve) => {
            setTimeout(() => {
                const requests = this.getRequestsFromLocalStorage();
                const parsedRequests =
                    ManagerRequestDataHandler.parseMultiple(requests);
                const filteredRequests = parsedRequests
                    .map((request) => {
                        const filteredExecutions = request.executions
                            .filter((execution) =>
                                execution.nodeStatusInfos.some(
                                    (status) => status.nodeId === nodeId
                                )
                            )
                            .map((execution) => {
                                const filteredNodeStatusInfos =
                                    execution.nodeStatusInfos.filter(
                                        (status) => status.nodeId === nodeId
                                    );
                                return {
                                    ...execution,
                                    nodeStatusInfos: filteredNodeStatusInfos,
                                };
                            });
                        return {
                            ...request,
                            executions: filteredExecutions,
                        };
                    })
                    .filter((request) => request.executions.length > 0);
                resolve(filteredRequests);
            }, 450);
        });
    }

    static async getRequestById(id: number): Promise<ManagerRequest> {
        return new Promise((resolve) => {
            setTimeout(() => {
                const requests = this.getRequestsFromLocalStorage();
                const parsedRequests =
                    ManagerRequestDataHandler.parseMultiple(requests);
                resolve(
                    parsedRequests.find(
                        (request: ManagerRequest) => request.id === Number(id)
                    ) || null
                );
            }, 450);
        });
    }

    static async createRequest(newRequest: ManagerRequest): Promise<void> {
        const requests = this.getRequestsFromLocalStorage();
        requests.push(newRequest);
        this.saveRequestsToLocalStorage(requests);
    }

    static async updateRequest(updatedRequest: ManagerRequest): Promise<void> {
        const requests = this.getRequestsFromLocalStorage();
        const requestIndex = requests.findIndex(
            (request) => request.id === updatedRequest.id
        );
        if (requestIndex !== -1) {
            requests[requestIndex] = updatedRequest;
            this.saveRequestsToLocalStorage(requests);
        } else {
            console.error("Request not found, cannot update.");
        }
    }

    static async closeRequest(id: number): Promise<void> {
        const request = await this.getRequestById(id);
        request.requestState = RequestState.Closed;
        request.executions.forEach((execution) => {
            execution.executionState = ExecutionState.Closed;
            execution.closedDate = new Date();
        });
        await this.updateRequest(request);
    }

    static async archiveRequest(id: number): Promise<void> {
        const request = await this.getRequestById(id);
        request.requestState = RequestState.Archived;
        request.executions.forEach((execution) => {
            execution.executionState = ExecutionState.Archived;
            execution.archivedDate = new Date();
        });
        await this.updateRequest(request);
    }

    static async publishRequestExecutuion(
        requestId: number,
        sequenceId: number
    ): Promise<void> {
        const request = await this.getRequestById(requestId);
        const execution = request.executions.find(
            (exec) => exec.sequenceId === sequenceId
        );
        if (!execution) {
            throw new Error("Execution not found");
        }
        execution.executionState = ExecutionState.PUBLISHED;
        execution.publishedDate = new Date();
        await this.updateRequest(request);
    }

    static async closeRequestExecutuion(
        requestId: number,
        sequenceId: number
    ): Promise<void> {
        const request = await this.getRequestById(requestId);
        const execution = request.executions.find(
            (exec) => exec.sequenceId === sequenceId
        );
        if (!execution) {
            throw new Error("Execution not found");
        }
        execution.executionState = ExecutionState.CLOSED;
        execution.closedDate = new Date();
        await this.updateRequest(request);
    }

    static async archiveRequestExecutuion(
        requestId: number,
        sequenceId: number
    ): Promise<void> {
        const request = await this.getRequestById(requestId);
        const execution = request.executions.find(
            (exec) => exec.sequenceId === sequenceId
        );
        if (!execution) {
            throw new Error("Execution not found");
        }
        execution.executionState = ExecutionState.ARCHIVED;
        execution.archivedDate = new Date();
        await this.updateRequest(request);
    }

    static async getAllRequestPrincipals(): Promise<Principal[]> {
        const requests = await this.getRequests();
        const principals: Principal[] = [];
        requests.forEach((request) => {
            if (request.query && request.query.principal) {
                principals.push(request.query.principal);
            }
        });
        return principals;
    }

    // ToDo downloadNewestResults()

    // ToDo downloadResultsAgain()
}

export default ManagerRequestService;
