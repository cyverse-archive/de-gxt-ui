/**
 *
 * @author sriram
 *
 */
import React, { Component } from "react";
import DenyJoinRequestDialog from "../../../../src/notifications/view/dialogs/DenyJoinRequestDialog";

class DenyJoinRequestDialogTest extends Component {
    render() {
        const logger = this.props.logger || ((Notification) => {
                console.log(Notification);
            });
        return (
            <DenyJoinRequestDialog open={true}
                                   name="Ipc Dev"
                                   team="test"/>
        );
    }
}

export default DenyJoinRequestDialogTest;