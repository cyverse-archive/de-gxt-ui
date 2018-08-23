/**
 *
 * @author Sriram
 *
 */
import React, { Component } from 'react';
import Dialog from "@material-ui/core/Dialog";
import withI18N, { getMessage } from "../../../util/I18NWrapper";
import DialogContent from "@material-ui/core/DialogContent";
import DialogActions from "@material-ui/core/DialogActions";
import TextField from "@material-ui/core/TextField/TextField";
import Button from "@material-ui/core/Button/Button";
import intlData from "../../messages";
import exStyles from "../../style";
import { withStyles } from "@material-ui/core/styles";
import DEDialogHeader from "../../../util/dialog/DEDialogHeader";

class AnalysisCommentsDialog extends Component {

    constructor(props) {
        super(props);
        this.state = {
            dialogOpen: props.dialogOpen,
        };
    }


    render() {
        return (
            <Dialog open={this.state.dialogOpen}>
                <DEDialogHeader
                    heading={getMessage("comments")}
                    onClose={() => {
                        this.setState({dialogOpen: false})
                    }}/>
                <DialogContent>
                    <TextField
                        id="multiline-static"
                        label={getMessage("comments")}
                        multiline
                        margin="normal"
                        value={this.props.comments}
                        style={{width: 400}}
                    />
                </DialogContent>
                <DialogActions>
                    <Button
                        onClick={() => {
                            this.setState({dialogOpen: false})
                        }}
                        color="primary">
                        {getMessage("okBtnText")}
                    </Button>
                    <Button
                        onClick={() => {
                            this.setState({dialogOpen: false})
                        }}
                        color="primary">
                        {getMessage("cancelBtnText")}
                    </Button>
                </DialogActions>
            </Dialog>

        );
    }
}

export default withStyles(exStyles)(withI18N(AnalysisCommentsDialog, intlData));