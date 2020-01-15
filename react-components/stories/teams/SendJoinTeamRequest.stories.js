import React, { Component } from "react";

import SendJoinTeamRequestDialog from "../../src/teams/edit/SendJoinTeamRequestDialog";

class SendJoinTeamRequestTest extends Component {
    render() {
        const logger =
            this.props.logger ||
            ((data) => {
                console.log(data);
            });

        const presenter = {
            closeJoinTeamRequestDlg: () =>
                logger("Close Send Join Team Request Dialog"),
            sendRequestToJoin: (teamName, message) =>
                logger("Send Request to Join", teamName, message),
        };

        return (
            <SendJoinTeamRequestDialog
                teamName="Bestest Team"
                presenter={presenter}
                loading={false}
                open={true}
            />
        );
    }
}

export default SendJoinTeamRequestTest;
