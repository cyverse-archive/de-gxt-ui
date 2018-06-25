/**
 * @author psarando
 */
import React, { Component } from "react";
import { Field, reduxForm } from "redux-form";

import withStoreProvider from "../../util/StoreProvider";

import styles from "../style";
import EditAttribute from "./EditAttribute";
import SlideUpTransition from "./SlideUpTransition";
import TemplateAttributeList from "./TemplateAttributeList";

import AppBar from "@material-ui/core/AppBar";
import Button from "@material-ui/core/Button";
import Checkbox from "@material-ui/core/Checkbox";
import Dialog from "@material-ui/core/Dialog";
import DialogContent from "@material-ui/core/DialogContent";
import Divider from "@material-ui/core/Divider";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import IconButton from "@material-ui/core/IconButton";
import TextField from "@material-ui/core/TextField";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";
import { withStyles } from "@material-ui/core/styles";

import CloseIcon from "@material-ui/icons/Close";

const FormTextField = ({
        input,
        label,
        meta: {touched, error},
        ...custom
    }) => (
    <TextField
        label={label}
        error={touched && !!error}
        helperText={error}
        fullWidth
        {...input}
        {...custom}
    />
);

const FormCheckbox = ({ input, label, ...custom }) => (
    <FormControlLabel
        control={
            <Checkbox checked={!!input.value}
                      onChange={input.onChange}
                      {...custom}
            />
        }
        label={label}
    />
);

class EditMetadataTemplate extends Component {
    constructor(props) {
        super(props);

        let attributes = [...props.initialValues.attributes];

        this.state = {
            attributes: attributes,
            editingAttrIndex: -1,
        };
    }

    onSaveTemplate = ({ name, description, deleted }) => {
        const { attributes } = this.state;

        this.props.presenter.onSaveTemplate({
            name,
            description,
            deleted,
            attributes,
        });
    };

    onAttributesChanged = (attributes) => {
        this.setState({attributes});
    };

    onAttributeUpdated = (index, attr) => {
        let attributes = [...this.state.attributes];
        attributes.splice(index, 1, attr);

        this.setState({attributes: attributes, editingAttrIndex: -1});
    };

    render() {
        const { classes, open, handleSubmit, pristine, submitting, error, initialValues } = this.props;
        const { attributes, editingAttrIndex } = this.state;
        const editingAttr = editingAttrIndex >= 0 ? attributes[editingAttrIndex] : {};

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

                    <TemplateAttributeList attributes={attributes}
                                           onAttributesChanged={this.onAttributesChanged}
                                           onAttributeUpdated={this.onAttributeUpdated}
                                           onEditAttr={(index) => this.setState({editingAttrIndex: index})}
                    />

                    <EditAttribute attribute={editingAttr}
                                   open={editingAttrIndex >= 0}
                                   parentName={initialValues.name}
                                   saveAttr={(attr) => this.onAttributeUpdated(editingAttrIndex, attr)}
                                   closeAttrDialog={() => this.setState({editingAttrIndex: -1})}
                    />
                </DialogContent>
            </Dialog>
        );
    }
}

const validate = values => {
    const errors = {};

    if (!values.name) {
        errors.name = "Required";
        errors._error = true;
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
