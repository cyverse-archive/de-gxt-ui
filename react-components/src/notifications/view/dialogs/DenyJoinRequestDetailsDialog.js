/**
 * A dialog that shows details of denying a request to join a team
 *
 * @author Sriram
 *
 **/
import React, { Component } from "react";
import intlData from "../../messages";
import ids from "../../ids";

import { build, getMessage, palette, withI18N } from "@cyverse-de/ui-lib";

import Button from "@material-ui/core/Button";
import Dialog from "@material-ui/core/Dialog";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import DialogContentText from "@material-ui/core/DialogContentText";
import DialogTitle from "@material-ui/core/DialogTitle";

import Typography from "@material-ui/core/Typography";
import TextField from "@material-ui/core/TextField";

class DenyJoinRequestDetailsDialog extends Component {
    constructor(props) {
        super(props);
        this.state = {
            dialogOpen: props.dialogOpen,
        };
    }

    render() {
        const { teamName, adminMessage } = this.props;
        const baseId = ids.DENY_REQUEST_DLG;
        return (
            <Dialog id={baseId} open={this.state.dialogOpen}>
                <DialogTitle style={{ backgroundColor: palette.blue }}>
                    <Typography style={{ color: palette.white }}>
                        {" "}
                        {getMessage("denyDetailsHeader")}
                    </Typography>
                </DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        <Typography>
                            {getMessage("denyDetailsMessage", {
                                values: {
                                    team: teamName,
                                },
                            })}
                        </Typography>
                        <p style={{ marginTop: 10 }}>
                            {getMessage("denyAdminLabel")}
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
                        </p>
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button
                        id={build(baseId, ids.OK_BTN)}
                        onClick={() => {
                            this.setState({ dialogOpen: false });
                        }}
                        color="primary"
                    >
                        {getMessage("okBtnText")}
                    </Button>
                </DialogActions>
            </Dialog>
        );
    }
}
export default withI18N(DenyJoinRequestDetailsDialog, intlData);
