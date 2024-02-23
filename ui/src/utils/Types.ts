import { RequestState, ExecutionState, RequestType } from "@/utils/Enums";
import type { MomentDuration } from "@/utils/MomentWrapper";

export type Request = SingleRequest | SeriesRequest;

export interface ManagerRequest {
    id: number;
    requestType: RequestType;
    tags: Array<string>;
    authorizedOrgs: Array<number>; // Organization IDs
    targetNodes: Array<number>; // ManagerNode IDs
    requestState: RequestState;
    modificationHistory: ModificationHistoryItem[];
    query: Query;
    executions: RequestExecution[];
    authorizedOrgsNames?: Array<string>; // optional field for RequestsTable only
    currentExecution?: RequestExecution; // optional field for RequestsTable only
}

export interface SingleRequest extends ManagerRequest {
    requestType: RequestType.SINGLE;
}

export interface SeriesRequest extends ManagerRequest {
    requestType: RequestType.SERIES;
    anchoredSequenceIdRef: number;
    isAutoPublishing: boolean;
    seriesClosingDate: Date | null;
    seriesArchiveDate: Date | null;
}

export interface Organization {
    id: number;
    name: string;
}

export interface ModificationHistoryItem {
    date: Date;
    user: string;
    clob: string;
}

export interface Query {
    title: string;
    description: string;
    sql: string;
    principal: Principal;
    singleExecution?: SingleExecution;
    repeatedExecution?: RepeatedExecution;
}

export interface Principal {
    [key: string]: string | null;
    name: string;
    organization: string;
    email: string;
    phone: string | null;
}

export interface SingleExecution {
    duration: MomentDuration;
}

// TODO check if interval/intervalHours is necessary
export interface RepeatedExecution {
    id: number;
    duration: MomentDuration;
    interval: MomentDuration;
    intervalHours: number;
}

export interface RequestExecution {
    sequenceId: number;
    externalId: number | null;
    referenceDate: Date;
    executionDate: Date;
    scheduledPublishDate: Date;
    publishedDate: Date | null;
    scheduledClosingDate: Date;
    closedDate: Date | null;
    scheduledArchiveDate: Date;
    archivedDate: Date | null;
    creator: string;
    createdDate: Date;
    executionState: ExecutionState;
    nodeStatusInfos: NodeStatusInfo[];
    resultsDownloadLog: ResultsDownloadLog[];
    nodes?: ManagerNode[]; // optional field for ExecutionsTable only
}

export interface NodeStatusInfo {
    nodeId: number;
    statusMessage: string | null;
    deleted: Date | null;
    retrieved: Date | null;
    queued: Date | null;
    processing: Date | null;
    completed: Date | null;
    rejected: Date | null;
    failed: Date | null;
    expired: Date | null;
}

export interface ResultsDownloadLog {
    user: string;
    userOrgs: Array<number>; // Organization IDs
    date: Date;
    hashValue: string;
    hashAlgorithm: string;
}

export interface ManagerNode {
    id: number;
    tags: Array<string>;
    clientDN: ClientDN;
    lastContact: Date | null;
    apiKey: string | null;
    notes: UserNote[];
    nodeStatusInfo?: NodeStatusInfo; // optional field for TargetNodesTable only
}

export interface ClientDN {
    CN: string;
    O: string;
    L: string;
}

export interface UserNote {
    date: Date;
    content: string;
}
