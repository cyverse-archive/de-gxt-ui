/**
 *  @author sriram
 *
 **/

import React, { Component } from 'react';
import { withStyles } from "@material-ui/core";
import Dialog from '@material-ui/core/Dialog';
import DialogContent from '@material-ui/core/DialogContent';
import DEDialogHeader from "../../util/dialog/DEDialogHeader";
import withI18N, { getMessage } from "../../util/I18NWrapper";
import intlData from "../../tools/messages";
import exStyles from "../../tools/style";
import { injectIntl } from "react-intl";
import { ErrorMessage, Field, Form, Formik } from 'formik';
import { FormMultilineTextField, FormTextField } from "../../util/FormField";
import Button from "@material-ui/core/Button";


class NewToolRequestForm extends Component {
    render() {
        const {open, intl} = this.props;
        return (
            <Dialog open={open}
                    disableBackdropClick
                    disableEscapeKeyDown>
                <DEDialogHeader heading={intl.formatMessage({id: "newToolRequestDialogHeading"})}/>
                <DialogContent>
                    <Formik
                        render={({errors, status, touched, isSubmitting}) => (
                            <Form>
                                <Field name="toolName"
                                       label={getMessage("toolNameLabel")}
                                       required={true}
                                       margin="dense"
                                       component={FormTextField}/>
                                <ErrorMessage name="toolName" component="div"/>
                                <Field name="toolDesc"
                                       label={getMessage("toolDescLabel")}
                                       required={true}
                                       margin="dense"
                                       component={FormMultilineTextField}/>
                                <ErrorMessage name="toolDesc" component="div"/>
                                <Field name="toolSrcLink"
                                       label={getMessage("toolSrcLinkLabel")}
                                       required={true}
                                       margin="dense"
                                       component={FormTextField}/>
                                <ErrorMessage name="toolSrcLink" component="div"/>
                                <Field name="toolVersion"
                                       label={getMessage("toolVersionLabel")}
                                       required={true}
                                       margin="dense"
                                       component={FormTextField}/>
                                <ErrorMessage name="toolVersion" component="div"/>
                                <Field name="toolDocumentation"
                                       label={getMessage("toolDocumentationLabel")}
                                       required={true}
                                       margin="dense"
                                       component={FormTextField}/>
                                <ErrorMessage name="toolDocumentation" component="div"/>
                                <Field name="toolInstructions"
                                       label={getMessage("toolInstructionsLabel")}
                                       required={false}
                                       margin="dense"
                                       component={FormMultilineTextField}/>
                                <ErrorMessage name="toolInstructions" component="div"/>
                                <Field name="toolTestData"
                                       label={getMessage("toolTestDataLabel")}
                                       required={false}
                                       margin="dense"
                                       component={FormTextField}/>
                                <ErrorMessage name="toolTestData" component="div"/>


                                <Button style={{float: "right"}} variant="contained"
                                        color="primary"
                                        type="submit"
                                        disabled={isSubmitting}>
                                    Submit
                                </Button>
                            </Form>
                        )}
                    />
                </DialogContent>
            </Dialog>
        );
    }
}

NewToolRequestForm.propTypes = {};

export default withStyles(exStyles)(withI18N(injectIntl(NewToolRequestForm), intlData));
