/**
 * @author psarando
 */
import React, { Component } from "react";

import { FastField, FieldArray, withFormik } from 'formik';
import PropTypes from "prop-types";
import { injectIntl } from "react-intl";

import build from "../../util/DebugIDUtil";
import withI18N, { formatMessage, getMessage } from "../../util/I18NWrapper";
import intlData from "../messages";
import styles from "../style";
import ids from "./ids";

import { FormCheckbox, FormTextField } from "../../util/FormField";
import DEConfirmationDialog from "../../util/dialog/DEConfirmationDialog";
import EditAttributeFormList from "./EditAttributeFormList";
import SlideUpTransition from "../SlideUpTransition";

import AppBar from "@material-ui/core/AppBar";
import Button from "@material-ui/core/Button";
import Dialog from "@material-ui/core/Dialog";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import Divider from "@material-ui/core/Divider";
import IconButton from "@material-ui/core/IconButton";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";
import { withStyles } from "@material-ui/core/styles";

import CloseIcon from "@material-ui/icons/Close";

const ConfirmationDialog = withI18N(DEConfirmationDialog, intlData);

class EditMetadataTemplate extends Component {
    constructor(props) {
        super(props);

        this.state = { showConfirmationDialog: false };

        [
            "closeMetadataTemplateDialog",
            "confirmCloseMetadataTemplateDialog",
            "closeConfirmationDialog",
        ].forEach(methodName => (this[methodName] = this[methodName].bind(this)));
    }

    static propTypes = {
        presenter: PropTypes.shape({
            onSaveTemplate: PropTypes.func.isRequired,
            closeMetadataTemplateDialog: PropTypes.func.isRequired,
        }),
    };

    closeMetadataTemplateDialog() {
        const { dirty, presenter } = this.props;

        dirty ?
            this.setState({showConfirmationDialog: true}) :
            presenter.closeMetadataTemplateDialog()
    }

    confirmCloseMetadataTemplateDialog() {
        this.closeConfirmationDialog();
        this.props.presenter.closeMetadataTemplateDialog();
    }

    closeConfirmationDialog() {
        this.setState({showConfirmationDialog: false});
    }

    render() {
        const {
            classes,
            intl,
            open,
            // from formik
            handleSubmit, dirty, isSubmitting, errors,
        } = this.props;

        const { showConfirmationDialog } = this.state;

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
                        <Typography id={dialogTitleID} variant="h6" color="inherit" className={classes.flex}>
                            {getMessage("dialogTitleEditMetadataTemplate")}
                        </Typography>
                        <IconButton id={build(ids.METADATA_TEMPLATE_FORM, ids.BUTTONS.CLOSE_ICON)}
                                    aria-label={formatMessage(intl, "close")}
                                    onClick={this.closeMetadataTemplateDialog}
                                    color="inherit"
                        >
                            <CloseIcon />
                        </IconButton>
                    </Toolbar>
                </AppBar>

                <DialogContent>
                    <FastField name="name"
                               label={getMessage("templateNameLabel")}
                               id={build(ids.METADATA_TEMPLATE_FORM, ids.TEMPLATE_NAME)}
                               required={true}
                               autoFocus
                               component={FormTextField}
                    />
                    <FastField name="description"
                               label={getMessage("description")}
                               id={build(ids.METADATA_TEMPLATE_FORM, ids.TEMPLATE_DESCRIPTION)}
                               component={FormTextField}
                    />

                    <FastField name="deleted"
                               label={getMessage("markAsDeleted")}
                               id={build(ids.METADATA_TEMPLATE_FORM, ids.CHECK_DELETED)}
                               color="primary"
                               component={FormCheckbox}
                    />

                    <Divider />

                    <FieldArray name="attributes"
                                component={EditAttributeFormList}
                    />
                </DialogContent>

                <DialogActions>
                    <Button id={build(ids.METADATA_TEMPLATE_FORM, ids.BUTTONS.CLOSE)}
                                onClick={this.closeMetadataTemplateDialog}
                                color="primary"
                    >
                        {getMessage("close")}
                    </Button>
                    <Button id={build(ids.METADATA_TEMPLATE_FORM, ids.BUTTONS.SAVE)}
                            disabled={!dirty || isSubmitting || errors.error}
                            onClick={handleSubmit}
                            color="primary"
                            variant="contained"
                    >
                        {getMessage("save")}
                    </Button>
                </DialogActions>

                <ConfirmationDialog dialogOpen={showConfirmationDialog}
                                    debugId={ids.METADATA_TEMPLATE_VIEW}
                                    heading={getMessage("confirmDiscardChangesDialogHeader")}
                                    message={getMessage("confirmDiscardChangesDialogMsg")}
                                    onOkBtnClick={this.confirmCloseMetadataTemplateDialog}
                                    onCancelBtnClick={this.closeConfirmationDialog}
                />
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
            attrErrors.error = getMessage("errAttrHasErrors");
            attributesArrayErrors[attrIndex] = attrErrors;
        } else {
            const namesMatch = attr => (attr.name === name);
            if (attributes.slice(0, attrIndex).some(namesMatch) || attributes.slice(attrIndex + 1).some(namesMatch)) {
                attrErrors.name = getMessage("errAttrNameMustBeUnique");
                attrErrors.error = attrErrors.error ? getMessage("errAttrHasErrors") : getMessage("errAttrNameMustBeUnique");
                attributesArrayErrors[attrIndex] = attrErrors;
            }
        }

        // Validate Enum values
        if (attr.type === "Enum") {
            if (!attr.values || attr.values.length < 1) {
                // Setting an attrErrors.values message allows the error to be displayed in the table header,
                // but that component will have to check first if attrErrors.values is an array or not.
                attrErrors.values = getMessage("errEnumValueRequired");
                attrErrors.error = attrErrors.error ? getMessage("errAttrHasErrors") : getMessage("errEnumValueRequired");
                attributesArrayErrors[attrIndex] = attrErrors;
            } else {
                const enumArrayErrors = [];
                attr.values.forEach((enumOption, valIndex) => {
                    if (!enumOption.value) {
                        enumArrayErrors[valIndex] = {
                            // Setting enumArrayErrors.error allows the dialog close button to be disabled.
                            error: true,
                            value: getMessage("required"),
                        };
                        attrErrors.error = attrErrors.error ? getMessage("errAttrHasErrors") : getMessage("errEnumOptionValueRequired");
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
                attrErrors.error = getMessage("errAttrHasErrors");
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
        errors.error = true;
    }

    if (values.attributes && values.attributes.length > 0) {
        const attributesArrayErrors = validateAttributes(values.attributes);
        if (attributesArrayErrors.length > 0) {
            errors.attributes = attributesArrayErrors;
            errors.error = true;
        }
    }

    return errors;
};

const handleSubmit = ({ name, description, deleted, attributes },
                      { props, setSubmitting, setStatus }) => {
    const resolve = (template) => {
        setSubmitting(false);
        setStatus({ success: true, template });
    };
    const errorCallback = (httpStatusCode, errorMessage) => {
        setSubmitting(false);
        setStatus({ success: false, errorMessage });
    };

    const { id } = props.template;

    props.presenter.onSaveTemplate(
        {
            id,
            name,
            description,
            deleted,
            attributes,
        },
        resolve,
        errorCallback,
    );
};

const mapPropsToValues = props => {
    const { template } = props;

    return { ...template };
};

export default withFormik(
    {
        enableReinitialize: true,
        mapPropsToValues,
        validate,
        handleSubmit,
    }
)(withStyles(styles)(withI18N(injectIntl(EditMetadataTemplate), intlData)));
