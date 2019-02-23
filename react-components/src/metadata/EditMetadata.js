/**
 * @author psarando
 */
import React, { Component, Fragment } from "react";

import { FieldArray, withFormik } from 'formik';
import PropTypes from "prop-types";
import { injectIntl } from "react-intl";

import build from "../util/DebugIDUtil";
import withI18N, { formatHTMLMessage, formatMessage, getMessage } from "../util/I18NWrapper";
import ids from "./ids";
import intlData from "./messages";
import styles from "./style";

import DEConfirmationDialog from "../util/dialog/DEConfirmationDialog";
import FormDialogEditAVU from "./EditAVU";
import MetadataList from "./MetadataList";
import SlideUpTransition from "./SlideUpTransition";

import AppBar from "@material-ui/core/AppBar";
import Button from "@material-ui/core/Button";
import CircularProgress from "@material-ui/core/CircularProgress";
import Dialog from "@material-ui/core/Dialog";
import DialogContent from "@material-ui/core/DialogContent";
import IconButton from "@material-ui/core/IconButton";
import Tab from "@material-ui/core/Tab";
import Tabs from "@material-ui/core/Tabs";
import Toolbar from "@material-ui/core/Toolbar";
import Tooltip from "@material-ui/core/Tooltip";
import Typography from "@material-ui/core/Typography";
import { withStyles } from "@material-ui/core/styles";

import CloseIcon from "@material-ui/icons/Close";
import ContentView from "@material-ui/icons/List";
import SaveIcon from "@material-ui/icons/Save";
import SaveAltIcon from "@material-ui/icons/SaveAlt";

const ConfirmationDialog = withI18N(DEConfirmationDialog, intlData);

class EditMetadata extends Component {
    constructor(props) {
        super(props);

        this.state = {
            showDiscardChangesDialog: false,
            showImportConfirmationDialog: false,
            editingAttrIndex: -1,
            tabIndex: 0,
            irodsAVUsSelected: [],
        };

        [
            "closeEditMetadataDialog",
            "closeDiscardChangesDialog",
            "handleTabChange",
            "handleSelectAVU",
            "handleSelectAllAVUs",
            "onConfirmImportIRODSMetadata",
        ].forEach(methodName => (this[methodName] = this[methodName].bind(this)));
    }

    static propTypes = {
        targetName: PropTypes.string,
        presenter: PropTypes.shape({
            onSaveMetadata: PropTypes.func.isRequired,
            closeEditMetadataDialog: PropTypes.func.isRequired,
            onSelectTemplateBtnSelected: PropTypes.func.isRequired,
            onSaveMetadataToFileBtnSelected: PropTypes.func.isRequired,
        }).isRequired,
    };

    closeEditMetadataDialog() {
        this.closeDiscardChangesDialog();
        this.props.presenter.closeEditMetadataDialog();
    }

    closeDiscardChangesDialog() {
        this.setState({showDiscardChangesDialog: false});
    }

    handleTabChange(event, tabIndex) {
        this.setState({ tabIndex });
    }

    handleSelectAVU(avu, selected) {
        const { irodsAVUsSelected } = this.state;

        const newSelection = selected ?
            [...irodsAVUsSelected, avu] :
            irodsAVUsSelected.filter(selectedAVU => selectedAVU !== avu);

        this.setState({ irodsAVUsSelected: newSelection });
    }

    handleSelectAllAVUs() {
        const { values: { "irods-avus": irodsAVUs } } = this.props;
        const { irodsAVUsSelected } = this.state;

        if (irodsAVUsSelected.length === 0) {
            this.setState({ irodsAVUsSelected: irodsAVUs });
        } else {
            this.setState({ irodsAVUsSelected: [] });
        }
    }

    onConfirmImportIRODSMetadata() {
        const { irodsAVUsSelected } = this.state;
        const {
            setFieldValue,
            values: {
                avus,
                "irods-avus": irodsAVUs,
            },
        } = this.props;

        const irodsAVUsRemaining = new Set(irodsAVUs);

        irodsAVUsSelected.forEach(selectedAVU => irodsAVUsRemaining.delete(selectedAVU));

        setFieldValue("avus", [...irodsAVUsSelected, ...avus]);
        setFieldValue("irods-avus", [...irodsAVUsRemaining]);

        this.setState({
            showImportConfirmationDialog: false,
            irodsAVUsSelected: [],
        });
    }

