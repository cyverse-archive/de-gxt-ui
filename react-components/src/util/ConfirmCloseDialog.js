/**
 * @author psarando
 */
import React, { Component } from "react";
import PropTypes from "prop-types";

import Button from "@material-ui/core/Button";
import Dialog from "@material-ui/core/Dialog";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import DialogTitle from "@material-ui/core/DialogTitle";

import build from "./DebugIDUtil";

class ConfirmCloseDialog extends Component {
    static propTypes = {
        onConfirm: PropTypes.func.isRequired,
        onClose: PropTypes.func.isRequired,
        onCancel: PropTypes.func.isRequired,
    };

    render() {
        const {
            open,
            parentId,
            onConfirm,
            onClose,
            onCancel,
            title,
            dialogContent,
            confirmLabel,
            closeLabel,
            cancelLabel,
        } = this.props;

        const dialogID = build(parentId, "confirmCloseDialog");
        const dialogTitleID = build(dialogID, "title");

        return (
            <Dialog open={open}
                    onClose={onCancel}
                    aria-labelledby={dialogTitleID}
            >
                <DialogTitle id={dialogTitleID}>
                    {title}
                </DialogTitle>
                <DialogContent>
                    {dialogContent}
                </DialogContent>
                <DialogActions>
                    <Button id={build(dialogID, "confirmBtn")}
                            color="primary"
                            onClick={onConfirm}
                    >
                        {confirmLabel}
                    </Button>
                    <Button id={build(dialogID, "closeBtn")}
                            color="primary"
                            onClick={onClose}
                    >
                        {closeLabel}
                    </Button>
                    <Button id={build(dialogID, "cancelBtn")}
                            onClick={onCancel}
                    >
                        {cancelLabel}
                    </Button>
                </DialogActions>
            </Dialog>
        );
    }
}

export default ConfirmCloseDialog;
