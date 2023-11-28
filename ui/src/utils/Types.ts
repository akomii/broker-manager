import { RequestState, ExecutionState } from "@/utils/enums";

export type Request = SingleRequest | SeriesRequest;

interface ManagerRequest {
  id: number;
  tags: string[];
  authorizedOrgs: number[]; // Organization IDs
  targetNodes: number[]; // ManagerNode IDs
  requestState: RequestState;
  modificationHistory: ModificationHistoryItem[];
  query: Query;
  executions: Execution[];
}

interface SingleRequest extends ManagerRequest {
}

interface SeriesRequest extends ManagerRequest {
  anchoredSequenceIdRef: number;
  isAutoPublishing: boolean;
  seriesClosingDate?: string;
  seriesArchiveDate?: string;
}

export interface Organization {
  id: number;
  name: string;
}

interface ModificationHistoryItem {
  date: string;
  user: string;
  clob: string;
}

interface Query {
  title: string;
  description: string;
  sql: string;
  principal: Principal;
  singleExecution?: SingleExecution;
  repeatedExecution?: RepeatedExecution;
}

export interface Principal {
  [key: string]: string;
  name: string;
  organization: string;
  email: string;
  phone?: string;
}

interface SingleExecution {
  duration: string;
}

interface RepeatedExecution {
  id: number;
  duration: string;
  interval: string;
  intervalHours: string;
}

interface Execution {
  sequenceId: number;
  externalId: number;
  referenceDate: string;
  scheduledExecutionDate: string;
  scheduledPublishDate: string;
  publishedDate?: string;
  scheduledClosingDate: string;
  closedDate?: string;
  scheduledArchiveDate: string;
  archivedDate?: string;
  creator: string;
  createdDate: string;
  executionState: ExecutionState;
  nodeStatusInfos: NodeStatusInfo[];
  resultsDownloadLog: ResultsDownloadLog[];
}

interface NodeStatusInfo {
  nodeId: number;
  statusMessage?: string;
  deleted?: string;
  retrieved?: string;
  queued?: string;
  processing?: string;
  completed?: string;
  rejected?: string;
  failed?: string;
  expired?: string;
}

interface ResultsDownloadLog {
  user: string;
  userOrgs: number[]; // Organization IDs
  date: string;
  hashValue: string;
  hashAlgorithm: string;
}

export interface ManagerNode {
  id: number;
  tags: string[];
  clientDN: ClientDN;
  lastContact: string;
  apiKey: string;
  notes: { [key: string]: string };
}

interface ClientDN {
  CN: string;
  O: string;
  L: string;
}
