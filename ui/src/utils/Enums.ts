export enum UserRole {
    IT = "ROLE_IT",
    EC = "ROLE_EC",
}

export enum RequestType {
    SINGLE = "SINGLE",
    SERIES = "SERIES",
}

export enum RequestState {
    DRAFT = "DRAFT",
    ONLINE = "ONLINE",
    CLOSED = "CLOSED",
    ARCHIVED = "ARCHIVED",
}

export enum ExecutionState {
    PENDING = "PENDING",
    PUBLISHED = "PUBLISHED",
    CLOSED = "CLOSED",
    ARCHIVED = "ARCHIVED",
}

export enum NodeState {
    RETRIEVED = "RETRIEVED",
    QUEUED = "QUEUED",
    PROCESSING = "PROCESSING",
    COMPLETED = "COMPLETED",
    REJECTED = "REJECTED",
    FAILED = "FAILED",
    EXPIRED = "EXPIRED",
}
