/**
 *
 * @author sriram
 *
 */
import React, { Component } from "react";
import DenyJoinRequestDetailsDialog from "../../../../src/notifications/view/dialogs/DenyJoinRequestDetailsDialog";

class DenyJoinRequestDetailsDialogTest extends Component {
    render() {
        const logger = this.props.logger || ((Notification) => {
                console.log(Notification);
            });
        return (
            <DenyJoinRequestDetailsDialog open={true}
                                          team="test"
                                          message="Nope. You are not cool."/>
        );
    }
}

export default DenyJoinRequestDetailsDialogTest;