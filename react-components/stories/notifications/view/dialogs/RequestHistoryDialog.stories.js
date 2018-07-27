/**
 *
 * @author sriram
 *
 */
import React, { Component } from "react";
import RequestHistoryDialog from "../../../../src/notifications/view/dialogs/RequestHistoryDialog";

class RequestHistoryDialogTest extends Component {
    render() {
        const logger = this.props.logger || ((Notification) => {
                console.log(Notification);
            });
        const history =  [{
            "status": "Submitted",
            "status_date": 1532546191001,
            "updated_by": "sriram@iplantcollaborative.org",
            "comments": ""
        }, {
            "status": "Pending",
            "status_date": 1532546304911,
            "updated_by": "sriram@iplantcollaborative.org",
            "comments": "this is a pending test"
        }, {
            "status": "Evaluation",
            "status_date": 1532546322556,
            "updated_by": "sriram@iplantcollaborative.org",
            "comments": "evaluation done..."
        }];
        return (
            <RequestHistoryDialog dialogOpen={true}
                                  toolName = "Test"
                                  history={history}
                             />
        );
    }
}

export default RequestHistoryDialogTest;