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

    static async getRequests(): Promise<ManagerRequest[]> {
        return this.newPromise<ManagerRequest[]>(() => {
            const requests = this.getRequestsFromLocalStorage();
            return ManagerRequestDataHandler.parseMultiple(requests);
        });
    }

    static async getRequestsForNodeId(
        nodeId: number
    ): Promise<ManagerRequest[]> {
        return this.newPromise<ManagerRequest[]>(() => {
            const requests = this.getRequestsFromLocalStorage();
            const parsedRequests =
                ManagerRequestDataHandler.parseMultiple(requests);
            return parsedRequests
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
        });
    }

    static async getRequestById(id: number): Promise<ManagerRequest> {
        return this.newPromise<ManagerRequest>(() => {
            const requests = this.getRequestsFromLocalStorage();
            const parsedRequests =
                ManagerRequestDataHandler.parseMultiple(requests);
            return (
                parsedRequests.find((request) => request.id === Number(id)) ||
                null
            );
        });
    }

    static async createRequest(newRequest: ManagerRequest): Promise<void> {
        return this.newPromise<void>(() => {
            const requests = this.getRequestsFromLocalStorage();
            requests.push(newRequest);
            this.saveRequestsToLocalStorage(requests);
        });
    }

    static async updateRequest(updatedRequest: ManagerRequest): Promise<void> {
        return this.newPromise<void>(() => {
            const requests = this.getRequestsFromLocalStorage();
            const requestIndex = requests.findIndex(
                (request) => request.id === updatedRequest.id
            );
            if (requestIndex !== -1) {
                requests[requestIndex] = updatedRequest;
                this.saveRequestsToLocalStorage(requests);
            } else {
                throw new Error("Request not found.");
            }
        });
    }

    static closeRequest(id: number): Promise<void> {
        return this.newPromise<void>(() => {
            const requests = this.getRequestsFromLocalStorage();
            const requestIndex = requests.findIndex(
                (request) => request.id === id
            );
            if (requestIndex !== -1) {
                const request = requests[requestIndex];
                request.requestState = RequestState.Closed;
                request.executions.forEach((execution) => {
                    execution.executionState = ExecutionState.Closed;
                    execution.closedDate = new Date();
                });
                this.saveRequestsToLocalStorage(requests);
            } else {
                throw new Error("Request not found.");
            }
        });
    }

    static archiveRequest(id: number): Promise<void> {
        return this.newPromise<void>(() => {
            const requests = this.getRequestsFromLocalStorage();
            const requestIndex = requests.findIndex(
                (request) => request.id === id
            );
            if (requestIndex !== -1) {
                const request = requests[requestIndex];
                request.requestState = RequestState.Archived;
                request.executions.forEach((execution) => {
                    execution.executionState = ExecutionState.Archived;
                    execution.archivedDate = new Date();
                });
                this.saveRequestsToLocalStorage(requests);
            } else {
                throw new Error("Request not found.");
            }
        });
    }

    static publishRequestExecution(
        requestId: number,
        sequenceId: number
    ): Promise<void> {
        return this.newPromise<void>(() => {
            const requests = this.getRequestsFromLocalStorage();
            const requestIndex = requests.findIndex(
                (request) => request.id === requestId
            );
            if (requestIndex !== -1) {
                const request = requests[requestIndex];
                const execution = request.executions.find(
                    (exec) => exec.sequenceId === sequenceId
                );
                if (execution) {
                    execution.executionState = ExecutionState.PUBLISHED;
                    execution.publishedDate = new Date();
                    this.saveRequestsToLocalStorage(requests);
                } else {
                    throw new Error("Execution not found");
                }
            }
        });
    }

    static closeRequestExecution(
        requestId: number,
        sequenceId: number
    ): Promise<void> {
        return this.newPromise<void>(() => {
            const requests = this.getRequestsFromLocalStorage();
            const requestIndex = requests.findIndex(
                (request) => request.id === requestId
            );
            if (requestIndex !== -1) {
                const request = requests[requestIndex];
                const execution = request.executions.find(
                    (exec) => exec.sequenceId === sequenceId
                );
                if (execution) {
                    execution.executionState = ExecutionState.CLOSED;
                    execution.closedDate = new Date();
                    this.saveRequestsToLocalStorage(requests);
                } else {
                    throw new Error("Execution not found");
                }
            }
        });
    }

    static archiveRequestExecution(
        requestId: number,
        sequenceId: number
    ): Promise<void> {
        return this.newPromise<void>(() => {
            const requests = this.getRequestsFromLocalStorage();
            const requestIndex = requests.findIndex(
                (request) => request.id === requestId
            );
            if (requestIndex !== -1) {
                const request = requests[requestIndex];
                const execution = request.executions.find(
                    (exec) => exec.sequenceId === sequenceId
                );
                if (execution) {
                    execution.executionState = ExecutionState.ARCHIVED;
                    execution.archivedDate = new Date();
                    this.saveRequestsToLocalStorage(requests);
                } else {
                    throw new Error("Execution not found");
                }
            }
        });
    }

    static async getAllRequestPrincipals(): Promise<Principal[]> {
        return this.newPromise<Principal[]>(() => {
            const requests = this.getRequestsFromLocalStorage();
            const principals: Principal[] = [];
            requests.forEach((request) => {
                if (request.query && request.query.principal) {
                    principals.push(request.query.principal);
                }
            });
            return principals;
        });
    }

    // ToDo downloadNewestResults()

    // ToDo downloadResultsAgain()
}

export default ManagerRequestService;
