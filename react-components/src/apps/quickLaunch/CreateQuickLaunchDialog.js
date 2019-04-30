/**
 *
 * @author sriram
 *
 */
import React from "react";
import { ErrorMessage, Field, Form, Formik } from "formik";
import { injectIntl } from "react-intl";

import build from "../../util/DebugIDUtil";
import ids from "../ids";
import intlData from "../messages";
import withI18N, { formatMessage, getMessage } from "../../util/I18NWrapper";

import {
    FormCheckbox,
    FormMultilineTextField,
    FormTextField,
} from "../../util/FormField";
import DEDialogHeader from "../../util/dialog/DEDialogHeader";

import Button from "@material-ui/core/Button";
import Dialog from "@material-ui/core/Dialog";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import Tooltip from "@material-ui/core/Tooltip";

function CreateQuickLaunchDialog(props) {
    const { dialogOpen, appName, presenter, intl, baseDebugId } = props;

    const handleSubmit = (values, actions) => {
        actions.setSubmitting(true);
        const { name, description, is_public } = values;
        presenter.createQuickLaunch(
            name,
            description,
            is_public,
            () => {
                actions.setSubmitting(false);
                handleClose();
            },
            (statusCode, errMessage) => {
                actions.setSubmitting(false);
            }
        );
    };

    const handleClose = () => {
        presenter.onHideCreateQuickLaunchRequestDialog();
    };

    return (
        <Dialog open={dialogOpen}>
            <DEDialogHeader heading={appName} onClose={handleClose} />
            <Formik
                initialValues={{ description: "", is_public: false }}
                enableReinitialize={true}
                onSubmit={handleSubmit}
                render={({ errors, status, touched, isSubmitting }) => (
                    <Form>
                        <DialogContent>
                            <Field
                                id={build(baseDebugId, ids.QUICK_LAUNCH.name)}
                                name="name"
                                label={getMessage("quickLaunchNameLabel")}
                                required={true}
                                margin="dense"
                                component={FormTextField}
                            />
                            <ErrorMessage name="name" component="div" />
                            <Field
                                id={build(
                                    baseDebugId,
                                    ids.QUICK_LAUNCH.description
                                )}
                                name="description"
                                label={getMessage("descriptionLabel")}
                                required={false}
                                margin="dense"
                                component={FormMultilineTextField}
                            />
                            <ErrorMessage name="description" component="div" />
                            <Tooltip
                                title={formatMessage(intl, "publicQLTooltip")}
                            >
                                <Field
                                    id={build(
                                        baseDebugId,
                                        ids.QUICK_LAUNCH.public
                                    )}
                                    name="is_public"
                                    label={getMessage("publicLabel")}
                                    required={false}
                                    margin="dense"
                                    component={FormCheckbox}
                                />
                            </Tooltip>
                            <br />
                        </DialogContent>
                        <DialogActions>
                            <Button
                                id={build(baseDebugId, ids.QUICK_LAUNCH.cancel)}
                                color="primary"
                                disabled={isSubmitting}
                            >
                                {getMessage("cancelLabel")}
                            </Button>
                            <Button
                                id={build(baseDebugId, ids.QUICK_LAUNCH.create)}
                                style={{ float: "right" }}
                                variant="contained"
                                color="primary"
                                type="submit"
                                disabled={isSubmitting}
                            >
                                {getMessage("createQuickLaunchLabel")}
                            </Button>
                        </DialogActions>
                    </Form>
                )}
            />
        </Dialog>
    );
}

export default withI18N(injectIntl(CreateQuickLaunchDialog), intlData);
