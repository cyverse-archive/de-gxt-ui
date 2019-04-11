/**
 *  @author sriram
 *
 * Rewrite IPlantPromptDialog in react.
 **/

import React, { Component } from "react";
import PropTypes from "prop-types";
import Dialog from "@material-ui/core/Dialog";
import DEDialogHeader from "./DEDialogHeader";
import withI18N, { getMessage } from "../I18NWrapper";
import TextField from "../../../node_modules/@material-ui/core/TextField/TextField";
import Button from "../../../node_modules/@material-ui/core/Button/Button";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import intlData from "./messages";

class DEPromptDialog extends Component {
    constructor(props) {
        super(props);
        this.state = {
            error: false,
            value: props.initialValue || "",
            disableOkBtn: props.isRequired && !props.initialValue,
        };
    }

    onChange = (event) => {
        let val = event.target.value;
        const valid = val || !this.props.isRequired;
        this.setState({ error: !valid, disableOkBtn: !valid, value: val });
    };

    render() {
        const {
            heading,
            prompt,
            multiline,
            isRequired,
            onOkBtnClick,
            onCancelBtnClick,
            dialogOpen,
        } = this.props;
        const { value, error, disableOkBtn } = this.state;
        return (
            <Dialog open={dialogOpen}>
                <DEDialogHeader
                    heading={heading}
                    onClose={() => {
                        if (onCancelBtnClick) {
                            onCancelBtnClick();
                        }
                    }}
                />
                <DialogContent>
                    <TextField
                        id="multiline-static"
                        helperText={prompt}
                        multiline={multiline}
                        rows={multiline ? 4 : 1}
                        margin="normal"
                        value={value}
                        style={{ width: 400 }}
                        required={isRequired}
                        onChange={(e) => this.onChange(e)}
                        error={error}
                    />
                </DialogContent>
                <DialogActions>
                    <Button
                        onClick={() => {
                            if (onCancelBtnClick) {
                                onCancelBtnClick();
                            }
                        }}
                        color="primary"
                    >
                        {getMessage("cancelBtnText")}
                    </Button>
                    <Button
                        variant="contained"
                        onClick={() => {
                            onOkBtnClick(value);
                        }}
                        disabled={disableOkBtn}
                        color="primary"
                    >
                        {getMessage("okBtnText")}
                    </Button>
                </DialogActions>
            </Dialog>
        );
    }
}

DEPromptDialog.propTypes = {
    dialogOpen: PropTypes.bool.isRequired,
    heading: PropTypes.string.isRequired,
    prompt: PropTypes.string.isRequired,
    multiline: PropTypes.bool,
    initialValue: PropTypes.string,
    isRequired: PropTypes.bool.isRequired,
    onOkBtnClick: PropTypes.func.isRequired,
    onCancelBtnClick: PropTypes.func,
};

export default withI18N(DEPromptDialog, intlData);
