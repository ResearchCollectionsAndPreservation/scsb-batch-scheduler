package org.recap;

/**
 * Created by rajeshbabuk on 7/4/17.
 */
public class ScsbConstants {

    public static final String SUCCESS = "SUCCESS";
    public static final String SCHEDULE = "Schedule";
    public static final String RESCHEDULE = "Reschedule";
    public static final String UNSCHEDULE = "Unschedule";
    public static final String UNSCHEDULED = "Unscheduled";
    public static final String JOB_NAME = "jobName";
    public static final String JOB_LAUNCHER = "jobLauncher";
    public static final String JOB_LOCATOR = "jobLocator";
    public static final String TRIGGER_SUFFIX = "_Trigger";
    public static final String ERROR_INVALID_CRON_EXPRESSION = "Cron expression is invalid.";
    public static final String ERROR_JOB_FAILED_SCHEDULING = "Job failed to be scheduled.";
    public static final String ERROR_JOB_FAILED_RESCHEDULING = "Job failed to be rescheduled.";
    public static final String ERROR_JOB_FAILED_UNSCHEDULING = "Job failed to be unscheduled.";
    public static final String JOB_SUCCESS_SCHEDULING = "Job successfully scheduled.";
    public static final String JOB_SUCCESS_RESCHEDULING = "Job successfully rescheduled.";
    public static final String JOB_SUCCESS_UNSCHEDULING = "Job successfully unscheduled.";
    public static final String GENERATE_ACCESSION_REPORT_JOB = "GenerateAccessionReport";
    public static final String PURGE_EMAIL_URL = "purge/purgeEmailAddress";
    public static final String PURGE_EXCEPTION_REQUEST_URL = "purge/purgeExceptionRequests";
    public static final String PURGE_ACCESSION_REQUEST_URL = "purge/purgeAccessionRequests";
    public static final String MATCHING_ALGORITHM_URL = "ongoingMatchingAlgorithmService/ongoingMatchingAlgorithmJob";
    public static final String GENERATE_REPORT_URL = "generateReportService/generateReports";
    public static final String REPORT_DELETED_RECORDS_URL = "reportDeleted/records";
    public static final String DATA_EXPORT_ETL_URL = "dataDump/exportDataDump/?institutionCodes={institutionCodes}&requestingInstitutionCode={requestingInstitutionCode}&imsDepositoryCodes={imsDepositoryCodes}&fetchType={fetchType}&outputFormat={outputFormat}&date={date}&emailToAddress={emailToAddress}&userName={userName}";
    public static final String DATA_EXPORT_JOB_SEQUENCE_URL = "dataDumpSequence/exportDataDumpSequence/?date={date}";
    public static final String DATA_EXPORT_TRIGGER_JOB_URL = "dataDump/exportDataDumpTriggerManually";


    public static final String ACCESSION_URL = "sharedCollection/ongoingAccessionJob";
    public static final String BATCH_JOB_EMAIL_URL = "batchJobEmailService/batchJobEmail";
    public static final String UPDATE_JOB_URL = "updateJobService/updateJob";
    public static final String GET_ALL_JOBS_URL = "updateJobService/getAllJobs";
    public static final String GET_JOB_BY_NAME_URL = "updateJobService/getJobByName";
    public static final String GET_JOB_PARAMS_BY_JOB_NAME_URL = "updateJobService/getJobParamsByJobName";
    public static final String DAILY_RECONCILIATION_URL = "/dailyReconciliation/startDailyReconciliation";
    public static final String ACCESSION_RECOCILIATION_URL = "/accessionReconciliation/startAccessionReconciliation";
    public static final String STATUS_RECONCILIATION_URL = "/statusReconciliation/itemStatusReconciliation";
    public static final String REQUEST_DATA_LOAD_URL = "/requestInitialDataLoad/startRequestInitialLoad";

    public static final String PURGE_EMAIL_ADDRESS = "PurgeEmailAddress";
    public static final String DAILY_LAS_TRANSACTION_RECONCILIATION = "DailyLASTransactionReconciliation";
    public static final String GENERATE_ACCESSION_REPORT = "GenerateAccessionReport";
    public static final String ACCESSION = "Accession";
    public static final String ACCESSION_MATCHING_JOBS_SEQUENCE = "AccessionToDataExportJobsInSequence";
    public static final String PURGE_ACCESSION_REQUESTS = "PurgeCompletedAccessions";

