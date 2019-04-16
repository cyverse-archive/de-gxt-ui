/**
 *
 * @author sriram
 *
 */
import React from "react";
import withI18N, { formatMessage, getMessage } from "../../util/I18NWrapper";
import { injectIntl } from "react-intl";
import intlData from "../messages";
import Dialog from "@material-ui/core/Dialog";
import DEDialogHeader from "../../util/dialog/DEDialogHeader";
import DialogContent from "@material-ui/core/DialogContent";
import { ErrorMessage, Field, Form, Formik } from "formik";
import {
    FormCheckbox,
    FormMultilineTextField,
    FormTextField,
} from "../../util/FormField";
import Button from "@material-ui/core/Button";
import DialogActions from "@material-ui/core/DialogActions";
import { Tooltip } from "@material-ui/core";

function CreateQuickLaunchDialog(props) {
    const { dialogOpen, appName, isOwner, presenter, intl } = props;

    const handleSubmit = (values, actions) => {
        console.log("submit clicked->" + values.name);
        actions.setSubmitting(false);
    };

    const handleClose = () => {
        presenter.onHideCreateQuickLaunchRequestDialog();
    };

    return (
        <Dialog open={dialogOpen}>
            <DEDialogHeader heading={appName} onClose={handleClose} />
            <Formik
                enableReinitialize={true}
                onSubmit={handleSubmit}
                render={({ errors, status, touched, isSubmitting }) => (
                    <Form>
                        <DialogContent>
                            <Field
                                name="name"
                                label={getMessage("quickLaunchNameLabel")}
                                required={true}
                                margin="dense"
                                component={FormTextField}
                            />
                            <ErrorMessage name="name" component="div" />
                            <Field
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
                                    name="default"
                                    label={getMessage("publicLabel")}
                                    required={false}
                                    margin="dense"
                                    component={FormCheckbox}
                                />
                            </Tooltip>
                            <br />
                        </DialogContent>
                        <DialogActions>
                            <Button color="primary" disabled={isSubmitting}>
                                {getMessage("cancelLabel")}
                            </Button>
                            <Button
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
