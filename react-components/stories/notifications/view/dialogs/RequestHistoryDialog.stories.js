/**
 *
 * @author sriram
 *
 */
import React, { Component } from "react";
import RequestHistoryDialog from "../../../../src/notifications/view/dialogs/RequestHistoryDialog";
import notificationCategory from "../../../../src/notifications/model/notificationCategory";

class RequestHistoryDialogTest extends Component {
    render() {
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
                                  name="Test"
                                  history={history}
                                  category={notificationCategory.permanent_id_request}
            />
        );
    }
}

export default RequestHistoryDialogTest;