    public static final String SUBMIT_COLLECTION_URL = "/submitCollectionJob/startSubmitCollection";
    public static final String NOTIFY_IF_PENDING_REQUEST = "/notifyPendingRequest/sendEmailForPendingRequest";
    public static final String CHECK_PENDING_REQUEST_IN_DB = "/identifyPendingRequest/identifyAndNotifyPendingRequests";

    public static final String DELETED_RECORDS_EXPORT = "DeletedRecordsExport";
    public static final String INCREMENTAL_RECORDS_EXPORT = "IncrementalRecordsExport";
    public static final String DATE = "date";
    public static final String EXPORT_DATE_FORMAT = "yyyy-MM-dd HH:mm";
    public static final String FETCH_TYPE = "fetchType";
    public static final String FROM_DATE = "fromDate";
    public static final String FROM_DATE_FORMAT = "yyyy-MM-dd";
    public static final String EMAIL_TO_ADDRESS = "emailToAddress";
    public static final String USER_NAME = "userName";
    public static final String SCHEDULER = "SCHEDULER";

    public static final String JOB_STATUS = "JobStatus";
    public static final String JOB_STATUS_MESSAGE = "JobStatusMessage";
    public static final String FAILURE = "FAILURE";

    public static final String ACCESSION_STATUS_NAME = "[Ongoing Accession]";
    public static final String ACCESSION_REPORT_STATUS_NAME = "[Ongoing Accession Report]";
    public static final String SUBMIT_COLLECTION_STATUS_NAME = "[Submit Collection]";
    public static final String MATCHING_ALGORITHM_STATUS_NAME = "[Ongoing Matching Algorithm]";
    public static final String DATA_EXPORT_STATUS_NAME = "[Data Export]";
    public static final String ACCESSION_RECONCILIATION_STATUS_NAME = "[Accession Reconciliation]";
    public static final String CGD_ROUND_TRIP_REPORTS_STATUS_NAME = "[CGD Round Trip Reports]";
    public static final String CHECK_AND_NOTIFY_PENDING_REQUEST_STATUS_NAME = "[Check And Notify Pending Request]";
    public static final String IDENTIFY_AND_NOTIFY_PENDING_REQUEST_STATUS_NAME = "[Identify And Notify Pending Request]";
    public static final String DAILY_RECONCILIATION_STATUS_NAME = "[Daily Reconciliation]";
    public static final String DELETED_RECORDS_STATUS_NAME = "[Deleted Records]";
    public static final String JOB_SEQUENCE_STATUS_NAME = "[Job Sequence]";
    public static final String PURGE_ACCESSION_REQUEST_STATUS_NAME = "[Purge Accession Request]";
    public static final String PURGE_EXCEPTION_REQUEST_STATUS_NAME = "[Purge Exception Request]";
    public static final String PURGE_EMAIL_ADDRESS_STATUS_NAME = "[Purge Email Address]";
    public static final String REQUEST_INITIAL_LOAD_STATUS_NAME = "[Request Initial Load]";
    public static final String STATUS_RECONCILIATION_STATUS_NAME = "[Status Reconciliation]";

    public static final String RAN = "ran";
    public static final String STARTED = "started";
    public static final String RUNNING = "Running";
    public static final String POLLING_ACTION = "action";
    public static final String START = "start";
    public static final String STOP = "stop";
    public static final String GENERATE_CDG_ROUND_TRIP_REPORT_JOB = "GenrateCgdRoundTripReport" ;
    public static final String GENERATE_CGD_ROUND_TRIP_REPORT_URL = "ongoingMatchingAlgorithmService/generateCGDRoundTripReport";

    public static boolean POLL_LONG_RUNNING_JOBS = true;

    public static final String FAILURE_QUEUE_MESSAGE = "Message undelivered or unreceived.";
    public static final String NO_REQUESTING_INSTITUTION = "Requesting Institution is blank. Please provide requesting institution for export.";

    public static final String GATEWAY_REQUEST_LOG_FOR_EMAIL_NOTIFICATION = "/requestsEmailNotification";

    public static final String GATEWAY_REQUEST_LOG_NOTIFICATION= "gateway request log notification execution started";
    private ScsbConstants(){}
}
