import React, { Component, Fragment } from "react";
import PropTypes from "prop-types";

import ids from "./ids";
import messages from "./messages";
import styles from "./styles";

import { build, withI18N, getMessage } from "@cyverse-de/ui-lib";

import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    TextField,
    withStyles,
} from "@material-ui/core";

/**
 * @author aramsey
 * A button which opens a dialog for naming an advanced search query and saving it
 */
class SaveSearchButton extends Component {
    constructor(props) {
        super(props);

        this.state = {
            open: false,
        };

        this.handleClose = this.handleClose.bind(this);
        this.handleOpen = this.handleOpen.bind(this);
        this.handleSave = this.handleSave.bind(this);
    }

    handleSave(values) {
        this.props.handleSave(values);
        this.setState({ open: false });
    }

    handleOpen() {
        this.setState({ open: true });
    }

    handleClose() {
        this.setState({ open: false });
    }

    render() {
        const {
            field: { value, onChange, ...field },
            form: { values },
            parentId,
            classes,
        } = this.props;

        return (
            <Fragment>
                <Button
                    variant="contained"
                    className={classes.searchButton}
                    id={build(parentId, ids.saveSearchBtn)}
                    onClick={this.handleOpen}
                >
                    {getMessage("saveBtn")}
                </Button>
                <Dialog open={this.state.open} onClose={this.handleClose}>
                    <DialogTitle id={ids.saveSearchDlg}>
                        {getMessage("saveSearchTitle")}
                    </DialogTitle>
                    <DialogContent>
                        <TextField
                            id={ids.saveTextField}
                            label={
                                value
                                    ? getMessage("filterName")
                                    : getMessage("requiredField")
                            }
                            value={value}
                            onChange={onChange}
                            {...field}
                        />
                    </DialogContent>
                    <DialogActions>
                        <Button
                            id={ids.cancelBtn}
                            color="primary"
                            onClick={this.handleClose}
                        >
                            {getMessage("cancelBtn")}
                        </Button>
                        <Button
                            variant="contained"
                            disabled={!value}
                            id={ids.saveBtn}
                            color="primary"
                            onClick={() => this.handleSave(values)}
                        >
                            {getMessage("saveBtn")}
                        </Button>
                    </DialogActions>
                </Dialog>
            </Fragment>
        );
    }
}

SaveSearchButton.propTypes = {
    parentId: PropTypes.string,
    handleSave: PropTypes.func.isRequired,
    field: PropTypes.object.isRequired,
};

export default withStyles(styles)(withI18N(SaveSearchButton, messages));
