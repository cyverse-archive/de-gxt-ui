/**
 * @author psarando
 */
import React, { Component } from "react";
import { Field, FieldArray, reduxForm } from "redux-form";
import { injectIntl } from "react-intl";

import withI18N, { getMessage, formatMessage } from "../../util/I18NWrapper";
import { FormCheckbox, FormTextField } from "../../util/FormField";
import withStoreProvider from "../../util/StoreProvider";

import intlData from "../messages";
import styles from "../style";
import FormDialogEditAttribute from "./EditAttribute";
import SlideUpTransition from "./SlideUpTransition";
import TemplateAttributeList from "./TemplateAttributeList";

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

        this.state = {
            editingAttrIndex: -1,
        };
    }

    onSaveTemplate = ({ name, description, deleted, attributes }) => {
        this.props.presenter.onSaveTemplate({
            ...this.props.initialValues,
            name,
            description,
            deleted,
            attributes,
        });
    };

    render() {
        const {
            classes,
            intl,
            open,
            presenter: { closeTemplateInfoDialog },
            // from redux-form
            handleSubmit, pristine, submitting, error, change, initialValues,
        } = this.props;
        const { editingAttrIndex } = this.state;

        return (
            <Dialog open={open}
                    onClose={closeTemplateInfoDialog}
                    fullScreen
                    disableBackdropClick
                    disableEscapeKeyDown
                    aria-labelledby="form-dialog-title"
                    TransitionComponent={SlideUpTransition}
            >
                <AppBar className={classes.appBar}>
                    <Toolbar>
                        <IconButton color="inherit" onClick={closeTemplateInfoDialog} aria-label={formatMessage(intl, "close")}>
                            <CloseIcon />
                        </IconButton>
                        <Typography id="form-dialog-title" variant="title" color="inherit" className={classes.flex}>
                            {getMessage("dialogTitleEditMetadataTemplate")}
                        </Typography>
                        <Button id="metadata-template-save"
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
                           id="templateName"
                           required={true}
                           autoFocus
                           margin="dense"
                           component={FormTextField}
                    />
                    <Field name="description"
                           label={getMessage("description")}
                           id="templateDescription"
                           component={FormTextField}
                    />

                    <Field name="deleted"
                           label={getMessage("markAsDeleted")}
                           id="templateDeleted"
                           color="primary"
                           component={FormCheckbox}
                    />

                    <Divider />

                    <FieldArray name="attributes"
                                component={TemplateAttributeList}
                                onEditAttr={(index) => this.setState({editingAttrIndex: index})}
                    />

                    <FieldArray name="attributes"
                                component={FormDialogEditAttribute}
                                change={change}
                                editingAttrIndex={editingAttrIndex}
                                parentName={initialValues.name}
                                closeAttrDialog={() => this.setState({editingAttrIndex: -1})}
                    />
                </DialogContent>
            </Dialog>
        );
    }
}

const validateAttributes = attributes => {
    const attributesArrayErrors = [];

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
            form: 'adminMetadataTemplateForm',
            enableReinitialize: true,
            validate,
        }
    )(withStyles(styles)(withI18N(injectIntl(EditMetadataTemplate), intlData))));
