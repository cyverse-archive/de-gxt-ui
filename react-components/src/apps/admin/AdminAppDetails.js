/**
 * @author aramsey
 */

import React from "react";

import ids from "../ids";
import messages from "../messages";
import styles from "../style";

import {
    build,
    DEDialogHeader,
    DEHyperlink,
    FormCheckbox,
    FormMultilineTextField,
    FormTextField,
    getMessage,
    LoadingMask,
    withI18N,
} from "@cyverse-de/ui-lib";
import { Field, Form, withFormik } from "formik";
import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    Paper,
    Typography,
} from "@material-ui/core";
import { withStyles } from "@material-ui/core/styles";
import PropTypes from "prop-types";

function AdminAppDetailsDialog(props) {
    const {
        open,
        app,
        parentId,
        presenter,
        restrictedChars,
        restrictedStartingChars,
        createDocWikiUrl,
        documentationTemplateUrl,
        handleSubmit,
        handleReset,
        isSubmitting,
    } = props;

    const handleClose = () => {
        handleReset();
        presenter.closeAppDetailsDlg();
    };

    return (
        <Dialog
            open={open}
            fullWidth={true}
            maxWidth="lg"
            onClose={handleClose}
            id={parentId}
        >
            <DEDialogHeader
                messages={messages.messages}
                heading={app.name}
                onClose={handleClose}
            />
            <DialogContent>
                <LoadingMask loading={isSubmitting}>
                    <StyledAdminAppDetailsForm
                        parentId={parentId}
                        presenter={presenter}
                        restrictedChars={restrictedChars}
                        restrictedStartingChars={restrictedStartingChars}
                        createDocWikiUrl={createDocWikiUrl}
                        documentationTemplateUrl={documentationTemplateUrl}
                    />
                </LoadingMask>
            </DialogContent>
            <DialogActions>
                <Button
                    variant="contained"
                    id={build(parentId, ids.BUTTONS.CANCEL)}
                    onClick={handleClose}
                >
                    {getMessage("cancelLabel")}
                </Button>
                <Button
                    variant="contained"
                    id={build(parentId, ids.BUTTONS.SAVE)}
                    type="submit"
                    color="primary"
                    onClick={handleSubmit}
                >
                    {getMessage("save")}
                </Button>
            </DialogActions>
        </Dialog>
    );
}

const StyledAdminAppDetailsForm = withStyles(styles)(AdminAppDetailsForm);

function AdminAppDetailsForm(props) {
    const {
        parentId,
        restrictedChars,
        restrictedStartingChars,
        createDocWikiUrl,
        documentationTemplateUrl,
        classes,
    } = props;

    return (
        <Form>
            <Field
                name={"app.name"}
                label={getMessage("appName")}
                id={build(parentId, ids.ADMIN_DETAILS.NAME)}
                validate={(value) =>
                    validateAppName(
                        restrictedStartingChars,
                        restrictedChars,
                        value
                    )
                }
                component={FormTextField}
            />
            <Field
                name={"app.description"}
                label={getMessage("descriptionLabel")}
                id={build(parentId, ids.ADMIN_DETAILS.DESCRIPTION)}
                component={FormMultilineTextField}
            />
            <Field
                name={"app.integrator_name"}
                label={getMessage("integratorName")}
                id={build(parentId, ids.ADMIN_DETAILS.INTEGRATOR)}
                component={FormTextField}
            />
            <Field
                name={"app.integrator_email"}
                label={getMessage("integratorEmail")}
                id={build(parentId, ids.ADMIN_DETAILS.INTEGRATOR_EMAIL)}
                component={FormTextField}
            />
            <Field
                name={"app.deleted"}
                label={getMessage("deleted")}
                id={build(parentId, ids.ADMIN_DETAILS.DELETED)}
                component={FormCheckbox}
            />
            <Field
                name={"app.disabled"}
                label={getMessage("disabled")}
                id={build(parentId, ids.ADMIN_DETAILS.DISABLED)}
                component={FormCheckbox}
            />
            <Field
                name={"app.beta"}
                label={getMessage("beta")}
                id={build(parentId, ids.ADMIN_DETAILS.BETA)}
                component={FormCheckbox}
            />
            <div>
                <DEHyperlink
                    text={getMessage("wikiURLInstructions")}
                    onClick={() => window.open(createDocWikiUrl)}
                />
            </div>
            <Field
                name={"app.wiki_url"}
                label={getMessage("wikiUrl")}
                id={build(parentId, ids.ADMIN_DETAILS.WIKI_URL)}
                component={FormTextField}
            />
            <Paper elevation={1}>
                <Typography variant="body2" classes={{ root: classes.paper }}>
                    {getMessage("documentationInstructions")}
                    <DEHyperlink
                        text={getMessage("documentationTemplate")}
                        onClick={() => window.open(documentationTemplateUrl)}
                    />
                </Typography>
            </Paper>
            <Field
                name={"documentation"}
                label={getMessage("appDocumentation")}
                id={build(parentId, ids.ADMIN_DETAILS.DOCUMENTATION)}
                multiline
                rows={15}
                component={FormTextField}
            />
        </Form>
    );
}

