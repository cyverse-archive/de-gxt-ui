/**
 * A dialog that shows details of denying a request to join a team
 *
 * @author Sriram
 *
 **/
import React, { Component } from "react";
import Dialog from "@material-ui/core/Dialog";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import DialogContentText from "@material-ui/core/DialogContentText";
import DialogTitle from "@material-ui/core/DialogTitle";
import withI18N, { getMessage } from "../../../util/I18NWrapper";
import Button from "@material-ui/core/Button";
import intlData from "../../messages";
import Color from "../../../util/CyVersePalette";
import Typography from "@material-ui/core/Typography";
import TextField from "@material-ui/core/TextField";
import build from "../../../util/DebugIDUtil";
import ids from "../../ids";

class DenyJoinRequestDetailsDialog extends Component {
    constructor(props) {
        super(props);
        this.state = {
            dialogOpen: props.dialogOpen,
        };
    }

    render() {
        const {teamName, adminMessage} = this.props;
        const baseId = ids.DENY_REQUEST_DLG;
        return (
            <Dialog
                id={baseId}
                open={this.state.dialogOpen}
            >
                <DialogTitle style={{backgroundColor: Color.blue}}>
                    <Typography
                        style={{color: Color.white}}> {getMessage("denyDetailsHeader")}</Typography>
                </DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        <Typography>{getMessage("denyDetailsMessage", {
                            values: {
                                team: teamName
                            }
                        })}</Typography>
                        <p style={{marginTop: 10}}>
                            {getMessage("denyAdminLabel")}
                            <TextField
                                InputLabelProps={{
                                    shrink: true,
                                    readOnly: true
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
                            this.setState({dialogOpen: false})
                        }}
                        color="primary">
                        {getMessage("okBtnText")}
                    </Button>
                </DialogActions>
            </Dialog>
        );
    }
}
export default withI18N(DenyJoinRequestDetailsDialog, intlData);