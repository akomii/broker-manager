import { Principal, ManagerNode, ClientDN, RequestExecution, NodeStatusInfo, ResultsDownloadLog, RequestState, ExecutionState, Organization, Request, SingleRequest, SeriesRequest, ManagerRequest, Query, SingleExecution, RepeatedExecution, ModificationHistoryItem } from '@/utils/Types.ts';
import { MomentDuration } from "@/utils/MomentWrapper";

class RequestParser {
    static parse(requestJson: any): Request {
        const commonFields: ManagerRequest = {
            id: requestJson.id,
            tags: new Set(requestJson.tags),
            authorizedOrgs: new Set(requestJson.authorizedOrgs),
            targetNodes: new Set(requestJson.targetNodes),
            requestState: requestJson.requestState as RequestState,
            modificationHistory: ModificationHistoryItemParser.parseMultiple(requestJson.modificationHistory),
            query: {
                title: requestJson.query.title,
                description: requestJson.query.description,
                sql: requestJson.query.sql,
                principal: PrincipalParser.parse(requestJson.query.principal),
                singleExecution: requestJson.query.singleExecution ? {
                    duration: MomentDuration(requestJson.query.singleExecution.duration)
                } as SingleExecution : undefined,
                repeatedExecution: requestJson.query.repeatedExecution ? {
                    id: requestJson.query.repeatedExecution.id,
                    duration: MomentDuration(requestJson.query.repeatedExecution.duration),
                    interval: requestJson.query.repeatedExecution.interval,
                    intervalHours: requestJson.query.repeatedExecution.intervalHours
                } as RepeatedExecution : undefined
            } as Query,
            executions: RequestExecutionParser.parseMultiple(requestJson.executions)
        };

        if ('anchoredSequenceIdRef' in requestJson) {
            return {
                ...commonFields,
                anchoredSequenceIdRef: requestJson.anchoredSequenceIdRef,
                isAutoPublishing: requestJson.isAutoPublishing,
                seriesClosingDate: requestJson.seriesClosingDate ? new Date(requestJson.seriesClosingDate) : null,
                seriesArchiveDate: requestJson.seriesArchiveDate ? new Date(requestJson.seriesArchiveDate) : null
            } as SeriesRequest;
        } else {
            return commonFields as SingleRequest;
        }
    };

    static parseMultiple = (requests: any[]): Request[] => requests.map(this.parse);
}

class OrganizationParser {
    static parse(organizationJson: any): Organization {
        return {
            id: organizationJson.id,
            name: organizationJson.name
        };
    };

    static parseMultiple = (organizations: any[]): Organization[] => organizations.map(this.parse);
}

class ModificationHistoryItemParser {
    static parse(modificationHistoryItemJson: any): ModificationHistoryItem {
        return {
            date: new Date(modificationHistoryItemJson.date),
            user: modificationHistoryItemJson.user,
            clob: modificationHistoryItemJson.clob
        };
    };

    static parseMultiple = (modificationHistoryItems: any[]): ModificationHistoryItem[] => modificationHistoryItems.map(this.parse);
}

class PrincipalParser {
    static parse(principalJson: any): Principal {
        return new Principal(
            principalJson.name,
            principalJson.organization,
            principalJson.email,
            principalJson.phone
        );
    };

    static parseMultiple = (principals: any[]): Principal[] => principals.map(this.parse);
}

class RequestExecutionParser {
    static parse(requestExecutionJson: any): RequestExecution {
        return {
            sequenceId: requestExecutionJson.sequenceId,
            externalId: requestExecutionJson.externalId,
            referenceDate: new Date(requestExecutionJson.referenceDate),
            executionDate: new Date(requestExecutionJson.executionDate),
            scheduledPublishDate: new Date(requestExecutionJson.scheduledPublishDate),
            publishedDate: requestExecutionJson.publishedDate ? new Date(requestExecutionJson.publishedDate) : null,
            scheduledClosingDate: new Date(requestExecutionJson.scheduledClosingDate),
            closedDate: requestExecutionJson.closedDate ? new Date(requestExecutionJson.closedDate) : null,
            scheduledArchiveDate: new Date(requestExecutionJson.scheduledArchiveDate),
            archivedDate: requestExecutionJson.archivedDate ? new Date(requestExecutionJson.archivedDate) : null,
            creator: requestExecutionJson.creator,
            createdDate: new Date(requestExecutionJson.createdDate),
            executionState: requestExecutionJson.executionState as ExecutionState,
            nodeStatusInfos: NodeStatusInfoParser.parseMultiple(requestExecutionJson.nodeStatusInfos),
            resultsDownloadLog: ResultsDownloadLogParser.parseMultiple(requestExecutionJson.resultsDownloadLog)
        };
    };

    static parseMultiple = (requestExecutions: any[]): RequestExecution[] => requestExecutions.map(this.parse);
}

class NodeStatusInfoParser {
    static parse(nodeStatusInfoJson: any): NodeStatusInfo {
        return {
            nodeId: nodeStatusInfoJson.nodeId,
            statusMessage: nodeStatusInfoJson.statusMessage,
            deleted: nodeStatusInfoJson.deleted ? new Date(nodeStatusInfoJson.deleted) : null,
            retrieved: nodeStatusInfoJson.retrieved ? new Date(nodeStatusInfoJson.retrieved) : null,
            queued: nodeStatusInfoJson.queued ? new Date(nodeStatusInfoJson.queued) : null,
            processing: nodeStatusInfoJson.processing ? new Date(nodeStatusInfoJson.processing) : null,
            completed: nodeStatusInfoJson.completed ? new Date(nodeStatusInfoJson.completed) : null,
            rejected: nodeStatusInfoJson.rejected ? new Date(nodeStatusInfoJson.rejected) : null,
            failed: nodeStatusInfoJson.failed ? new Date(nodeStatusInfoJson.failed) : null,
            expired: nodeStatusInfoJson.expired ? new Date(nodeStatusInfoJson.expired) : null
        };
    };

    static parseMultiple = (nodeStatusInfos: any[]): NodeStatusInfo[] => nodeStatusInfos.map(this.parse);
}

class ResultsDownloadLogParser {
    static parse(resultsDownloadLogJson: any): ResultsDownloadLog {
        return {
            user: resultsDownloadLogJson.user,
            userOrgs: new Set(resultsDownloadLogJson.userOrgs),
            date: new Date(resultsDownloadLogJson.date),
            hashValue: resultsDownloadLogJson.hashValue,
            hashAlgorithm: resultsDownloadLogJson.hashAlgorithm
        };
    };

    static parseMultiple = (resultsDownloadLogs: any[]): ResultsDownloadLog[] => resultsDownloadLogs.map(this.parse);
}

class ManagerNodeParser {
    static parse(managerNodeJson: any): ManagerNode {
        return {
            id: managerNodeJson.id,
            tags: new Set(managerNodeJson.tags),
            clientDN: {
                CN: managerNodeJson.clientDN.CN,
                O: managerNodeJson.clientDN.O,
                L: managerNodeJson.clientDN.L
            } as ClientDN,
            lastContact: new Date(managerNodeJson.lastContact),
            apiKey: managerNodeJson.apiKey,
            notes: managerNodeJson.notes
        };
    };

    static parseMultiple = (managerNodes: any[]): ManagerNode[] => managerNodes.map(this.parse);
}
