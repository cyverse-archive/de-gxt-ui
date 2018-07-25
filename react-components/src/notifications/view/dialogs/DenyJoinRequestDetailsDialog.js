/**
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


class DenyJoinRequestDetailsDialog extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <Dialog
                open={this.props.open}
                onClose={this.props.handleDenyJoinRequestClose}
                aria-labelledby="alert-dialog-title"
                aria-describedby="alert-dialog-description"
            >
                <DialogTitle style={{backgroundColor: Color.blue}}
                             id="alert-dialog-title">
                    <Typography
                        style={{color: Color.white}}> {getMessage("denyDetailsHeader")}</Typography>
                </DialogTitle>
                <DialogContent>
                    <DialogContentText id="alert-dialog-description">
                        {getMessage("denyDetailsMessage", {
                            values: {
                                team: this.props.team
                            }
                        })}
                        <div style={{marginTop: 10}}>
                            <Typography subheading>{getMessage("denyAdminLabel")}</Typography>
                            <TextField
                                id="full-width"
                                InputLabelProps={{
                                    shrink: true,
                                }}
                                fullWidth
                                margin="normal"
                                value={this.props.message}
                                disabled={true}
                            />
                        </div>
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={this.handleClose} color="primary">
                        {getMessage("okBtnText")}
                    </Button>
                </DialogActions>
            </Dialog>
        );
    }
}
export default withI18N(DenyJoinRequestDetailsDialog, intlData);