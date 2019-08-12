/**
 * A dialog that shows details of denying a request to join a team
 *
 * @author Sriram
 *
 **/
import React, { Component } from "react";
import { injectIntl } from "react-intl";
import intlData from "../../messages";
import ids from "../../ids";

import {
    build,
    DEDialogHeader,
    formatMessage,
    getMessage,
    withI18N,
} from "@cyverse-de/ui-lib";

import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    Typography,
    TextField,
} from "@material-ui/core";

class DenyJoinRequestDetailsDialog extends Component {
    constructor(props) {
        super(props);
        this.state = {
            dialogOpen: props.dialogOpen,
        };
    }

    render() {
        const { teamName, adminMessage, intl } = this.props;
        const baseId = ids.DENY_REQUEST_DLG;
        return (
            <Dialog id={baseId} open={this.state.dialogOpen}>
                <DEDialogHeader
                    heading={formatMessage(intl, "denyDetailsHeader")}
                    onClose={() => {
                        this.setState({ dialogOpen: false });
                    }}
                />
                <DialogContent>
                    <Typography component={"span"}>
                        {getMessage("denyDetailsMessage", {
                            values: {
                                team: teamName,
                            },
                        })}
                    </Typography>

                    <Typography component={"span"}>
                        {getMessage("denyAdminLabel")}
                    </Typography>
                    <TextField
                        InputLabelProps={{
                            shrink: true,
                        }}
                        InputProps={{
                            readOnly: true,
                        }}
                        fullWidth
                        margin="normal"
                        value={adminMessage}
                    />
                </DialogContent>
                <DialogActions>
                    <Button
                        id={build(baseId, ids.OK_BTN)}
                        onClick={() => {
                            this.setState({ dialogOpen: false });
                        }}
                        color="primary"
                        variant="contained"
                    >
                        {getMessage("okBtnText")}
                    </Button>
                </DialogActions>
            </Dialog>
        );
    }
}

export default withI18N(injectIntl(DenyJoinRequestDetailsDialog), intlData);
