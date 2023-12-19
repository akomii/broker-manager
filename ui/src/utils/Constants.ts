import { RequestState, ExecutionState } from "@/utils/Enums.ts";

export const requestStateColorMap = {
    [RequestState.DRAFT]: "warning",
    [RequestState.ONLINE]: "success",
    [RequestState.CLOSED]: "danger",
    [RequestState.ARCHIVED]: "info",
};

export const executionStateColorMap = {
    [ExecutionState.PENDING]: "warning",
    [ExecutionState.PUBLISHED]: "success",
    [ExecutionState.CLOSED]: "danger",
    [ExecutionState.ARCHIVED]: "info",
};
