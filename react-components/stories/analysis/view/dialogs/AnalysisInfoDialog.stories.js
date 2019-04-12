import React, { Component } from "react";
import AnalysisInfoDialog from "../../../../src/analysis/view/dialogs/AnalysisInfoDialog";

class AnalysisInfoDialogTest extends Component {
    render() {
        const info = {
            analysis_id: "6869ed8f-ab38-4aaf-bb12-d0842e9fcb73",
            steps: [
                {
                    step_number: 1,
                    external_id: "853900453991617001-242ac116-0001-007",
                    startdate: "1553818229058",
                    enddate: "1553818332000",
                    status: "Completed",
                    app_step_number: 1,
                    step_type: "Agave",
                    updates: [
                        {
                            status: "PENDING",
                            message: "Job accepted and queued for submission.",
                            timestamp: "1553818231000",
                        },
                        {
                            status: "PROCESSING_INPUTS",
                            message: "Attempt 1 to stage job inputs",
                            timestamp: "1553818247000",
                        },
                        {
                            status: "PROCESSING_INPUTS",
                            message: "Identifying input files for staging",
                            timestamp: "1553818247000",
                        },
                        {
                            status: "STAGING_INPUTS",
                            message: "Copy in progress",
                            timestamp: "1553818253000",
                        },
                        {
                            status: "STAGED",
                            message: "Job inputs staged to execution system",
                            timestamp: "1553818256000",
                        },
                        {
                            status: "SUBMITTING",
                            message: "Preparing job for submission.",
                            timestamp: "1553818275000",
                        },
                        {
                            status: "SUBMITTING",
                            message: "Attempt 1 to submit job",
                            timestamp: "1553818275000",
                        },
                        {
                            status: "STAGING_JOB",
                            message:
                                "Fetching app assets from agave://data.iplantcollaborative.org/${foundation.service.apps.default.public.dir}/cut_columns-0.0.0u1.zip",
                            timestamp: "1553818278000",
                        },
                        {
                            status: "STAGING_JOB",
                            message:
                                "Staging runtime assets to agave://cyverseUK-Batch2/sarahr/job-853900453991617001-242ac116-0001-007-6869ed8f-ab38-4aaf-bb12-d0842e9fcb73_0001",
                            timestamp: "1553818283000",
                        },
                        {
                            status: "QUEUED",
                            message:
                                "CLI job successfully forked as process id 928228",
                            timestamp: "1553818304000",
                        },
                        {
                            status: "RUNNING",
                            message:
                                "CLI job successfully forked as process id 928228",
                            timestamp: "1553818304000",
                        },
                        {
                            status: "RUNNING",
                            message:
                                "Job receieved duplicate RUNNING notification",
                            timestamp: "1553818307000",
                        },
                        {
                            status: "CLEANING_UP",
                            message:
                                "Job completion detected by process monitor.",
                            timestamp: "1553818332000",
                        },
                        {
                            status: "ARCHIVING",
                            message: "Beginning to archive output.",
                            timestamp: "1553818335000",
                        },
                        {
                            status: "ARCHIVING",
                            message: "Attempt 1 to archive job output",
                            timestamp: "1553818335000",
                        },
                        {
                            status: "ARCHIVING",
                            message:
                                "Archiving agave://cyverseUK-Batch2/sarahr/job-853900453991617001-242ac116-0001-007-6869ed8f-ab38-4aaf-bb12-d0842e9fcb73_0001 to agave://qairods.cyverse.org//sarahr/analyses_qa/cut_201903281639-2019-03-29-00-10-15.5",
                            timestamp: "1553818340000",
                        },
                        {
                            status: "ARCHIVING_FINISHED",
                            message: "Job archiving completed successfully.",
                            timestamp: "1553818415000",
                        },
                        {
                            status: "FINISHED",
                            message: "Job complete",
                            timestamp: "1553818416000",
                        },
                    ],
                },
                {
                    step_number: 2,
                    external_id: "2c6e0013-7c62-4f2f-a076-656c61115786",
                    startdate: "1553126468806",
                    enddate: "1553126512573",
                    status: "Completed",
                    app_step_number: 1,
                    step_type: "DE",
                    updates: [
                        {
                            status: "Submitted",
                            message: "Launched Condor ID 262633",
                            timestamp: "1553126469309",
                        },
                        {
                            status: "Running",
                            message:
                                "Job 2c6e0013-7c62-4f2f-a076-656c61115786 is running on host oscar.cyverse.org",
                            timestamp: "1553126482935",
                        },
                        {
                            status: "Running",
                            message: "creating data container data_0_0",
                            timestamp: "1553126484788",
                        },
                        {
                            status: "Running",
                            message: "Downloading input_path.list",
                            timestamp: "1553126486099",
                        },
                        {
                            status: "Running",
                            message:
                                "finished creating data container data_0_0",
                            timestamp: "1553126486099",
                        },
                        {
                            status: "Running",
                            message: "finished downloading input_path.list",
                            timestamp: "1553126492212",
                        },
                        {
                            status: "Running",
                            message:
                                "Running tool container discoenv/url-import:latest with arguments: arbitrary.json",
                            timestamp: "1553126492212",
                        },
                        {
                            status: "Running",
                            message:
                                "Tool container discoenv/url-import:latest with arguments 'arbitrary.json' finished successfully",
                            timestamp: "1553126493737",
                        },
                        {
                            status: "Running",
                            message:
                                "Beginning to upload outputs to /iplant/home/sarahr/analyses_qa/dewc_201903201700-2019-03-21-00-01-08.8",
                            timestamp: "1553126493738",
                        },
                        {
                            status: "Running",
                            message:
                                "Done uploading outputs to /iplant/home/sarahr/analyses_qa/dewc_201903201700-2019-03-21-00-01-08.8",
                            timestamp: "1553126512572",
                        },
                        {
                            status: "Completed",
                            message: "UNKNOWN",
                            timestamp: "1553126512573",
                        },
                    ],
                },
            ],
            timestamp: "1553818603053",
            total: 1,
        };

        return <AnalysisInfoDialog info={info} dialogOpen={true} />;
    }
}

export default AnalysisInfoDialogTest;
