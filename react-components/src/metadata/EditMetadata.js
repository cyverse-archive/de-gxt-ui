/**
 * @author psarando
 */
import React, { Component } from "react";
import { FieldArray, reduxForm } from "redux-form";
import { injectIntl } from "react-intl";

import withI18N, { getMessage, formatMessage } from "../util/I18NWrapper";
import withStoreProvider from "../util/StoreProvider";

import FormDialogEditAVU from "./EditAVU";
import MetadataList from "./MetadataList";
import SlideUpTransition from "./SlideUpTransition";
import intlData from "./messages";
import styles from "./style";

import AppBar from "@material-ui/core/AppBar";
import Button from "@material-ui/core/Button";
import Dialog from "@material-ui/core/Dialog";
import DialogContent from "@material-ui/core/DialogContent";
import IconButton from "@material-ui/core/IconButton";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";
import { withStyles } from "@material-ui/core/styles";

import CloseIcon from "@material-ui/icons/Close";

class EditMetadata extends Component {
    constructor(props) {
        super(props);

        this.state = {
            editingAttrIndex: -1,
        };

        this.onSaveMetadata = this.onSaveMetadata.bind(this);
    }

    onSaveMetadata ({ avus }) {
        let metadata = {...this.props.initialValues, avus: avus};
        this.props.presenter.onSaveMetadata(metadata);
    }

    render() {
        const {
            classes,
            intl,
            open,
            presenter: { closeEditMetadataDialog },
            // from redux-form
            handleSubmit, pristine, submitting, error, change,
        } = this.props;
        const { editingAttrIndex } = this.state;

        return (
            <Dialog open={open}
                    onClose={closeEditMetadataDialog}
                    fullScreen
                    disableBackdropClick
                    disableEscapeKeyDown
                    aria-labelledby="form-dialog-title"
                    TransitionComponent={SlideUpTransition}
            >
                <AppBar className={classes.appBar}>
                    <Toolbar>
                        <IconButton color="inherit" onClick={closeEditMetadataDialog} aria-label={formatMessage(intl, "close")}>
                            <CloseIcon />
                        </IconButton>
                        <Typography id="form-dialog-title" variant="title" color="inherit" className={classes.flex}>
                            {getMessage("dialogTitleEditMetadata")}
                        </Typography>
                        <Button id="metadata-template-save"
                                disabled={pristine || submitting || error}
                                onClick={handleSubmit(this.onSaveMetadata)}
                                color="inherit"
                        >
                            {getMessage("save")}
                        </Button>
                    </Toolbar>
                </AppBar>

                <DialogContent>
                    <FieldArray name="avus"
                                component={MetadataList}
                                field="avus"
                                change={change}
                                onEditAVU={(index) => this.setState({editingAttrIndex: index})}
                    />

                    <FieldArray name="avus"
                                component={FormDialogEditAVU}
                                change={change}
                                editingAttrIndex={editingAttrIndex}
                                closeAttrDialog={() => this.setState({editingAttrIndex: -1})}
                    />
                </DialogContent>
            </Dialog>
        );
    }
}

const validateAVUs = avus => {
    const avusArrayErrors = [];

    // Also track AVU errors in an avusArrayErrors["_error"] key,
    // so that dialogs can track when their AVUs have errors.
    const _error = [];

    avus.forEach((avu, index) => {
        const avuErrors = {};

        if (!avu.attr) {
            avuErrors.attr = getMessage("required");
            avusArrayErrors[index] = avuErrors;
        }

        if (avu.avus && avu.avus.length > 0) {
            const subAVUErros = validateAVUs(avu.avus);
            if (subAVUErros.length > 0) {
                avuErrors.avus = subAVUErros;
                avusArrayErrors[index] = avuErrors;
            }
        }

        if (avusArrayErrors[index]) {
            _error[index] = avuErrors;
            avusArrayErrors["_error"] = _error;
        }
    });

    return avusArrayErrors;
};

const validate = values => {
    const errors = {};

    if (values.avus && values.avus.length > 0) {
        const avusArrayErrors = validateAVUs(values.avus);
        if (avusArrayErrors.length > 0) {
            errors.avus = avusArrayErrors;
            errors._error = true;
        }
    }

    return errors;
};

export default withStoreProvider(
    reduxForm(
        {
            form: "EditMetadataForm",
            enableReinitialize: true,
            validate,
        }
    )(withStyles(styles)(withI18N(injectIntl(EditMetadata), intlData))));
