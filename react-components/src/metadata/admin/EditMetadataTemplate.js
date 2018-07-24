/**
 * @author psarando
 */
import React, { Component } from "react";
import { Field, FieldArray, reduxForm } from "redux-form";
import { injectIntl } from "react-intl";

import build from "../../util/DebugIDUtil";
import withI18N, { getMessage, formatMessage } from "../../util/I18NWrapper";
import withStoreProvider from "../../util/StoreProvider";
import intlData from "../messages";
import styles from "../style";
import ids from "./ids";

import ConfirmCloseDialog from "../../util/ConfirmCloseDialog";
import { FormCheckbox, FormTextField } from "../../util/FormField";
import EditAttributeFormList from "./EditAttributeFormList";
import SlideUpTransition from "./SlideUpTransition";

import AppBar from "@material-ui/core/AppBar";
import Button from "@material-ui/core/Button";
import Dialog from "@material-ui/core/Dialog";
import DialogContent from "@material-ui/core/DialogContent";
import Divider from "@material-ui/core/Divider";
import IconButton from "@material-ui/core/IconButton";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";
import { withStyles } from "@material-ui/core/styles";

import CloseIcon from "@material-ui/icons/Close";

class EditMetadataTemplate extends Component {
    constructor(props) {
        super(props);

        this.state = { showConfirmationDialog: false };
    }

    onSaveTemplate = ({ name, description, deleted, attributes }) => {
        const { id } = this.props.initialValues;

        this.props.presenter.onSaveTemplate({
            id,
            name,
            description,
            deleted,
            attributes,
        });

        this.closeConfirmationDialog();
    };

    closeConfirmationDialog = () => this.setState({ showConfirmationDialog: false });

    render() {
        const {
            classes,
            intl,
            open,
            // from redux-form
            handleSubmit, pristine, submitting, error, change,
        } = this.props;

        const dialogTitleID = build(ids.METADATA_TEMPLATE_FORM, ids.TITLE);

        return (
            <Dialog open={open}
                    fullWidth={true}
                    maxWidth="md"
                    disableBackdropClick
                    disableEscapeKeyDown
                    aria-labelledby={dialogTitleID}
                    TransitionComponent={SlideUpTransition}
            >
                <AppBar className={classes.appBar}>
                    <Toolbar>
                        <IconButton id={build(ids.METADATA_TEMPLATE_FORM, ids.BUTTONS.CLOSE)}
                                    aria-label={formatMessage(intl, "close")}
                                    onClick={() => (
                                        pristine ?
                                            this.props.presenter.closeTemplateInfoDialog() :
                                            this.setState({showConfirmationDialog: true})
                                    )}
                                    color="inherit"
                        >
                            <CloseIcon />
                        </IconButton>
                        <Typography id={dialogTitleID} variant="title" color="inherit" className={classes.flex}>
                            {getMessage("dialogTitleEditMetadataTemplate")}
                        </Typography>
                        <Button id={build(ids.METADATA_TEMPLATE_FORM, ids.BUTTONS.SAVE)}
                                disabled={pristine || submitting || error}
                                onClick={handleSubmit(this.onSaveTemplate)}
                                color="inherit"
                        >
                            {getMessage("save")}
                        </Button>
                    </Toolbar>
                </AppBar>

                <DialogContent>
                    <Field name="name"
                           label={getMessage("templateNameLabel")}
                           id={build(ids.METADATA_TEMPLATE_FORM, ids.TEMPLATE_NAME)}
                           required={true}
                           autoFocus
                           margin="dense"
                           component={FormTextField}
                    />
                    <Field name="description"
                           label={getMessage("description")}
                           id={build(ids.METADATA_TEMPLATE_FORM, ids.TEMPLATE_DESCRIPTION)}
                           component={FormTextField}
                    />

                    <Field name="deleted"
                           label={getMessage("markAsDeleted")}
                           id={build(ids.METADATA_TEMPLATE_FORM, ids.CHECK_DELETED)}
                           color="primary"
                           component={FormCheckbox}
                    />

                    <Divider />

                    <FieldArray name="attributes"
                                component={EditAttributeFormList}
                                change={change}
                    />
                </DialogContent>

                <ConfirmCloseDialog open={this.state.showConfirmationDialog}
                                    parentId={ids.METADATA_TEMPLATE_FORM}
                                    onConfirm={handleSubmit(this.onSaveTemplate)}
                                    onClose={() => {
                                        this.closeConfirmationDialog();
                                        this.props.presenter.closeTemplateInfoDialog();
                                    }}
                                    onCancel={this.closeConfirmationDialog}
                                    title={getMessage("save")}
                                    dialogContent={getMessage("confirmCloseUnsavedChanges")}
                                    confirmLabel={getMessage("yes")}
                                    closeLabel={getMessage("no")}
                                    cancelLabel={getMessage("cancel")}
                />
            </Dialog>
        );
    }
}

const validateAttributes = attributes => {
    const attributesArrayErrors = [];
    const _error = [];

    attributes.forEach((attr, attrIndex) => {
        const attrErrors = {};
        const name = attr.name;

        if (!name) {
            attrErrors.name = getMessage("required");
            attributesArrayErrors[attrIndex] = attrErrors;
        } else {
            const namesMatch = attr => (attr.name === name);
            if (attributes.slice(0, attrIndex).some(namesMatch) || attributes.slice(attrIndex + 1).some(namesMatch)) {
                attrErrors.name = getMessage("errAttrNameMustBeUnique");
                attributesArrayErrors[attrIndex] = attrErrors;
            }
        }

        // Validate Enum values
        if (attr.type === "Enum") {
            if (!attr.values || attr.values.length < 1) {
                // Setting an attrErrors.values["_error"] message allows the error to be displayed in the table header.
                attrErrors.values = [];
                attrErrors.values["_error"] = getMessage("errEnumValueRequired");
                attributesArrayErrors[attrIndex] = attrErrors;
            } else {
                const enumArrayErrors = [];
                attr.values.forEach((enumOption, valIndex) => {
                    if (!enumOption.value) {
                        enumArrayErrors[valIndex] = {value: getMessage("required")};
                        // Setting an enumArrayErrors["_error"] message allows the error to be displayed in the table header,
                        // and also allows the dialog close button to be disabled.
                        enumArrayErrors["_error"] = getMessage("errEnumOptionValueRequired");
                        attrErrors.values = enumArrayErrors;
                        attributesArrayErrors[attrIndex] = attrErrors;
                    }
                });
            }
        }

        if (attr.attributes && attr.attributes.length > 0) {
            const subAttrErros = validateAttributes(attr.attributes);
            if (subAttrErros.length > 0) {
                attrErrors.attributes = subAttrErros;
                attributesArrayErrors[attrIndex] = attrErrors;
            }
        }

        if (attributesArrayErrors[attrIndex]) {
            _error[attrIndex] = getMessage("errAttrHasErrors");
            attributesArrayErrors["_error"] = _error;
        }
    });

    return attributesArrayErrors;
};

const validate = values => {
    const errors = {};

    if (!values.name) {
        errors.name = getMessage("required");
        errors._error = true;
    }

    if (values.attributes && values.attributes.length > 0) {
        const attributesArrayErrors = validateAttributes(values.attributes);
        if (attributesArrayErrors.length > 0) {
            errors.attributes = attributesArrayErrors;
            errors._error = true;
        }
    }

    return errors;
};

export default withStoreProvider(
    reduxForm(
        {
            form: ids.METADATA_TEMPLATE_FORM,
            enableReinitialize: true,
            validate,
        }
    )(withStyles(styles)(withI18N(injectIntl(EditMetadataTemplate), intlData))));