    render() {
        const {
            classes,
            intl,
            open,
            editable,
            loading,
            targetName,
            // from formik
            handleSubmit, dirty, isSubmitting, errors, values,
        } = this.props;

        const {
            showDiscardChangesDialog,
            showImportConfirmationDialog,
            editingAttrIndex,
            tabIndex,
            irodsAVUsSelected,
        } = this.state;

        const dialogTitleID = build(ids.EDIT_METADATA_FORM, ids.TITLE);

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
                        <IconButton id={build(ids.EDIT_METADATA_FORM, ids.BUTTONS.CLOSE)}
                                    onClick={() =>
                                        dirty ?
                                            this.setState({showDiscardChangesDialog: true}) :
                                            this.props.presenter.closeEditMetadataDialog()
                                    }
                                    aria-label={formatMessage(intl, "close")}
                                    color="inherit"
                        >
                            <CloseIcon />
                        </IconButton>
                        <Typography id={dialogTitleID} variant="h6" color="inherit" className={classes.flex}>
                            {getMessage(
                                editable ? "dialogTitleEditMetadataFor" : "dialogTitleViewMetadataFor",
                                { values: { targetName } }
                            )}
                        </Typography>

                        {tabIndex === 0 &&
                        <Tooltip title={getMessage("viewInTemplate")}
                                 placement="bottom"
                                 enterDelay={200}
                        >
                            <span>
                                <IconButton id={build(ids.EDIT_METADATA_FORM, ids.BUTTONS.VIEW_TEMPLATES)}
                                            aria-label={formatMessage(intl, "viewInTemplate")}
                                            disabled={loading || isSubmitting || errors.error}
                                            onClick={() => this.props.presenter.onSelectTemplateBtnSelected(values)}
                                            color="inherit"
                                >
                                    <ContentView/>
                                </IconButton>
                            </span>
                        </Tooltip>}

                        {tabIndex === 1 &&
                        <Tooltip title={getMessage("importIRODSMetadataTooltip")}
                                 placement="bottom"
                                 enterDelay={200}
                        >
                            <span>
                                <IconButton id={build(ids.EDIT_METADATA_FORM, ids.BUTTONS.IMPORT_IRODS_METADATA)}
                                            aria-label={formatMessage(intl, "importIRODSMetadata")}
                                            disabled={loading || isSubmitting || irodsAVUsSelected.length < 1}
                                            onClick={() => this.setState({showImportConfirmationDialog: true})}
                                            color="inherit"
                                >
                                    <SaveAltIcon/>
                                </IconButton>
                            </span>
                        </Tooltip>}

                        <Tooltip title={getMessage("saveToFile")}
                                 placement="bottom-start"
                                 enterDelay={200}
                        >
                            <span>
                                <IconButton id={build(ids.EDIT_METADATA_FORM, ids.BUTTONS.SAVE_METADATA_TO_FILE)}
                                            aria-label={formatMessage(intl, "saveToFile")}
                                            disabled={loading || dirty || isSubmitting}
                                            onClick={() => this.props.presenter.onSaveMetadataToFileBtnSelected()}
                                            color="inherit"
                                >
                                    <SaveIcon/>
                                </IconButton>
                            </span>
                        </Tooltip>

                        {editable &&
                        <Button id={build(ids.EDIT_METADATA_FORM, ids.BUTTONS.SAVE)}
                                disabled={loading || !dirty || isSubmitting || errors.error}
                                onClick={handleSubmit}
                                color="inherit"
                        >
                            {getMessage("save")}
                        </Button>}
                    </Toolbar>
                </AppBar>

                <DialogContent>
                    {loading ?
                        <CircularProgress className={classes.loadingStyle} size={50} thickness={4}/>
                        :
                        <Fragment>
                            {values["irods-avus"] &&
                            <AppBar position="static" color="default">
                                <Tabs value={tabIndex}
                                      onChange={this.handleTabChange}
                                      indicatorColor="primary"
                                      textColor="primary"
                                      variant="fullWidth"
                                >
                                    <Tab label={getMessage("userMetadata")}/>
                                    <Tab label={getMessage("additionalMetadata")}/>
                                </Tabs>
                            </AppBar>}

                            {tabIndex === 0 &&
                            <Fragment>
                                <FieldArray name="avus"
                                            render={arrayHelpers => (
                                                <MetadataList {...arrayHelpers}
                                                              field="avus"
                                                              editable={editable}
                                                              parentID={ids.EDIT_METADATA_FORM}
                                                              onEditAVU={(index) => this.setState({ editingAttrIndex: index })}
                                                />
                                            )}
                                />

                                <FieldArray name="avus"
                                            render={arrayHelpers => (
                                                <FormDialogEditAVU {...arrayHelpers}
                                                                   editable={editable}
                                                                   targetName={targetName}
                                                                   editingAttrIndex={editingAttrIndex}
                                                                   closeAttrDialog={() => this.setState({ editingAttrIndex: -1 })}
                                                />
                                            )}
                                />
                            </Fragment>}

                            {tabIndex === 1 &&
                            <Fragment>
                                <FieldArray name="irods-avus"
                                            render={arrayHelpers => (
                                                <MetadataList {...arrayHelpers}
                                                              field="irods-avus"
                                                              editable={false}
                                                              parentID={ids.EDIT_METADATA_FORM}
                                                              onEditAVU={(index) => this.setState({ editingAttrIndex: index })}
                                                              selectable={true}
                                                              onSelectAVU={this.handleSelectAVU}
                                                              onSelectAllClick={this.handleSelectAllAVUs}
                                                              avusSelected={irodsAVUsSelected}
                                                              rowsInPage={values["irods-avus"].length}
                                                />
                                            )}
                                />
                                <FieldArray name="irods-avus"
                                            render={arrayHelpers => (
                                                <FormDialogEditAVU {...arrayHelpers}
                                                                   editable={false}
                                                                   targetName={targetName}
                                                                   editingAttrIndex={editingAttrIndex}
                                                                   closeAttrDialog={() => this.setState({ editingAttrIndex: -1 })}
                                                />
                                            )}
                                />
                            </Fragment>}
                        </Fragment>
                    }
                </DialogContent>

                <ConfirmationDialog dialogOpen={showDiscardChangesDialog}
                                    debugId={ids.EDIT_METADATA_FORM}
                                    heading={getMessage("confirmDiscardChangesDialogHeader")}
                                    message={getMessage("confirmDiscardChangesDialogMsg")}
                                    onOkBtnClick={this.closeEditMetadataDialog}
                                    onCancelBtnClick={this.closeDiscardChangesDialog}
                />

                <ConfirmationDialog dialogOpen={showImportConfirmationDialog}
                                    debugId={ids.EDIT_METADATA_FORM}
                                    heading={getMessage("importIRODSMetadata")}
                                    message={formatHTMLMessage("importIRODSMetadataMsg")}
                                    onOkBtnClick={this.onConfirmImportIRODSMetadata}
                                    onCancelBtnClick={() => this.setState({ showImportConfirmationDialog: false })}
                />
            </Dialog>
        );
    }
}

