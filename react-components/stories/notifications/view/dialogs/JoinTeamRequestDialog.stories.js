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
        return (
            <JoinTeamRequestDialog open={true}
                                   handleJoinTeamRequestDialogClose={() => logger("dialog closed")}
                                   team="test team"
                                   name="sriram"
                                   email="sriram@email.arizona.edu"
                                   message="I want in...I want in...I want in...I want in...I want in...I want in...I want in...I want in...I want in...I want in...I want in...I want in..."/>
        );
    }
}

export default JoinTeamRequestDialogTest;