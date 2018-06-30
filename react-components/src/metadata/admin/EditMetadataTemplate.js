/**
 * @author psarando
 */
import React, { Component } from "react";
import { Field, FieldArray, reduxForm } from "redux-form";

import { FormCheckbox, FormTextField } from "../../util/FormField";
import withStoreProvider from "../../util/StoreProvider";

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
        const { classes, open, handleSubmit, pristine, submitting, error, initialValues } = this.props;
        const { editingAttrIndex } = this.state;

        return (
            <Dialog open={open}
                    onClose={this.props.presenter.closeTemplateInfoDialog}
                    fullScreen
                    disableBackdropClick
                    disableEscapeKeyDown
                    aria-labelledby="form-dialog-title"
                    TransitionComponent={SlideUpTransition}
            >
                <AppBar className={classes.appBar}>
                    <Toolbar>
                        <IconButton color="inherit" onClick={this.props.presenter.closeTemplateInfoDialog} aria-label="Close">
                            <CloseIcon />
                        </IconButton>
                        <Typography id="form-dialog-title" variant="title" color="inherit" className={classes.flex}>
                            Edit Metadata Template
                        </Typography>
                        <Button id="metadata-template-save"
                                disabled={pristine || submitting || error}
                                onClick={handleSubmit(this.onSaveTemplate)}
                                color="inherit"
                        >
                            {this.props.saveText}
                        </Button>
                    </Toolbar>
                </AppBar>

                <DialogContent>
                    <Field name="name"
                           label="Name"
                           id="templateName"
                           autoFocus
                           margin="dense"
                           component={FormTextField}
                    />
                    <Field name="description"
                           label="Description"
                           id="templateDescription"
                           component={FormTextField}
                    />

                    <Field name="deleted"
                           label="Mark as Deleted?"
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
            attrErrors.name = "Required";
            attributesArrayErrors[attrIndex] = attrErrors;
        } else {
            const namesMatch = attr => (attr.name === name);
            if (attributes.slice(0, attrIndex).some(namesMatch) || attributes.slice(attrIndex + 1).some(namesMatch)) {
                attrErrors.name = "Attribute name must be unique";
                attributesArrayErrors[attrIndex] = attrErrors;
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
        errors.name = "Required";
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
    )(withStyles(styles)(EditMetadataTemplate)));
