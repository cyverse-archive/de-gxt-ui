/**
 *  @author sriram psarando
 *
 **/
import React, { Component } from "react";
import Dialog from "@material-ui/core/Dialog/Dialog";
import withI18N, { getMessage } from "../I18NWrapper";
import DialogContent from "@material-ui/core/DialogContent/DialogContent";
import Button from "@material-ui/core/Button/Button";
import DialogActions from "@material-ui/core/DialogActions/DialogActions";
import DEDialogHeader from "./DEDialogHeader";
import intlData from "./messages";
import PropTypes from "prop-types";
import ids from "./ids";
import build from "../DebugIDUtil";

class DEConfirmationDialog extends Component {
    render() {
        const {
            heading,
            message,
            dialogOpen,
            onOkBtnClick,
            onCancelBtnClick,
            debugId,
            okLabel,
            messages,
        } = this.props;
        const dialogTitleID = build(debugId, "title");

        return (
            <Dialog
                id={debugId}
                open={dialogOpen}
                aria-labelledby={dialogTitleID}
            >
                <DEDialogHeader
                    id={dialogTitleID}
                    messages={messages}
                    heading={heading}
                    onClose={onCancelBtnClick}
                />
                <DialogContent>{message}</DialogContent>
                <DialogActions>
                    <Button
                        id={build(debugId, ids.CANCEL)}
                        onClick={onCancelBtnClick}
                        color="primary"
                    >
                        {getMessage("cancelBtnText")}
                    </Button>
                    <Button
                        id={build(debugId, ids.OK)}
                        variant="contained"
                        onClick={onOkBtnClick}
                        color="primary"
                    >
                        {okLabel}
                    </Button>
                </DialogActions>
            </Dialog>
        );
    }
}

DEConfirmationDialog.defaultProps = {
    okLabel: getMessage("okBtnText"),
};

DEConfirmationDialog.propTypes = {
    dialogOpen: PropTypes.bool.isRequired,
    message: PropTypes.oneOfType([PropTypes.string, PropTypes.object])
        .isRequired,
    heading: PropTypes.oneOfType([PropTypes.string, PropTypes.object])
        .isRequired,
    onOkBtnClick: PropTypes.func.isRequired,
    onCancelBtnClick: PropTypes.func.isRequired,
    debugId: PropTypes.string.isRequired,
};
export default withI18N(DEConfirmationDialog, intlData);
