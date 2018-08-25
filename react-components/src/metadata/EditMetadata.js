/**
 * @author psarando
 */
import React, { Component } from "react";

import PropTypes from "prop-types";
import { FieldArray, reduxForm } from "redux-form";
import { injectIntl } from "react-intl";

import build from "../util/DebugIDUtil";
import withI18N, { formatMessage, getMessage } from "../util/I18NWrapper";
import withStoreProvider from "../util/StoreProvider";
import ids from "./ids";
import intlData from "./messages";
import styles from "./style";

import FormDialogEditAVU from "./EditAVU";
import MetadataList from "./MetadataList";
import SlideUpTransition from "./SlideUpTransition";

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

        [
            "onSaveMetadata",
        ].forEach(methodName => (this[methodName] = this[methodName].bind(this)));
    }

    static propTypes = {
        presenter: PropTypes.shape({
            onSaveMetadata: PropTypes.func.isRequired,
            closeEditMetadataDialog: PropTypes.func.isRequired,
        }).isRequired,
    };

    onSaveMetadata ({ avus }) {
        let metadata = {...this.props.initialValues, avus: avus};
        this.props.presenter.onSaveMetadata(metadata);
    }

    render() {
        const {
            classes,
            intl,
            open,
            editable,
            presenter: { closeEditMetadataDialog },
            // from redux-form
            handleSubmit, pristine, submitting, error, change,
        } = this.props;

        const { editingAttrIndex } = this.state;

        const dialogTitleID = build(ids.EDIT_METADATA_FORM, ids.TITLE);

        return (
            <Dialog open={open}
                    onClose={closeEditMetadataDialog}
                    fullWidth={true}
                    maxWidth="md"
                    disableBackdropClick
                    disableEscapeKeyDown
                    aria-labelledby={dialogTitleID}
                    TransitionComponent={SlideUpTransition}
            >
                <AppBar className={classes.appBar}>
                    <Toolbar>
                        <IconButton id={build(ids.EDIT_METADATA_FORM, ids.BUTTONS.CLOSE)}
                                    onClick={closeEditMetadataDialog}
                                    aria-label={formatMessage(intl, "close")}
                                    color="inherit"
                        >
                            <CloseIcon />
                        </IconButton>
                        <Typography id={dialogTitleID} variant="title" color="inherit" className={classes.flex}>
                            {editable ?
                                getMessage("dialogTitleEditMetadata") :
                                getMessage("dialogTitleViewMetadata")}
                        </Typography>
                        {editable &&
                        <Button id={build(ids.EDIT_METADATA_FORM, ids.BUTTONS.SAVE)}
                                disabled={pristine || submitting || error}
                                onClick={handleSubmit(this.onSaveMetadata)}
                                color="inherit"
                        >
                            {getMessage("save")}
                        </Button>}
                    </Toolbar>
                </AppBar>

                <DialogContent>
                    <FieldArray name="avus"
                                component={MetadataList}
                                field="avus"
                                change={change}
                                editable={editable}
                                parentID={ids.EDIT_METADATA_FORM}
                                onEditAVU={(index) => this.setState({editingAttrIndex: index})}
                    />

                    <FieldArray name="avus"
                                component={FormDialogEditAVU}
                                change={change}
                                editable={editable}
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
            form: ids.EDIT_METADATA_FORM,
            enableReinitialize: true,
            validate,
        }
    )(withStyles(styles)(withI18N(injectIntl(EditMetadata), intlData))));
