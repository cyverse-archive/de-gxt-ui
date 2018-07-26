/**
 *
 * @author sriram
 *
 */
import React, { Component } from "react";
import JoinTeamRequestDialog from "../../../../src/notifications/view/dialogs/JoinTeamRequestDialog";

class JoinTeamRequestDialogTest extends Component {
    render() {
        const logger = this.props.logger || ((Notification) => {
                console.log(Notification);
            });
        const request = {
            action: "team_join_request",
            email_address: "sriram@iplantcollaborative.org",
            requester_email: "core-sw@iplantcollaborative.org",
            requester_id: "ipcdev",
            requester_message: "I would like join...",
            requester_name: "Ipc Dev",
            team_name: "sriram:sriram-test",
        }
        return (
            <JoinTeamRequestDialog dialogOpen={true}
                                   handleJoinTeamRequestDialogClose={() => logger("dialog closed")}
                                   request={request}
                                   />
        );
    }
}

export default JoinTeamRequestDialogTest;