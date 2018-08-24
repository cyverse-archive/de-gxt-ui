/**
 *  @author sriram
 *
 * Rewrite IPlantPromptDialog in react.
 **/

import React, { Component } from 'react';
import PropTypes from 'prop-types';
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
            value: props.initialValue ? props.initialValue : "",
            dialogOpen: props.dialogOpen,
            disableOkBtn: (props.isRequired && !props.initialValue) ? true : false,
        }
    }

    onChange = (event) => {
        let val = event.target.value;
        if ((!val || val.length === 0) && this.props.isRequired) {
            this.setState({error: true, disableOkBtn: true, value: val});
        } else {
            this.setState({error: false, disableOkBtn: false, value: val});
        }
    };

    render() {
        const {heading, prompt, multiline, isRequired, onOkBtnClick, onCancelBtnClick} = this.props;
        const {dialogOpen, value, error, disableOkBtn} = this.state;
        return (
            <Dialog open={dialogOpen}>
                <DEDialogHeader
                    heading={heading}
                    onClose={() => {
                        this.setState({dialogOpen: false})
                    }}/>
                <DialogContent>
                    <TextField
                        id="multiline-static"
                        label={prompt}
                        multiline={multiline ? multiline : false}
                        rows={multiline? 4 : 1}
                        margin="normal"
                        value={value}
                        style={{width: 400}}
                        required={isRequired}
                        onChange={(e) => this.onChange(e)}
                        error={error}
                    />
                </DialogContent>
                <DialogActions>
                    <Button
                        onClick={() => {
                            this.setState({dialogOpen: false});
                            onOkBtnClick(value);
                        }}
                        disabled={disableOkBtn}
                        color="primary">
                        {getMessage("okBtnText")}
                    </Button>
                    <Button
                        onClick={() => {
                            this.setState({dialogOpen: false});
                            if (onCancelBtnClick) {
                                onCancelBtnClick();
                            }
                        }}
                        color="primary">
                        {getMessage("cancelBtnText")}
                    </Button>
                </DialogActions>
            </Dialog>

        );
    }
}

DEPromptDialog.propTypes = {
    heading: PropTypes.string.isRequired,
    prompt: PropTypes.string.isRequired,
    multiline: PropTypes.bool,
    initialValue: PropTypes.string,
    isRequired: PropTypes.bool.isRequired,
    onOkBtnClick: PropTypes.func.isRequired,
    onCancelBtnClick: PropTypes.func,
    dialogOpen: PropTypes.bool.required,
};

export default withI18N(DEPromptDialog, intlData);
