/**
 * @author psarando
 */
import React, { Component, Fragment } from 'react';

import { Field, FieldArray, withFormik } from 'formik';
import { injectIntl } from "react-intl";

import withI18N, { formatMessage, getMessage } from "../util/I18NWrapper";
import build from "../util/DebugIDUtil";
import ids from "./ids";
import intlData from "./messages";
import styles from "./style";

import ConfirmCloseDialog from "../util/ConfirmCloseDialog";
import { FormikCheckbox, FormikIntegerField, FormikNumberField, FormikTextField } from "../util/FormField";
import SlideUpTransition from "./SlideUpTransition";

import { withStyles } from '@material-ui/core/styles';

import AppBar from "@material-ui/core/AppBar/AppBar";
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogContent from '@material-ui/core/DialogContent';
import Grid from '@material-ui/core/Grid';
import IconButton from "@material-ui/core/IconButton/IconButton";
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';

import ContentAdd from '@material-ui/icons/Add';
import CloseIcon from "@material-ui/icons/Close";
import ContentRemove from '@material-ui/icons/Delete';

class MetadataTemplateAttributeView extends Component {
    onAddAVU(arrayHelpers, attribute) {
        const avu = {
            attr: attribute.name,
            value: "",
            unit: "",
        };

        this.addSubAVUs(attribute, avu);

        arrayHelpers.push(avu);
    }

    addSubAVUs(attribute, avu) {
        const requiredAttrs = attribute.attributes && attribute.attributes.filter(subAttr => subAttr.required);

        if (requiredAttrs && requiredAttrs.length > 0) {
            avu.avus = requiredAttrs.map(subAttr => {
                const subAVU = {
                    attr: subAttr.name,
                    value: "",
                    unit: "",
                };

                this.addSubAVUs(subAttr, subAVU);

                return subAVU;
            });
        }
    }

    render() {
        const { classes, intl, field, attributes, avus, errors } = this.props;
        return (
            <FieldArray name={`${field}.avus`}
                        render={(arrayHelpers) => {
                            return attributes.map((attribute) => {
                                const attrFieldId = build(ids.METADATA_TEMPLATE_VIEW, field, attribute.name);
                                let canRemove = !attribute.required;

                                let FieldComponent;
                                switch (attribute.type) {
                                    case "Boolean":
                                        FieldComponent = FormikCheckbox;
                                        break;
                                    case "Number":
                                        FieldComponent = FormikNumberField;
                                        break;
                                    case "Integer":
                                        FieldComponent = FormikIntegerField;
                                        break;
                                    default:
                                        FieldComponent = FormikTextField;
                                        break;
                                }

                                let avuFields = avus && avus.map((avu, index) => {
                                    if (avu.attr !== attribute.name) {
                                        return null;
                                    }

                                    let avuFieldName = `${field}.avus[${index}]`;
                                    const rowID = build(ids.METADATA_TEMPLATE_VIEW, avuFieldName);

                                    let avuField = (
                                        <Fragment key={avuFieldName}>
                                            <Grid container
                                                  spacing={16}
                                                  justify="flex-start"
                                                  alignItems="center"
                                            >
                                                <Grid item xs>
                                                    <Field name={`${avuFieldName}.value`}
                                                           component={FieldComponent}
                                                           label={attribute.name}
                                                           required={attribute.required}
                                                    />
                                                </Grid>
                                                {canRemove &&
                                                <Grid item xs={1}>
                                                    <IconButton id={build(rowID, ids.BUTTONS.DELETE)}
                                                                aria-label={formatMessage(intl, "delete")}
                                                                classes={{root: classes.deleteIcon}}
                                                                onClick={() => arrayHelpers.remove(index)}
                                                    >
                                                        <ContentRemove />
                                                    </IconButton>
                                                </Grid>
                                                }
                                            </Grid>
                                            {attribute.attributes && attribute.attributes.length > 0 &&
                                            <MetadataTemplateAttributeForm field={avuFieldName}
                                                                           errors={errors && errors.avus}
                                                                           attributes={attribute.attributes}
                                                                           avus={avu.avus}
                                            />}
                                        </Fragment>
                                    );

                                    canRemove = true;

                                    return avuField;
                                });

                                return (
                                    <fieldset key={attribute.name}>
                                        <legend>{attribute.name}</legend>
                                        <Button id={build(attrFieldId, ids.BUTTONS.ADD)}
                                                variant="fab"
                                                mini
                                                color="primary"
                                                aria-label={formatMessage(intl, "addRow")}
                                                onClick={() => this.onAddAVU(arrayHelpers, attribute)}
                                        >
                                            <ContentAdd/>
                                        </Button>
                                        {avuFields}
                                    </fieldset>
                                );
                            });
                        }}
            />
        );
    }
}

const MetadataTemplateAttributeForm = withStyles(styles)(withI18N(injectIntl(MetadataTemplateAttributeView), intlData));

class MetadataTemplateView extends Component {
    constructor(props) {
        super(props);

        this.state = {
            showConfirmationDialog: false,
        };

        this.closeTemplateInfoDialog = this.closeTemplateInfoDialog.bind(this);
        this.closeConfirmationDialog = this.closeConfirmationDialog.bind(this);
    }

    closeTemplateInfoDialog() {
        this.closeConfirmationDialog();
        this.props.presenter.closeTemplateInfoDialog();
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
            values, handleSubmit, dirty, isSubmitting, errors,
        } = this.props;

