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
    RETRIEVED = "retrieved",
    QUEUED = "queued",
    PROCESSING = "processing",
    COMPLETED = "completed",
    REJECTED = "rejected",
    FAILED = "failed",
    EXPIRED = "expired",
}
