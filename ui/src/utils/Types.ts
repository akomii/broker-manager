import { RequestState, ExecutionState } from "@/utils/enums";
import { MomentDuration } from "@/utils/MomentWrapper";

export type Request = SingleRequest | SeriesRequest;

interface ManagerRequest {
  id: number;
  tags: Set<string>;
  authorizedOrgs: Set<number>; // Organization IDs
  targetNodes: Set<number>; // ManagerNode IDs
  requestState: RequestState;
  modificationHistory: ModificationHistoryItem[];
  query: Query;
  executions: RequestExecution[];
}

interface SingleRequest extends ManagerRequest {}

interface SeriesRequest extends ManagerRequest {
  anchoredSequenceIdRef: number;
  isAutoPublishing: boolean;
  seriesClosingDate: Date | null;
  seriesArchiveDate: Date | null;
}

export interface Organization {
  id: number;
  name: string;
}

interface ModificationHistoryItem {
  date: Date;
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
  duration: MomentDuration;
}

// TODO check if interval/intervalHours is necessary
interface RepeatedExecution {
  id: number;
  duration: MomentDuration;
  interval: number;
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

interface ResultsDownloadLog {
  user: string;
  userOrgs: Set<number>; // Organization IDs
  date: Date;
  hashValue: string;
  hashAlgorithm: string;
}

export interface ManagerNode {
  id: number;
  tags: Set<string>;
  clientDN: ClientDN;
  lastContact: Date;
  apiKey: string;
  notes: { [key: string]: string };
}

interface ClientDN {
  CN: string;
  O: string;
  L: string;
}
