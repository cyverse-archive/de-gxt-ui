/**
 *
 * @author sriram
 *
 */

import React from "react";
import PropTypes from "prop-types";
import { injectIntl } from "react-intl";
import withI18N, { getMessage } from "../I18NWrapper";
import intlData from "./messages";
import DialogContentText from "@material-ui/core/DialogContentText";
import Dialog from "@material-ui/core/Dialog";
import DEDialogHeader from "./DEDialogHeader";
import DialogContent from "@material-ui/core/DialogContent";
import DialogActions from "@material-ui/core/DialogActions";
import Button from "@material-ui/core/Button";

DEAlertDialog.propTypes = {
    dialogOpen: PropTypes.bool.isRequired,
    heading: PropTypes.string.isRequired,
    alertMessage: PropTypes.string.isRequired,
    handleClose: PropTypes.func.isRequired,
};

function DEAlertDialog(props) {
    const [dialogOpen, heading, alertMessage, handleClose] = props;
    return (
        <Dialog
            open={dialogOpen}
            onClose={handleClose}
            aria-describedby="alert-dialog-description"
        >
            <DEDialogHeader heading={heading} onClose={handleClose} />
            <DialogContent>
                <DialogContentText id="alert-dialog-description">
                    {alertMessage}
                </DialogContentText>
            </DialogContent>
            <DialogActions>
                <Button onClick={this.handleClose} color="primary" autoFocus>
                    {getMessage("okBtnText")}
                </Button>
            </DialogActions>
        </Dialog>
    );
}

export default withI18N(injectIntl(DEAlertDialog), intlData);