const validateAVUs = avus => {
    const avusArrayErrors = [];

    avus.forEach((avu, index) => {
        const avuErrors = {};

        if (!avu.attr) {
            avuErrors.error = true;
            avuErrors.attr = getMessage("required");
            avusArrayErrors[index] = avuErrors;
        }

        if (avu.avus && avu.avus.length > 0) {
            const subAVUErros = validateAVUs(avu.avus);
            if (subAVUErros.length > 0) {
                avuErrors.error = true;
                avuErrors.avus = subAVUErros;
                avusArrayErrors[index] = avuErrors;
            }
        }
    });

    return avusArrayErrors;
};

const validate = values => {
    const errors = {};

    if (values.avus && values.avus.length > 0) {
        const avusArrayErrors = validateAVUs(values.avus);
        if (avusArrayErrors.length > 0) {
            errors.error = true;
            errors.avus = avusArrayErrors;
        }
    }

    return errors;
};

const handleSubmit = ({ avus, "irods-avus": irodsAVUs }, { props, setSubmitting, setStatus }) => {
    const resolve = (metadata) => {
        setSubmitting(false);
        setStatus({ success: true, metadata });
    };
    const errorCallback = (httpStatusCode, errorMessage) => {
        setSubmitting(false);
        setStatus({ success: false, errorMessage });
    };

    const metadata = { ...props.metadata, avus, "irods-avus": irodsAVUs };

    props.presenter.onSaveMetadata(
        metadata,
        resolve,
        errorCallback,
    );
};

export default withFormik(
    {
        enableReinitialize: true,
        mapPropsToValues: ({ metadata }) => ({...metadata}),
        validate,
        handleSubmit,
    }
)(withStyles(styles)(withI18N(injectIntl(EditMetadata), intlData)));
