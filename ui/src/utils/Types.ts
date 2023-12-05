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
  executions: RequestExecution[];
}

interface SingleRequest extends ManagerRequest {}

interface SeriesRequest extends ManagerRequest {
  anchoredSequenceIdRef: number;
  isAutoPublishing: boolean;
  seriesClosingDate: string | null;
  seriesArchiveDate: string | null;
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
  [key: string]: string | null;
  name: string;
  organization: string;
  email: string;
  phone: string | null;
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

export interface RequestExecution {
  sequenceId: number;
  externalId: number;
  referenceDate: string;
  scheduledExecutionDate: string;
  scheduledPublishDate: string;
  publishedDate: string | null;
  scheduledClosingDate: string;
  closedDate: string | null;
  scheduledArchiveDate: string;
  archivedDate: string | null;
  creator: string;
  createdDate: string;
  executionState: ExecutionState;
  nodeStatusInfos: NodeStatusInfo[];
  resultsDownloadLog: ResultsDownloadLog[];
}

interface NodeStatusInfo {
  nodeId: number;
  statusMessage: string | null;
  deleted: string | null;
  retrieved: string | null;
  queued: string | null;
  processing: string | null;
  completed: string | null;
  rejected: string | null;
  failed: string | null;
  expired: string | null;
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
