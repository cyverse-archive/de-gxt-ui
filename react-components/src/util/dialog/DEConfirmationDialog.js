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

export const TWO_BUTTON_VARIANT = "twoButtonVariant";
export const THREE_BUTTON_VARIANT = "threeButtonVariant";

class DEConfirmationDialog extends Component {
    render() {
        const {
            heading,
            message,
            dialogOpen,
            variant,
            onOkBtnClick,
            onCancelBtnClick,
            onOptionalBtnClick,
            debugId,
            okLabel,
            cancelLabel,
            optionalLabel,
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
                    {variant === THREE_BUTTON_VARIANT && (
                        <Button
                            id={build(debugId, ids.OPTIONAL)}
                            onClick={() => {
                                if (onOptionalBtnClick) {
                                    onOptionalBtnClick();
                                }
                            }}
                            color="primary"
                        >
                            {optionalLabel}
                        </Button>
                    )}
                    <Button
                        id={build(debugId, ids.CANCEL)}
                        onClick={onCancelBtnClick}
                        color="primary"
                    >
                        {cancelLabel}
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
    cancelLabel: getMessage("cancelBtnText"),
    closeLabel: getMessage("closeBtnText"),
    variant: TWO_BUTTON_VARIANT,
};

DEConfirmationDialog.propTypes = {
    dialogOpen: PropTypes.bool.isRequired,
    message: PropTypes.oneOfType([PropTypes.string, PropTypes.object])
        .isRequired,
    heading: PropTypes.oneOfType([PropTypes.string, PropTypes.object])
        .isRequired,
    onOkBtnClick: PropTypes.func.isRequired,
    onCancelBtnClick: PropTypes.func.isRequired,
    onOptionalBtnClick: PropTypes.func,
    debugId: PropTypes.string.isRequired,
    okLabel: PropTypes.string,
    cancelLabel: PropTypes.string,
    optionalLabel: PropTypes.string,
    variant: PropTypes.oneOf([TWO_BUTTON_VARIANT, THREE_BUTTON_VARIANT]),
};
export default withI18N(DEConfirmationDialog, intlData);
