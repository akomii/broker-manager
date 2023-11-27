export const TestDataService = {
  getRequests() {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve([
          {
            id: 1,
            tags: ["urgent", "medical", "diagnosis"],
            allowedOrganisationsToDownloadResults: ["RKI", "AKTIN"],
            targetNodes: [1, 2, 3, 4, 5],
            requestState: "ONLINE",
            requestModificationHistory: [
              {
                modifiedDate: "2023-08-05T11:15",
                modifiedBy: "jdoe123",
                managerRequestBlob:
                  '<?xml version="1.0" encoding="utf-8" ?><xsd:schema elementFormDefault="qualified" xmlns:xsd="http://www.w3.org/2001/XMLSchema"><xsd:element name="bookstore" type="bookstoreType" /><xsd:complexType name="bookstoreType"><xsd:sequence minOccurs="0" maxOccurs="unbounded"><xsd:element name="book" type="bookType" /></xsd:sequence></xsd:complexType><xsd:complexType name="bookType"><xsd:sequence><xsd:element name="title" type="xsd:string" /><xsd:element name="author" type="authorName" /><xsd:element name="genre" type="xsd:string" minOccurs="0" /></xsd:sequence><xsd:attribute name="price" type="xsd:decimal" use="required" /><xsd:attribute name="publicationdate" type="xsd:date" /><xsd:attribute name="ISBN" type="xsd:string" /></xsd:complexType><xsd:complexType name="authorName"><xsd:sequence><xsd:element name="first-name" type="xsd:string" /><xsd:element name="last-name" type="xsd:string" /></xsd:sequence></xsd:complexType></xsd:schema>',
              },
              {
                modifiedDate: "2023-09-15T14:30",
                modifiedBy: "asmith456",
                managerRequestBlob:
                  '<?xml version="1.0" encoding="utf-8" ?><xsd:schema elementFormDefault="qualified" xmlns:xsd="http://www.w3.org/2001/XMLSchema"><xsd:element name="bookstore" type="bookstoreType" /><xsd:complexType name="bookstoreType"><xsd:sequence minOccurs="0" maxOccurs="unbounded"><xsd:element name="book" type="bookType" /></xsd:sequence></xsd:complexType><xsd:complexType name="bookType"><xsd:sequence><xsd:element name="title" type="xsd:string" /><xsd:element name="author" type="authorName" /><xsd:element name="genre" type="xsd:string" minOccurs="0" /></xsd:sequence><xsd:attribute name="price" type="xsd:decimal" use="required" /><xsd:attribute name="publicationdate" type="xsd:date" /><xsd:attribute name="ISBN" type="xsd:string" /></xsd:complexType><xsd:complexType name="authorName"><xsd:sequence><xsd:element name="first-name" type="xsd:string" /><xsd:element name="last-name" type="xsd:string" /></xsd:sequence></xsd:complexType></xsd:schema>',
              },
              {
                modifiedDate: "2023-10-20T09:45",
                modifiedBy: "kjohnson789",
                managerRequestBlob:
                  '<?xml version="1.0" encoding="utf-8" ?><xsd:schema elementFormDefault="qualified" xmlns:xsd="http://www.w3.org/2001/XMLSchema"><xsd:element name="bookstore" type="bookstoreType" /><xsd:complexType name="bookstoreType"><xsd:sequence minOccurs="0" maxOccurs="unbounded"><xsd:element name="book" type="bookType" /></xsd:sequence></xsd:complexType><xsd:complexType name="bookType"><xsd:sequence><xsd:element name="title" type="xsd:string" /><xsd:element name="author" type="authorName" /><xsd:element name="genre" type="xsd:string" minOccurs="0" /></xsd:sequence><xsd:attribute name="price" type="xsd:decimal" use="required" /><xsd:attribute name="publicationdate" type="xsd:date" /><xsd:attribute name="ISBN" type="xsd:string" /></xsd:complexType><xsd:complexType name="authorName"><xsd:sequence><xsd:element name="first-name" type="xsd:string" /><xsd:element name="last-name" type="xsd:string" /></xsd:sequence></xsd:complexType></xsd:schema>',
              },
            ],
            type: "SeriesRequest",
            anchoredEnumerativeExecution: 5,
            isAutoPublishing: true,
            seriesClosingDate: null,
            seriesArchiveDate: "2024-01-01T00:00",
            query: {
              title:
                "Request for Patient Data Number and a really really long title",
              description:
                "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi. Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse",
              extensions:
                "<sql xmlns=\"http://aktin.org/ns/i2b2/sql\"><temporary-table name=\"temp_data\"/><source type=\"application/sql\">SELECT a.id AS AccountID, a.name AS AccountName, p.name AS PlanName, SUM(t.amount) AS TotalTransactionAmount, COUNT(DISTINCT t.id) AS NumberOfTransactions, MAX(t.date) AS LastTransactionDate, CASE WHEN a.status = 'active' THEN 'Active Account' ELSE 'Inactive Account' END AS AccountStatus, COALESCE(c.comment, 'No Comments') AS LatestComment FROM accounts a INNER JOIN transactions t ON a.id = t.account_id LEFT JOIN plans p ON a.plan_id = p.id LEFT JOIN ( SELECT account_id, MAX(date) AS latest_comment_date FROM comments GROUP BY account_id ) lc ON a.id = lc.account_id LEFT JOIN comments c ON a.id = c.account_id AND lc.latest_comment_date = c.date WHERE a.creation_date BETWEEN '2021-01-01' AND '2023-01-01' AND t.status IN ('completed', 'pending') GROUP BY a.id, a.name, p.name, a.status, c.comment HAVING SUM(t.amount) > 1000 ORDER BY TotalTransactionAmount DESC, LastTransactionDate DESC LIMIT 100;</source><anonymize><ref table=\"temp_data\" column=\"encounter_num\"/></anonymize><export destination=\"technical_report\" table=\"temp_data\"/></sql>",
              principal: {
                name: "Dr. Allen Smith",
                organization: "HealthOrg of healthy organisation",
                email: "dr.smith@healthorg.com",
                phone: "1111-2222-3333-4444-5555",
              },
              repeatedExecution: {
                duration: "-P2M",
                interval: "P2Y3M10D",
                intervalHours: "8",
                id: "1",
              },
            },
            executions: [
              {
                enumerativeId: 1,
                externalId: 101,
                referenceDate: "2023-11-25T00:00",
                scheduledExecutionDate: "2023-12-02T00:00",
                scheduledPublishDate: "2023-12-03T00:00",
                publishedDate: "2023-12-03T00:00",
                scheduledClosingDate: "2023-12-10T00:00",
                closedDate: "2023-12-10T00:00",
                scheduledArchiveDate: "2023-12-15T00:00",
                archivedDate: "2023-12-15T00:00",
                creator: "jsmith123",
                createdDate: "2023-11-20T00:00",
                requestExecutionState: "ARCHIVED",
                nodeStatusInfos: [
                  {
                    externalId: 101,
                    statusMessage: null,
                    nodeId: 1,
                    deleted: null,
                    retrieved: "2023-12-10T08:00",
                    queued: null,
                    processing: null,
                    completed: null,
                    rejected: "2023-12-10T08:05",
                    failed: null,
                    expired: null,
                  },
                  {
                    externalId: 101,
                    statusMessage: null,
                    nodeId: 2,
                    deleted: null,
                    retrieved: "2023-12-10T08:00",
                    queued: "2023-12-10T08:10",
                    processing: "2023-12-10T08:15",
                    completed: "2023-12-10T08:30",
                    rejected: null,
                    failed: null,
                    expired: null,
                  },
                  {
                    externalId: 101,
                    statusMessage: null,
                    nodeId: 3,
                    deleted: null,
                    retrieved: "2023-12-10T08:00",
                    queued: "2023-12-10T08:10",
                    processing: "2023-12-10T08:15",
                    completed: "2023-12-10T08:30",
                    rejected: null,
                    failed: null,
                    expired: null,
                  },
                  {
                    externalId: 101,
                    statusMessage: null,
                    nodeId: 4,
                    deleted: null,
                    retrieved: "2023-12-10T08:00",
                    queued: "2023-12-10T08:10",
                    processing: "2023-12-10T08:15",
                    completed: "2023-12-10T08:30",
                    rejected: null,
                    failed: null,
                    expired: null,
                  },
                  {
                    externalId: 101,
                    statusMessage: null,
                    nodeId: 5,
                    deleted: null,
                    retrieved: "2023-12-10T08:00",
                    queued: "2023-12-10T08:10",
                    processing: "2023-12-10T08:15",
                    completed: "2023-12-10T08:30",
                    rejected: null,
                    failed: null,
                    expired: null,
                  },
                ],
                resultsDownloadLog: [
                  {
                    user: "jsmith",
                    userOrgs: ["RKI"],
                    downloadedDate: "2024-01-06T09:15",
                    resultHash: "5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8",
                  },
                  {
                    user: "asmith",
                    userOrgs: ["AKTIN"],
                    downloadedDate: "2024-01-06T08:30",
                    resultHash: "5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8",
                  },
                  {
                    user: "kjohnson",
                    userOrgs: ["RKI", "AKTIN"],
                    downloadedDate: "2024-01-07T10:00",
                    resultHash: "9c4a6a156b9c51d57227be4dbf6149d1122b85b1",
                  },
                ],
              },
              {
                enumerativeId: 2,
                externalId: 102,
                referenceDate: "2023-11-28T00:00",
                scheduledExecutionDate: "2023-12-05T00:00",
                scheduledPublishDate: "2023-12-06T00:00",
                publishedDate: "2023-12-06T00:00",
                scheduledClosingDate: "2023-12-12T00:00",
                closedDate: "2023-12-12T00:00",
                scheduledArchiveDate: "2023-12-17T00:00",
                archivedDate: "2023-12-17T00:00",
                creator: "auto",
                createdDate: "2023-11-23T00:00",
                requestExecutionState: "ARCHIVED",
                nodeStatusInfos: [
                  {
                    externalId: 102,
                    statusMessage: null,
                    nodeId: 1,
                    deleted: null,
                    retrieved: "2023-12-15T08:00",
                    queued: null,
                    processing: null,
                    completed: null,
                    rejected: "2023-12-15T08:05",
                    failed: null,
                    expired: null,
                  },
                  {
                    externalId: 102,
                    statusMessage: null,
                    nodeId: 2,
                    deleted: null,
                    retrieved: "2023-12-15T08:00",
                    queued: "2023-12-15T08:10",
                    processing: "2023-12-15T08:15",
                    completed: "2023-12-15T08:30",
                    rejected: null,
                    failed: null,
                    expired: null,
                  },
                  {
                    externalId: 102,
                    statusMessage: null,
                    nodeId: 3,
                    deleted: null,
                    retrieved: "2023-12-15T08:00",
                    queued: "2023-12-15T08:10",
                    processing: "2023-12-15T08:15",
                    completed: "2023-12-15T08:30",
                    rejected: null,
                    failed: null,
                    expired: null,
                  },
                  {
                    externalId: 102,
                    statusMessage: null,
                    nodeId: 4,
                    deleted: null,
                    retrieved: "2023-12-15T08:00",
                    queued: "2023-12-15T08:10",
                    processing: "2023-12-15T08:15",
                    completed: "2023-12-15T08:30",
                    rejected: null,
                    failed: null,
                    expired: null,
                  },
                  {
                    externalId: 102,
                    statusMessage: null,
                    nodeId: 5,
                    deleted: null,
                    retrieved: "2023-12-15T08:00",
                    queued: "2023-12-15T08:10",
                    processing: "2023-12-15T08:15",
                    completed: "2023-12-15T08:30",
                    rejected: null,
                    failed: null,
                    expired: null,
                  },
                ],
                resultsDownloadLog: [
                  {
                    user: "jsmith",
                    userOrgs: ["RKI"],
                    downloadedDate: "2024-01-08T14:45",
                    resultHash: "5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8",
                  },
                  {
                    user: "asmith",
                    userOrgs: ["AKTIN"],
                    downloadedDate: "2024-01-08T15:30",
                    resultHash: "9c4a6a156b9c51d57227be4dbf6149d1122b85b1",
                  },
                  {
                    user: "kjohnson",
                    userOrgs: ["RKI", "AKTIN"],
                    downloadedDate: "2024-01-09T09:00",
                    resultHash: "d8e8fca2dc0f896fd7cb4cb0031ba2499cc77390",
                  },
                  {
                    user: "jsmith",
                    userOrgs: ["RKI"],
                    downloadedDate: "2024-01-09T10:30",
                    resultHash: "5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8",
                  },
                ],
              },
              {
                enumerativeId: 3,
                externalId: 103,
                referenceDate: "2023-12-01T00:00",
                scheduledExecutionDate: "2023-12-08T00:00",
                scheduledPublishDate: "2023-12-09T00:00",
                publishedDate: "2023-12-09T00:00",
                scheduledClosingDate: "2023-12-16T00:00",
                closedDate: "2023-12-16T00:00",
                scheduledArchiveDate: "2023-12-21T00:00",
                archivedDate: null,
                creator: "auto",
                createdDate: "2023-11-26T00:00",
                requestExecutionState: "CLOSED",
                nodeStatusInfos: [
                  {
                    externalId: 103,
                    statusMessage: null,
                    nodeId: 1,
                    deleted: null,
                    retrieved: "2023-12-20T08:00",
                    queued: null,
                    processing: null,
                    completed: null,
                    rejected: "2023-12-20T08:05",
                    failed: null,
                    expired: null,
                  },
                  {
                    externalId: 103,
                    statusMessage: null,
                    nodeId: 2,
                    deleted: null,
                    retrieved: "2023-12-20T08:00",
                    queued: "2023-12-20T08:10",
                    processing: "2023-12-20T08:15",
                    completed: "2023-12-20T08:30",
                    rejected: null,
                    failed: null,
                    expired: null,
                  },
                  {
                    externalId: 103,
                    statusMessage:
                      "Failed with error: Node 3 processing error.",
                    nodeId: 3,
                    deleted: null,
                    retrieved: "2023-12-20T08:00",
                    queued: "2023-12-20T08:10",
                    processing: "2023-12-20T08:15",
                    completed: null,
                    rejected: null,
                    failed: "2023-12-20T08:25",
                    expired: null,
                  },
                  {
                    externalId: 103,
                    statusMessage:
                      "Failed with error: Node 4 processing error.",
                    nodeId: 4,
                    deleted: null,
                    retrieved: "2023-12-20T08:00",
                    queued: "2023-12-20T08:10",
                    processing: "2023-12-20T08:15",
                    completed: null,
                    rejected: null,
                    failed: "2023-12-20T08:25",
                    expired: null,
                  },
                  {
                    externalId: 103,
                    statusMessage: null,
                    nodeId: 5,
                    deleted: null,
                    retrieved: "2023-12-20T08:00",
                    queued: null,
                    processing: null,
                    completed: null,
                    rejected: null,
                    failed: null,
                    expired: "2023-12-20T08:30",
                  },
                ],
                resultsDownloadLog: [
                  {
                    user: "jsmith",
                    userOrgs: ["RKI"],
                    downloadedDate: "2024-01-11T14:45",
                    resultHash: "5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8",
                  },
                  {
                    user: "asmith",
                    userOrgs: ["AKTIN"],
                    downloadedDate: "2024-01-11T15:30",
                    resultHash: "9c4a6a156b9c51d57227be4dbf6149d1122b85b1",
                  },
                  {
                    user: "kjohnson",
                    userOrgs: ["RKI", "AKTIN"],
                    downloadedDate: "2024-01-12T09:00",
                    resultHash: "d8e8fca2dc0f896fd7cb4cb0031ba2499cc77390",
                  },
                  {
                    user: "jsmith",
                    userOrgs: ["RKI"],
                    downloadedDate: "2024-01-12T10:30",
                    resultHash: "5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8",
                  },
                  {
                    user: "asmith",
                    userOrgs: ["AKTIN"],
                    downloadedDate: "2024-01-12T11:15",
                    resultHash: "9c4a6a156b9c51d57227be4dbf6149d1122b85b1",
                  },
                  {
                    user: "kjohnson",
                    userOrgs: ["RKI", "AKTIN"],
                    downloadedDate: "2024-01-12T12:00",
                    resultHash: "d8e8fca2dc0f896fd7cb4cb0031ba2499cc77390",
                  },
                ],
              },
              {
                enumerativeId: 4,
                externalId: 104,
                referenceDate: "2023-12-05T00:00",
                scheduledExecutionDate: "2023-12-12T00:00",
                scheduledPublishDate: "2023-12-13T00:00",
                publishedDate: "2023-12-13T00:00",
                scheduledClosingDate: "2023-12-20T00:00",
                closedDate: null,
                scheduledArchiveDate: "2023-12-25T00:00",
                archivedDate: null,
                creator: "auto",
                createdDate: "2023-11-30T00:00",
                requestExecutionState: "PUBLISHED",
                nodeStatusInfos: [
                  {
                    externalId: 104,
                    statusMessage: null,
                    nodeId: 1,
                    deleted: null,
                    retrieved: "2023-12-25T08:00",
                    queued: null,
                    processing: null,
                    completed: null,
                    rejected: "2023-12-25T08:05",
                    failed: null,
                    expired: null,
                  },
                  {
                    externalId: 104,
                    statusMessage: null,
                    nodeId: 2,
                    deleted: null,
                    retrieved: "2023-12-25T08:00",
                    queued: null,
                    processing: null,
                    completed: null,
                    rejected: null,
                    failed: null,
                    expired: null,
                  },
                  {
                    externalId: 104,
                    statusMessage: null,
                    nodeId: 3,
                    deleted: null,
                    retrieved: "2023-12-25T08:00",
                    queued: null,
                    processing: null,
                    completed: null,
                    rejected: null,
                    failed: null,
                    expired: null,
                  },
                  {
                    externalId: 104,
                    statusMessage: null,
                    nodeId: 4,
                    deleted: null,
                    retrieved: "2023-12-25T08:00",
                    queued: null,
                    processing: null,
                    completed: null,
                    rejected: null,
                    failed: null,
                    expired: null,
                  },
                  {
                    externalId: 104,
                    statusMessage: null,
                    nodeId: 5,
                    deleted: null,
                    retrieved: "2023-12-25T08:00",
                    queued: null,
                    processing: null,
                    completed: null,
                    rejected: null,
                    failed: null,
                    expired: null,
                  },
                ],
                resultsDownloadLog: [
                  {
                    user: "kjohnson",
                    userOrgs: ["RKI", "AKTIN"],
                    downloadedDate: "2024-01-10T11:15",
                    resultHash: "1a79a4d60de6718e8e5b326e338ae533",
                  },
                ],
              },
              {
                enumerativeId: 5,
                externalId: 105,
                referenceDate: "2023-12-10T00:00",
                scheduledExecutionDate: "2023-12-17T00:00",
                scheduledPublishDate: "2023-12-18T00:00",
                publishedDate: null,
                scheduledClosingDate: "2023-12-25T00:00",
                closedDate: null,
                scheduledArchiveDate: "2023-12-30T00:00",
                archivedDate: null,
                creator: "auto",
                createdDate: "2023-12-05T00:00",
                requestExecutionState: "PENDING",
                nodeStatusInfos: [],
                resultsDownloadLog: [],
              },
            ],
          },
          {
            id: 2,
            tags: [
              "physical-therapy",
              "cardiology",
              "radiology",
              "dermatology",
              "allergy",
              "orthopedics",
            ],
            allowedOrganisationsToDownloadResults: ["AKTIN"],
            targetNodes: [1, 3, 5],
            requestState: "ONLINE",
            requestModificationHistory: [
              {
                modifiedDate: "2023-11-25T13:45",
                modifiedBy: "lsanders987",
                managerRequestBlob:
                  '<?xml version="1.0" encoding="utf-8" ?><xsd:schema elementFormDefault="qualified" xmlns:xsd="http://www.w3.org/2001/XMLSchema"><xsd:element name="bookstore" type="bookstoreType" /><xsd:complexType name="bookstoreType"><xsd:sequence minOccurs="0" maxOccurs="unbounded"><xsd:element name="book" type="bookType" /></xsd:sequence></xsd:complexType><xsd:complexType name="bookType"><xsd:sequence><xsd:element name="title" type="xsd:string" /><xsd:element name="author" type="authorName" /><xsd:element name="genre" type="xsd:string" minOccurs="0" /></xsd:sequence><xsd:attribute name="price" type="xsd:decimal" use="required" /><xsd:attribute name="publicationdate" type="xsd:date" /><xsd:attribute name="ISBN" type="xsd:string" /></xsd:complexType><xsd:complexType name="authorName"><xsd:sequence><xsd:element name="first-name" type="xsd:string" /><xsd:element name="last-name" type="xsd:string" /></xsd:sequence></xsd:complexType></xsd:schema>',
              },
              {
                modifiedDate: "2023-12-15T10:20",
                modifiedBy: "rsmith789",
                managerRequestBlob:
                  '<?xml version="1.0" encoding="utf-8" ?><xsd:schema elementFormDefault="qualified" xmlns:xsd="http://www.w3.org/2001/XMLSchema"><xsd:element name="bookstore" type="bookstoreType" /><xsd:complexType name="bookstoreType"><xsd:sequence minOccurs="0" maxOccurs="unbounded"><xsd:element name="book" type="bookType" /></xsd:sequence></xsd:complexType><xsd:complexType name="bookType"><xsd:sequence><xsd:element name="title" type="xsd:string" /><xsd:element name="author" type="authorName" /><xsd:element name="genre" type="xsd:string" minOccurs="0" /></xsd:sequence><xsd:attribute name="price" type="xsd:decimal" use="required" /><xsd:attribute name="publicationdate" type="xsd:date" /><xsd:attribute name="ISBN" type="xsd:string" /></xsd:complexType><xsd:complexType name="authorName"><xsd:sequence><xsd:element name="first-name" type="xsd:string" /><xsd:element name="last-name" type="xsd:string" /></xsd:sequence></xsd:complexType></xsd:schema>',
              },
            ],
            type: "SingleRequest",
            query: {
              title: "Research Data Collection for Cardiovascular Studies",
              description:
                "This request aims to gather comprehensive data on cardiovascular diseases for a large-scale research project. The data will be used to analyze trends, treatment outcomes, and patient demographics across various healthcare institutions. The study focuses on improving diagnostic methods and treatment strategies for cardiovascular conditions. We are looking for detailed patient records, treatment histories, and follow-up data from participating organizations. The data should be anonymized to ensure patient confidentiality and comply with all relevant data protection regulations.",
              extensions:
                '<sql xmlns="http://aktin.org/ns/i2b2/sql"><temporary-table name="temp_data"/><source type="application/sql">SSELECT p.patient_id, p.name, p.age, p.gender, d.diagnosis, t.treatment, t.outcome, t.follow_up_date FROM patients p INNER JOIN diagnoses d ON p.patient_id = d.patient_id INNER JOIN treatments t ON p.patient_id = t.patient_id WHERE d.condition = \'Cardiovascular\' AND t.treatment_date BETWEEN \'2022-01-01\' AND \'2023-01-01\' ORDER BY t.follow_up_date;</source><anonymize><ref table="temp_data" column="encounter_num"/></anonymize><export destination="technical_report" table="temp_data"/></sql>',
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
            execution: {
              enumerativeId: 1,
              externalId: 106,
              referenceDate: "2023-12-15T00:00",
              scheduledExecutionDate: "2023-12-22T00:00",
              scheduledPublishDate: "2023-12-23T00:00",
              publishedDate: "2023-12-23T00:00",
              scheduledClosingDate: "2023-12-30T00:00",
              closedDate: null,
              scheduledArchiveDate: "2024-01-04T00:00",
              archivedDate: null,
              creator: "tgreen456",
              createdDate: "2023-12-10T00:00",
              requestExecutionState: "PUBLISHED",
              nodeStatusInfos: [
                {
                  externalId: 106,
                  statusMessage: null,
                  nodeId: 1,
                  deleted: null,
                  retrieved: "2024-01-05T08:00",
                  queued: "2024-01-05T08:10",
                  processing: "2024-01-05T08:15",
                  completed: "2024-01-05T08:30",
                  rejected: null,
                  failed: null,
                  expired: null,
                },
                {
                  externalId: 106,
                  statusMessage: "Failed with error: Node 3 processing error.",
                  nodeId: 3,
                  deleted: null,
                  retrieved: "2024-01-05T08:00",
                  queued: "2024-01-05T08:10",
                  processing: "2024-01-05T08:15",
                  completed: null,
                  rejected: null,
                  failed: "2024-01-05T08:25",
                  expired: null,
                },
                {
                  externalId: 106,
                  statusMessage: null,
                  nodeId: 5,
                  deleted: null,
                  retrieved: "2024-01-05T08:00",
                  queued: null,
                  processing: null,
                  completed: null,
                  rejected: null,
                  failed: null,
                  expired: null,
                },
              ],
              resultsDownloadLog: [
                {
                  user: "asmith",
                  userOrgs: ["AKTIN"],
                  downloadedDate: "2024-01-13T14:45",
                  resultHash: "5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8",
                },
                {
                  user: "asmith",
                  userOrgs: ["AKTIN"],
                  downloadedDate: "2024-01-13T15:30",
                  resultHash: "9c4a6a156b9c51d57227be4dbf6149d1122b85b1",
                },
                {
                  user: "kjohnson",
                  userOrgs: ["AKTIN", "RKI"],
                  downloadedDate: "2024-01-14T09:00",
                  resultHash: "d8e8fca2dc0f896fd7cb4cb0031ba2499cc77390",
                },
              ],
            },
          },
        ]);
      }, 450); // Simulate an async operation
    });
  },

  getNodes() {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve([
          {
            apiKey: "a4f8b2e1",
            tags: ["cardiology", "patient-care", "emergency-care"],
            notes: {
              "2023-01-01": "Note content for 2023-01-01",
              "2023-02-15": "Another note for 2023-02-15",
            },
            id: 1,
            clientDN:
              "CN=BBT Trier,O=Krankenhaus der Barmherzigen Brüder Trier,L=Trier",
            lastContact: "2023-03-10T09:30",
          },

          {
            apiKey: "c7e3f5d2",
            tags: ["diagnostics"],
            notes: {
              "2023-03-05": "Note content for 2023-03-05",
              "2023-04-20": "Additional note for 2023-04-20",
            },
            id: 2,
            clientDN:
              "CN=Ev. Oldenburg,O=Evangelisches Krankenhaus Oldenburg,L=Oldenburg",
            lastContact: "2023-05-02T14:45",
          },
          {
            apiKey: "f9a1b4e6",
            tags: ["orthopedics", "physical-therapy", "urology", "oncology"],
            notes: {
              "2023-06-12": "Note content for 2023-06-12",
            },
            id: 3,
            clientDN:
              "CN=UKSH Lübeck,O=Universitätsklinikum Schleswig-Holstein Campus Lübeck,L=Lübeck",
            lastContact: "2023-08-05T11:15",
          },
          {
            apiKey: "d1e2a3d4",
            tags: ["dermatology", "allergy", "radiology"],
            notes: {
              "2023-09-30": "Note content for 2023-09-30",
              "2023-10-18": "Additional note for 2023-10-18",
            },
            id: 4,
            clientDN: "CN=Charite Mitte,O=Campus Charite Mitte,L=Berlin",
            lastContact: "2023-11-07T17:20",
          },
          {
            apiKey: "b5c6d7e8",
            tags: ["neurology", "mental-health"],
            notes: {},
            id: 5,
            clientDN:
              "CN=Helios Frankfurt,O=HELIOS Klinikum Frankfurt,L=Frankfurt",
            lastContact: "2024-02-10T10:00",
          },
        ]);
      }, 450); // Simulate an async operation
    });
  },

  getPrincipals() {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve([
          {
            name: "John Doe",
            organization: "City General Hospital",
            email: "john.doe@example.com",
            phone: "123-456-7890",
          },
          {
            name: "Jane Smith",
            organization: "Community Health Center",
            email: "jane.smith@example.com",
            phone: "987-654-3210",
          },
          {
            name: "Alice Johnson",
            organization: "Metropolitan Medical Center",
            email: "alice.johnson@example.com",
            phone: "555-123-4567",
          },
          {
            name: "Robert Brown",
            organization: "Regional Health Clinic",
            email: "robert.brown@example.com",
            phone: "456-789-0123",
          },
          {
            name: "Emily Davis",
            organization: "Unity Medical Group",
            email: "emily.davis@example.com",
            phone: "789-012-3456",
          },
          {
            name: "Daniel White",
            organization: "City Health Services",
            email: "daniel.white@example.com",
            phone: "234-567-8901",
          },
          {
            name: "Sophia Wilson",
            organization: "Community Wellness Center",
            email: "sophia.wilson@example.com",
            phone: "567-890-1234",
          },
          {
            name: "Matthew Taylor",
            organization: "Metropolitan Health Solutions",
            email: "matthew.taylor@example.com",
            phone: "890-123-4567",
          },
          {
            name: "Olivia Martinez",
            organization: "City Medical Associates",
            email: "olivia.martinez@example.com",
            phone: "123-234-5678",
          },
          {
            name: "William Clark",
            organization: "Regional Wellness Center",
            email: "william.clark@example.com",
            phone: "234-345-6789",
          },
        ]);
      }, 450); // Simulate an async operation
    });
  },

  getOrganizations() {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve([
          { name: "NuHeal", id: 1 },
          { name: "AllWellth", id: 2 },
          { name: "PalmHealth", id: 3 },
          { name: "QiMed", id: 4 },
          { name: "MediWise", id: 5 },
          { name: "Medispa", id: 6 },
          { name: "LabHealthy", id: 7 },
          { name: "SafeMed", id: 8 },
          { name: "MindHeal", id: 9 },
          { name: "ProHealthy", id: 10 },
        ]);
      });
    });
  },
};

export default TestDataService;
