/**
 *
 * @author sriram
 *
 */
import React from "react";
import { Field, Form, Formik } from "formik";
import { injectIntl } from "react-intl";

import ids from "../ids";
import intlData from "../messages";

import {
    build,
    DEDialogHeader,
    FormCheckbox,
    FormTextField,
    formatMessage,
    getMessage,
    withI18N,
} from "@cyverse-de/ui-lib";

import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    Tooltip,
} from "@material-ui/core";

function CreateQuickLaunchDialog(props) {
    const { dialogOpen, appName, presenter, intl, baseDebugId } = props;

    const handleSubmit = (values, actions) => {
        actions.setSubmitting(true);
        const { name, is_public } = values;
        presenter.createQuickLaunch(
            name,
            "",
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
                                component={FormTextField}
                            />
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
                                onClick={handleClose}
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
