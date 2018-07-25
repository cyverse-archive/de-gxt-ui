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


class DenyJoinRequestDialog extends Component {
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
                        style={{color: Color.white}}> {getMessage("denyRequestHeader")}</Typography>
                </DialogTitle>
                <DialogContent>
                    <DialogContentText id="alert-dialog-description">
                        {getMessage("denyRequestMessage", {
                            values: {
                                name: this.props.name,
                                team: this.props.team
                            }
                        })}
                        <TextField
                            id="full-width"
                            InputLabelProps={{
                                shrink: true,
                            }}
                            placeholder="Message"
                            fullWidth
                            margin="normal"
                        />
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={this.handleClose} color="primary">
                        {getMessage("okBtnText")}
                    </Button>
                    <Button onClick={this.handleClose} color="primary" autoFocus>
                        {getMessage("cancelBtnText")}
                    </Button>
                </DialogActions>
            </Dialog>
        );
    }
}
export default withI18N(DenyJoinRequestDialog, intlData);