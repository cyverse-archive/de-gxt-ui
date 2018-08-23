/**
 *  @author sriram
 *
 **/

import React, { Component } from 'react';
import PropTypes from 'prop-types';
import DialogTitle from "@material-ui/core/DialogTitle";
import Typography from "@material-ui/core/Typography";
import CloseIcon from "../../../node_modules/@material-ui/icons/Close";
import { withStyles } from "@material-ui/core/styles";
import exStyles from "./style";
import IconButton from "@material-ui/core/IconButton";

class DEDialogHeader extends Component {
    render() {
        const {classes, heading, onClose} = this.props;
        return (
            <DialogTitle className={classes.header}>
                <Typography
                    className={classes.title}>
                    {heading}
                </Typography>
                <IconButton
                    aria-label="More"
                    aria-haspopup="true"
                    onClick={onClose}
                    className={classes.dialogCloseButton}
                >
                    <CloseIcon/>
                </IconButton>
            </DialogTitle>
        );
    }
}

DEDialogHeader.propTypes = {
    heading: PropTypes.string.isRequired,
    onClose: PropTypes.func.isRequired,
};

export default withStyles(exStyles)(DEDialogHeader);
