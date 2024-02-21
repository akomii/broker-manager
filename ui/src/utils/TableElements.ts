import type { NodeStatusInfo, ExecutionState } from "@/utils/Types.ts";

export interface NodeExecutionsTableElement {
    id: number;
    sequenceId: number;
    externalId: number | null;
    executionState: ExecutionState;
    title: string;
    referenceDate: Date;
    nodeStatusInfo: NodeStatusInfo;
}