        const dialogTitleID = build(ids.METADATA_TEMPLATE_VIEW, ids.TITLE);

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
                        <IconButton id={build(ids.METADATA_TEMPLATE_VIEW, ids.BUTTONS.CLOSE)}
                                    aria-label={formatMessage(intl, "close")}
                                    onClick={() => (
                                        dirty ?
                                            this.setState({showConfirmationDialog: true}) :
                                            this.props.presenter.closeTemplateInfoDialog()
                                    )}
                                    color="inherit"
                        >
                            <CloseIcon />
                        </IconButton>
                        <Typography id={dialogTitleID} variant="title" color="inherit" className={classes.flex}>
                            {getMessage("dialogTitleEditMetadataTemplate")}
                        </Typography>
                        <Button id={build(ids.METADATA_TEMPLATE_VIEW, ids.BUTTONS.SAVE)}
                                disabled={isSubmitting || errors.error}
                                onClick={handleSubmit}
                                color="inherit"
                        >
                            {getMessage("save")}
                        </Button>
                    </Toolbar>
                </AppBar>

                <DialogContent>
                    <MetadataTemplateAttributeForm field="metadata"
                                                   errors={errors && errors.metadata && errors.metadata.avus}
                                                   attributes={values.template.attributes}
                                                   avus={values.metadata.avus}
                    />
                </DialogContent>

                <ConfirmCloseDialog open={this.state.showConfirmationDialog}
                                    parentId={ids.METADATA_TEMPLATE_VIEW}
                                    onConfirm={() => {
                                        this.closeConfirmationDialog();
                                        handleSubmit();
                                    }}
                                    onClose={this.closeTemplateInfoDialog}
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

const mapPropsToValues = props => {
    const { template } = props;
    const metadata = { ...props.metadata };

    const attributeMap = {};
    const mapAttributesToAttrMap = (attrMap, attributes) => attributes.forEach(attribute => {
        const attrCopy = { ...attribute };
        attrMap[attribute.name] = attrCopy;

        if (attrCopy.attributes && attrCopy.attributes.length > 0) {
            const subAttrMap = {};
            mapAttributesToAttrMap(subAttrMap, attrCopy.attributes);
            attrCopy.attributes = subAttrMap;
        }
    });

    mapAttributesToAttrMap(attributeMap, template.attributes);

    const mapAttributesToAVUs = (attributes, propsAVUs) => {

        let avus = [...propsAVUs];

        attributes.filter((attribute) => attribute.required).forEach((attribute) => {
            if (avus.filter((avu) => avu.attr === attribute.name).length < 1) {
                avus.push({ attr: attribute.name, value: "", unit: "" });
            }

            const { attributes } = attribute;
            if (attributes && attributes.length > 0) {
                avus = avus.map(propsAVU => {
                    let avu = { ...propsAVU };

                    if (avu.attr === attribute.name) {
                        avu.avus = mapAttributesToAVUs(attributes, (avu.avus || []));
                    }

                    return avu;
                });
            }
        });

        return avus;
    };

    metadata.avus = mapAttributesToAVUs(template.attributes, metadata.avus);

    return { template, attributeMap, metadata };
};

const validateAVUs = (avus, attributeMap) => {
    const avuArrayErrors = [];

    avus.forEach((avu, avuIndex) => {
        const avuErrors = {};
        const value = avu.value;

        let attrTemplate = attributeMap[avu.attr];
        if (!attrTemplate) {
            return;
        }

        if (attrTemplate.required && value === "") {
            avuErrors.value = getMessage("required");
            avuArrayErrors[avuIndex] = avuErrors;
        } else if (value) {
            switch (attrTemplate.type) {
                case "Number":
                case "Integer":
                    let numVal = Number(value);
                    if (isNaN(numVal)) {
                        avuErrors.value = getMessage("templateValidationErrMsgNumber");
                        avuArrayErrors[avuIndex] = avuErrors;
                    }

                    break;
                case "Boolean":
                    break;
                default:
                    break;
            }
        }

        if (attrTemplate.attributes) {
            const subAttrErros = validateAVUs(avu.avus, attrTemplate.attributes);
            if (subAttrErros.length > 0) {
                avuErrors.avus = subAttrErros;
                avuArrayErrors[avuIndex] = avuErrors;
            }
        }
    });

    return avuArrayErrors;
};

const validate = values => {
    const errors = {};
    const { attributeMap, metadata } = values;

    if (metadata.avus && metadata.avus.length > 0) {
        const avuArrayErrors = validateAVUs(metadata.avus, attributeMap);
        if (avuArrayErrors.length > 0) {
            errors.metadata = { avus: avuArrayErrors };
            errors.error = true;
        }
    }

    return errors;
};

const handleSubmit = ({ metadata }, { props, setSubmitting, setStatus }) => {
    const resolve = (metadata) => {
        setSubmitting(false);
        setStatus("submitted");
    };
    const errorCallback = (httpStatusCode, errorMessage) => {
        setSubmitting(false);
        setStatus("error");
    };

    props.presenter.onSaveMetadata(
        metadata,
        resolve,
        errorCallback,
    );
};

export default withFormik(
    {
        enableReinitialize: true,
        mapPropsToValues,
        validate,
        handleSubmit,
    }
)(withStyles(styles)(withI18N(injectIntl(MetadataTemplateView), intlData)));