function validateAppName(restrictedStartingChars, restrictedChars, value) {
    if (!value) {
        return "Empty value";
    }

    let startingCharsRegex = new RegExp("^[" + restrictedStartingChars + "]");
    let invalid = value.match(startingCharsRegex);
    if (invalid) {
        return (
            "App name cannot begin with `" +
            restrictedStartingChars +
            "`. Invalid char: `" +
            invalid +
            "`"
        );
    }

    // Escape each non-alphanumeric char since some are used as special chars in regex
    let escapedRestrictedChars = restrictedChars.replace(/\W/g, "\\$&");
    let restrictedCharsRegex = new RegExp(
        "[" + escapedRestrictedChars + "]",
        "g"
    );
    invalid = value.match(restrictedCharsRegex);
    if (invalid) {
        return (
            "App name cannot contain `" +
            restrictedChars +
            "`. Invalid chars: `" +
            invalid.join("") +
            "`"
        );
    }
}

const handleSubmit = (values, { props, setSubmitting }) => {
    const { presenter } = props;

    let promises = [];
    if (props.app !== values.app) {
        let saveApp = new Promise((resolve, reject) => {
            presenter.onSaveAppSelected(values.app, resolve, reject);
        });
        promises.push(saveApp);
    }

    if (props.app.beta !== values.app.beta) {
        let betaUpdate = new Promise((resolve, reject) => {
            presenter.updateBetaStatus(values.app, resolve, reject);
        });
        promises.push(betaUpdate);
    }

    if (!props.documentation && values.documentation) {
        let addAppDoc = new Promise((resolve, reject) => {
            presenter.addAppDocumentation(
                values.app.system_id,
                values.app.id,
                values.documentation,
                resolve,
                reject
            );
        });
        promises.push(addAppDoc);
    } else if (props.documentation !== values.documentation) {
        let updateAppDoc = new Promise((resolve, reject) => {
            presenter.updateAppDocumentation(
                values.app.system_id,
                values.app.id,
                values.documentation,
                resolve,
                reject
            );
        });
        promises.push(updateAppDoc);
    }

    Promise.all(promises)
        .then(() => {
            setSubmitting(false);
            props.presenter.closeAppDetailsDlg();
        })
        .catch(() => {
            setSubmitting(false);
        });
};

function mapPropsToValues(props) {
    const { app, documentation } = props;
    return {
        app: app,
        documentation: documentation,
    };
}

AdminAppDetailsDialog.propTypes = {
    open: PropTypes.bool.isRequired,
    presenter: PropTypes.shape({
        onSaveAppSelected: PropTypes.func.isRequired,
        addAppDocumentation: PropTypes.func.isRequired,
        updateAppDocumentation: PropTypes.func.isRequired,
        updateBetaStatus: PropTypes.func.isRequired,
        closeAppDetailsDlg: PropTypes.func.isRequired,
    }),
    app: PropTypes.object.isRequired,
    documentation: PropTypes.string.isRequired,
    restrictedChars: PropTypes.string.isRequired,
    restrictedStartingChars: PropTypes.string.isRequired,
    createDocWikiUrl: PropTypes.string.isRequired,
    documentationTemplateUrl: PropTypes.string.isRequired,
    parentId: PropTypes.string.isRequired,
};

export default withFormik({
    enableReinitialize: true,
    mapPropsToValues,
    handleSubmit,
})(withI18N(AdminAppDetailsDialog, messages));
