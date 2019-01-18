/**
 *  @author sriram
 *
 **/
import React, { Component } from 'react';
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
        const {heading, message, dialogOpen, onOkBtnClick, onCancelBtnClick, debugId} = this.props;
        return (
            <Dialog open={dialogOpen} id={debugId}>
                <DEDialogHeader
                    heading={heading}
                    onClose={() => {
                        if (onCancelBtnClick) {
                            onCancelBtnClick();
                        }
                    }}/>
                <DialogContent>
                    {message}
                </DialogContent>
                <DialogActions>
                    <Button id={build(debugId, ids.CANCEL)}
                            onClick={() => {
                        if (onCancelBtnClick) {
                            onCancelBtnClick();
                        }
                    }} color="primary">
                        {getMessage("cancel")}
                    </Button>
                    <Button
                        id={build(debugId, ids.OK)}
                        variant="contained"
                        onClick={onOkBtnClick}
                        color="primary">
                        {getMessage("ok")}
                    </Button>
                </DialogActions>
            </Dialog>
        );
    }
}

DEConfirmationDialog.propTypes = {
    dialogOpen: PropTypes.bool.isRequired,
    message: PropTypes.string.isRequired,
    heading: PropTypes.string.isRequired,
    onOkBtnClick: PropTypes.func.isRequired,
    onCancelBtnClick: PropTypes.func,
    debugId: PropTypes.string.isRequired,
};
export default withI18N(DEConfirmationDialog, intlData);