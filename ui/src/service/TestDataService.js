export const TestDataService = {
  getRequests() {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve([
          {
            id: 1,
            type: "SeriesRequest",
            tags: ["urgent", "medical"],
            allowedOrganisations: [
              "Universitätsklinikum Heidelberg",
              "Uniklinik Köln",
            ],
            createdDate: "2023-01-01T09:12",
            createdBy: "UserA",
            modifierLog: {
              "2023-01-02T12:12": "UserB",
              "2023-01-02T15:16": "UserA",
              "2023-02-02T14:09": "UserC",
            },
            state: "ONLINE",
            isAutoPublishing: true,
            seriesClosingDate: "2023-12-12T00:00",
            seriesArchiveDate: "2024-01-01T00:00",
            targetedNodes: [101, 102, 103, 104, 105],
            executions: [
              {
                externalId: "EX1004",
                referenceDate: "2023-01-01T00:00",
                scheduledExecutionDate: "2023-01-03T00:00",
                scheduledPublishDate: "2023-01-04T00:00",
                scheduledClosingDate: "2023-01-06T00:00",
                scheduledArchiveDate: "2023-01-08T00:00",
                state: "PENDING",
                nodeStatusInfos: [
                  {
                    nodeId: 101,
                    executionId: "EX1004",
                  },
                  {
                    nodeId: 102,
                    executionId: "EX1004",
                  },
                  {
                    nodeId: 103,
                    executionId: "EX1004",
                  },
                  {
                    nodeId: 104,
                    executionId: "EX1004",
                  },
                  {
                    nodeId: 105,
                    executionId: "EX1004",
                  },
                ],
              },
              {
                externalId: "EX1003",
                referenceDate: "2023-02-01T10:00",
                scheduledExecutionDate: "2023-02-03T10:00",
                scheduledPublishDate: "2023-02-04T10:00",
                publishedDate: "2023-02-05T10:00",
                scheduledClosingDate: "2023-02-06T10:00",
                closedDate: "2023-02-07T10:00",
                scheduledArchiveDate: "2023-02-08T10:00",
                archivedDate: "2023-02-09T10:00",
                createdDate: "2023-02-02T10:00",
                state: "PUBLISHED",
                nodeStatusInfos: [
                  {
                    nodeId: 101,
                    executionId: "EX1002",
                    deleted: null,
                    retrieved: "2023-01-15T12:00",
                    queued: "2023-01-16T12:00",
                    processing: "2023-01-17T12:00",
                    completed: "2023-01-18T12:00",
                    rejected: null,
                    failed: null,
                    expired: null,
                  },
                  {
                    nodeId: 102,
                    executionId: "EX1002",
                    deleted: "2023-01-15T13:00",
                    retrieved: null,
                    queued: null,
                    processing: null,
                    completed: null,
                    rejected: null,
                    failed: null,
                    expired: "2023-01-19T13:00",
                  },
                  {
                    nodeId: 103,
                    executionId: "EX1002",
                    deleted: null,
                    retrieved: "2023-01-15T14:00",
                    queued: "2023-01-16T14:00",
                    processing: "2023-01-17T14:00",
                    completed: null,
                    rejected: "2023-01-18T14:00",
                    failed: null,
                    expired: null,
                  },
                  {
                    nodeId: 104,
                    executionId: "EX1002",
                    deleted: null,
                    retrieved: "2023-01-15T15:00",
                    queued: "2023-01-16T15:00",
                    processing: "2023-01-17T15:00",
                    completed: "2023-01-18T15:00",
                    rejected: null,
                    failed: null,
                    expired: null,
                  },
                  {
                    nodeId: 105,
                    executionId: "EX1002",
                    deleted: null,
                    retrieved: "2023-01-15T16:00",
                    queued: "2023-01-16T16:00",
                    processing: "2023-01-17T16:00",
                  },
                ],
              },
              {
                externalId: "EX1002",
                referenceDate: "2023-03-01T15:30",
                scheduledExecutionDate: "2023-03-03T15:30",
                scheduledPublishDate: "2023-03-04T15:30",
                publishedDate: "2023-03-05T15:30",
                scheduledClosingDate: "2023-03-06T15:30",
                closedDate: "2023-03-07T15:30",
                scheduledArchiveDate: "2023-03-08T15:30",
                archivedDate: "2023-03-09T15:30",
                createdDate: "2023-03-02T15:30",
                state: "CLOSED",
                nodeStatusInfos: [
                  {
                    nodeId: 101,
                    executionId: "EX1002",
                    deleted: null,
                    retrieved: "2023-01-15T12:00",
                    queued: "2023-01-16T12:00",
                    processing: "2023-01-17T12:00",
                    completed: "2023-01-18T12:00",
                    rejected: null,
                    failed: null,
                    expired: null,
                  },
                  {
                    nodeId: 102,
                    executionId: "EX1002",
                    deleted: "2023-01-15T13:00",
                    retrieved: null,
                    queued: null,
                    processing: null,
                    completed: null,
                    rejected: null,
                    failed: "2023-01-18T13:00",
                    expired: null,
                    statusMessage: "Processing failed",
                  },
                  {
                    nodeId: 103,
                    executionId: "EX1002",
                    deleted: null,
                    retrieved: "2023-01-15T14:00",
                    queued: "2023-01-16T14:00",
                    processing: "2023-01-17T14:00",
                    completed: "2023-01-18T14:00",
                    rejected: null,
                    failed: null,
                    expired: null,
                  },
                  {
                    nodeId: 104,
                    executionId: "EX1002",
                    deleted: null,
                    retrieved: "2023-01-15T15:00",
                    queued: "2023-01-16T15:00",
                    processing: "2023-01-17T15:00",
                    completed: null,
                    rejected: "2023-01-18T15:00",
                    failed: null,
                    expired: null,
                  },
                  {
                    nodeId: 105,
                    executionId: "EX1002",
                    deleted: "2023-01-15T13:00",
                    retrieved: null,
                    queued: null,
                    processing: null,
                    completed: null,
                    rejected: null,
                    failed: null,
                    expired: "2023-01-19T13:00",
                  },
                ],
              },
              {
                externalId: "EX1001",
                referenceDate: "2023-04-01T08:45",
                scheduledExecutionDate: "2023-04-03T08:45",
                scheduledPublishDate: "2023-04-04T08:45",
                publishedDate: "2023-04-05T08:45",
                scheduledClosingDate: "2023-04-06T08:45",
                closedDate: "2023-04-07T08:45",
                scheduledArchiveDate: "2023-04-08T08:45",
                archivedDate: "2023-04-09T08:45",
                createdDate: "2023-04-02T08:45",
                state: "ARCHIVED",
                nodeStatusInfos: [
                  {
                    nodeId: 101,
                    executionId: "EX1001",
                    deleted: "2023-01-10T12:00",
                    retrieved: "2023-01-11T12:00",
                    queued: "2023-01-12T12:00",
                    processing: "2023-01-13T12:00",
                    completed: "2023-01-14T12:00",
                    rejected: null,
                    failed: null,
                    expired: null,
                  },
                  {
                    nodeId: 102,
                    executionId: "EX1001",
                    deleted: null,
                    retrieved: "2023-01-11T13:00",
                    queued: "2023-01-12T13:00",
                    processing: "2023-01-13T13:00",
                    completed: null,
                    rejected: null,
                    failed: "2023-01-14T13:00",
                    expired: null,
                    statusMessage: "Processing failed",
                  },
                  {
                    nodeId: 103,
                    executionId: "EX1001",
                    deleted: null,
                    retrieved: "2023-01-11T14:00",
                    queued: "2023-01-12T14:00",
                    processing: "2023-01-13T14:00",
                    completed: "2023-01-14T14:00",
                    rejected: null,
                    failed: null,
                    expired: null,
                  },
                  {
                    nodeId: 104,
                    executionId: "EX1001",
                    deleted: null,
                    retrieved: "2023-01-11T15:00",
                    queued: "2023-01-12T15:00",
                    processing: "2023-01-13T15:00",
                    completed: null,
                    rejected: "2023-01-14T15:00",
                    failed: null,
                    expired: null,
                  },
                  {
                    nodeId: 105,
                    executionId: "EX1001",
                    deleted: "2023-01-10T16:00",
                    retrieved: null,
                    queued: null,
                    processing: null,
                    completed: null,
                    rejected: null,
                    failed: null,
                    expired: "2023-01-14T16:00",
                  },
                ],
              },
            ],
            query: {
              title:
                "Request for Patient Data Number and a really really long title",
              description:
                "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi. Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse",
              extension:
                "SELECT a.id AS AccountID, a.name AS AccountName, p.name AS PlanName, SUM(t.amount) AS TotalTransactionAmount, COUNT(DISTINCT t.id) AS NumberOfTransactions, MAX(t.date) AS LastTransactionDate, CASE WHEN a.status = 'active' THEN 'Active Account' ELSE 'Inactive Account' END AS AccountStatus, COALESCE(c.comment, 'No Comments') AS LatestComment FROM accounts a INNER JOIN transactions t ON a.id = t.account_id LEFT JOIN plans p ON a.plan_id = p.id LEFT JOIN ( SELECT account_id, MAX(date) AS latest_comment_date FROM comments GROUP BY account_id ) lc ON a.id = lc.account_id LEFT JOIN comments c ON a.id = c.account_id AND lc.latest_comment_date = c.date WHERE a.creation_date BETWEEN '2021-01-01' AND '2023-01-01' AND t.status IN ('completed', 'pending') GROUP BY a.id, a.name, p.name, a.status, c.comment HAVING SUM(t.amount) > 1000 ORDER BY TotalTransactionAmount DESC, LastTransactionDate DESC LIMIT 100;",
              principal: {
                name: "Dr. Allen Smith",
                organization: "HealthOrg of healthy organisation",
                email: "dr.smith@healthorg.com",
                phone: "1111-2222-3333-4444-5555",
              },
              repeatedExecution: {
                interval: "P2Y3M10D",
                intervalHours: "8",
                id: "1",
                duration: "-P2M",
              },
            },
          },
          {
            id: 2,
            type: "SingleRequest",
            tags: ["priority", "research"],
            allowedOrganisations: ["Mayo Clinic", "Johns Hopkins Hospital"],
            createdDate: "2023-02-01T10:30",
            createdBy: "UserC",
            modifierLog: {
              "2023-02-03T11:00": "UserD",
              "2023-02-04T14:20": "UserC",
              "2023-03-05T09:30": "UserE",
            },
            state: "CLOSED",
            targetedNodes: [106, 107, 108, 109, 110],
            execution: {
              externalId: "EX2001",
              referenceDate: "2023-02-01T08:00",
              scheduledExecutionDate: "2023-02-03T08:00",
              scheduledPublishDate: "2023-02-04T08:00",
              scheduledClosingDate: "2023-02-06T08:00",
              scheduledArchiveDate: "2023-02-08T08:00",
              state: "PENDING",
              nodeStatusInfos: [
                {
                  nodeId: 106,
                  executionId: "EX2001",
                  deleted: null,
                  retrieved: "2023-01-11T14:00",
                  queued: "2023-01-12T14:00",
                  processing: "2023-01-13T14:00",
                  completed: "2023-01-14T14:00",
                  rejected: null,
                  failed: null,
                  expired: null,
                },
                {
                  nodeId: 107,
                  executionId: "EX2001",
                  deleted: null,
                  retrieved: "2023-01-11T14:00",
                  queued: "2023-01-12T14:00",
                  processing: "2023-01-13T14:00",
                  completed: "2023-01-14T14:00",
                  rejected: null,
                  failed: null,
                  expired: null,
                },
                {
                  nodeId: 108,
                  executionId: "EX2001",
                  deleted: null,
                  retrieved: "2023-01-11T14:00",
                  queued: "2023-01-12T14:00",
                  processing: "2023-01-13T14:00",
                  completed: "2023-01-14T14:00",
                  rejected: null,
                  failed: null,
                  expired: null,
                },
                {
                  nodeId: 109,
                  executionId: "EX2001",
                  deleted: null,
                  retrieved: "2023-01-11T14:00",
                  queued: "2023-01-12T14:00",
                  processing: "2023-01-13T14:00",
                  completed: "2023-01-14T14:00",
                  rejected: null,
                  failed: null,
                  expired: null,
                },
                {
                  nodeId: 110,
                  executionId: "EX2001",
                  deleted: null,
                  retrieved: "2023-01-11T14:00",
                  queued: "2023-01-12T14:00",
                  processing: "2023-01-13T14:00",
                  completed: "2023-01-14T14:00",
                  rejected: null,
                  failed: null,
                  expired: null,
                },
              ],
            },
            query: {
              title: "Research Data Collection for Cardiovascular Studies",
              description:
                "This request aims to gather comprehensive data on cardiovascular diseases for a large-scale research project. The data will be used to analyze trends, treatment outcomes, and patient demographics across various healthcare institutions. The study focuses on improving diagnostic methods and treatment strategies for cardiovascular conditions. We are looking for detailed patient records, treatment histories, and follow-up data from participating organizations. The data should be anonymized to ensure patient confidentiality and comply with all relevant data protection regulations.",
              extension:
                "SELECT p.patient_id, p.name, p.age, p.gender, d.diagnosis, t.treatment, t.outcome, t.follow_up_date FROM patients p INNER JOIN diagnoses d ON p.patient_id = d.patient_id INNER JOIN treatments t ON p.patient_id = t.patient_id WHERE d.condition = 'Cardiovascular' AND t.treatment_date BETWEEN '2022-01-01' AND '2023-01-01' ORDER BY t.follow_up_date;",
              principal: {
                name: "Dr. Emily Johnson",
                organization: "Cardio Research Institute",
                email: "dr.johnson@cardio-reserach-institute.com",
                phone: "1111-2222-3333-4444-5555",
              },
              singleExecution: {
                duration: "-P1Y2D",
              },
            },
          },
        ]);
      }, 450); // Simulate an async operation
    });
  },
};

export default TestDataService;
