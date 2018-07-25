/**
 *
 * @author sriram
 *
 */
import React, { Component } from "react";
import ApproveJoinRequestDialog from "../../../../src/notifications/view/dialogs/ApproveJoinRequestDialog";

class ApproveJoinRequestDialogTest extends Component {
    render() {
        const logger = this.props.logger || ((Notification) => {
                console.log(Notification);
            });
        return (
            <ApproveJoinRequestDialog open={true}
                                      privilege="admin"
                                      name="Ipc Dev"
                                      team="test"
                                      handleChange={() => logger("privilege changed")}/>
        );
    }
}

export default ApproveJoinRequestDialogTest;