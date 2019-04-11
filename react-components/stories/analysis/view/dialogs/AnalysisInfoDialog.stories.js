import React, { Component } from "react";
import AnalysisInfoDialog from "../../../../src/analysis/view/dialogs/AnalysisInfoDialog";

class AnalysisInfoDialogTest extends Component {
    render() {
        const info = {
            analysis_id: "71380ffa-9a9a-11e8-9ac7-f64e9b87c109",
            steps: [
                {
                    step_number: 1,
                    external_id: "c4e5fc28-31ae-41b7-a013-e2e56555e808",
                    startdate: "1533684874626",
                    enddate: "1533684920402",
                    status: "Failed",
                    app_step_number: 1,
                    step_type: "DE",
                },
            ],
            timestamp: "1534889180472",
            total: 1,
        };
        return <AnalysisInfoDialog info={info} dialogOpen={true} />;
    }
}

export default AnalysisInfoDialogTest;
