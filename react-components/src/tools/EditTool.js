/**
 * @author aramsey
 */
import React from "react";
import ContainerImage from "./ContainerImage";
import ContainerPorts from "./ContainerPorts";
import ids from "./ids";
import messages from "./messages";
import Restrictions from "./ToolRestrictions";
import { nonEmptyField } from "./Validations";

import {
    build,
    DEDialogHeader,
    FormMultilineTextField,
    FormNumberField,
    FormSelectField,
    FormTextField,
    getMessage,
    LoadingMask,
    withI18N,
} from "@cyverse-de/ui-lib";
import { Field, FieldArray, Form, getIn, withFormik } from "formik";
import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    MenuItem,
} from "@material-ui/core";
import PropTypes from "prop-types";

function EditToolDialog(props) {
    const {
        open,
        parentId,
        tool,
        loading,
        toolTypes,
        maxCPUCore,
        maxMemory,
        maxDiskSpace,
        handleSubmit,
        values,
        presenter,
    } = props;

    return (
        <Dialog
            open={open}
            fullWidth={true}
            maxWidth="lg"
            onClose={() => presenter.closeEditToolDlg()}
            id={parentId}
        >
            <DEDialogHeader
                messages={messages.messages}
                heading={
                    tool
                        ? getMessage("editTool", {
                              values: { name: tool.name },
                          })
                        : getMessage("addTool")
                }
                onClose={() => presenter.closeEditToolDlg()}
            />
            <DialogContent>
                <LoadingMask loading={loading}>
                    <EditToolForm
                        values={values}
                        parentId={parentId}
                        toolTypes={toolTypes}
                        maxCPUCore={maxCPUCore}
                        maxMemory={maxMemory}
                        maxDiskSpace={maxDiskSpace}
                    />
                </LoadingMask>
            </DialogContent>
            <DialogActions>
                <Button
                    variant="contained"
                    id={build(parentId, ids.BUTTONS.CANCEL)}
                    onClick={() => presenter.closeEditToolDlg()}
                >
                    {getMessage("cancel")}
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

function EditToolForm(props) {
    const {
        values,
        parentId,
        toolTypes,
        maxCPUCore,
        maxMemory,
        maxDiskSpace,
    } = props;

    const selectedToolType = getIn(values, "type");
    const isOSGTool = selectedToolType === "osg";
    const isInteractiveTool = selectedToolType === "interactive";

    return (
        <Form>
            <Field
                name="name"
                label={getMessage("toolName")}
                id={build(parentId, ids.EDIT_TOOL_DLG.NAME)}
                required
                validate={nonEmptyField}
                component={FormTextField}
            />
            <Field
                name="description"
                label={getMessage("toolDesc")}
                id={build(parentId, ids.EDIT_TOOL_DLG.DESCRIPTION)}
                component={FormMultilineTextField}
            />
            <Field
                name="version"
                label={getMessage("toolVersion")}
                id={build(parentId, ids.EDIT_TOOL_DLG.VERSION)}
                required
                validate={nonEmptyField}
                component={FormTextField}
            />
            <Field
                name="type"
                validate={nonEmptyField}
                render={({ field: { onChange, ...field }, ...props }) => (
                    <FormSelectField
                        {...props}
                        label={getMessage("type")}
                        required
                        field={field}
                        onChange={(event) => {
                            resetOnTypeChange(event.target.value, props.form);
                            onChange(event);
                        }}
                        id={build(parentId, ids.EDIT_TOOL_DLG.TYPE)}
                    >
                        {toolTypes.map((type, index) => (
                            <MenuItem
                                key={index}
                                value={type}
                                id={build(
                                    parentId,
                                    ids.EDIT_TOOL_DLG.TYPE,
                                    type
                                )}
                            >
                                {type}
                            </MenuItem>
                        ))}
                    </FormSelectField>
                )}
            />
            <Field
                name={"container.image"}
                parentId={parentId}
                isOSGTool={isOSGTool}
                component={ContainerImage}
            />
            <Field
                name="container.entrypoint"
                label={getMessage("entrypoint")}
                id={build(parentId, ids.EDIT_TOOL_DLG.ENTRYPOINT)}
                component={FormTextField}
            />
            <Field
                name="container.working_directory"
                label={getMessage("workingDirectory")}
                id={build(parentId, ids.EDIT_TOOL_DLG.WORKING_DIR)}
                component={FormTextField}
            />
            <Field
                name="container.uid"
                label={getMessage("containerUID")}
                id={build(parentId, ids.EDIT_TOOL_DLG.CONTAINER_UID)}
                component={FormNumberField}
            />
            {isInteractiveTool && (
                <FieldArray
                    name="container.container_ports"
                    render={(arrayHelpers) => (
                        <ContainerPorts
                            parentId={build(
                                parentId,
                                ids.EDIT_TOOL_DLG.CONTAINER_PORTS
                            )}
                            {...arrayHelpers}
                        />
                    )}
                />
            )}
            <Restrictions
                parentId={build(parentId, ids.EDIT_TOOL_DLG.RESTRICTIONS)}
                maxDiskSpace={maxDiskSpace}
                maxCPUCore={maxCPUCore}
                maxMemory={maxMemory}
            />
        </Form>
    );
}

/**
 * Ensures that if the user previously filled out information for an OSG
 * or interactive/VICE tool, and then selects a different type,
 * that those fields get cleared out to prevent any validation errors and
 * also to prevent empty values being unintentionally sent to the service
 *
 * @param currentType
 * @param form
 */
function resetOnTypeChange(currentType, form) {
    if (currentType !== "osg") {
        form.setFieldValue("container.image.osg_image_path", null);
    }
    if (currentType !== "interactive") {
        form.setFieldValue("container.container_ports", null);
    }
}

const handleSubmit = (values, { props }) => {
    const { tool, presenter } = props;
    if (tool) {
        presenter.updateTool(values);
    } else {
        presenter.addTool(values);
    }
};

const DEFAULT_TOOL = {
    name: "",
    version: "",
    container: {
        image: {
            name: "",
            tag: "",
            osg_image_path: "",
        },
    },
    type: "",
};

function mapPropsToValues(props) {
    const { tool } = props;
    if (!tool) {
        return { ...DEFAULT_TOOL };
    } else {
        return { ...tool };
    }
}

EditToolDialog.propTypes = {
    open: PropTypes.bool.isRequired,
    presenter: PropTypes.shape({
        addTool: PropTypes.func.isRequired,
        updateTool: PropTypes.func.isRequired,
        closeEditToolDlg: PropTypes.func.isRequired,
    }),
    loading: PropTypes.bool.isRequired,
    tool: PropTypes.object,
    parentId: PropTypes.string.isRequired,
    toolTypes: PropTypes.array.isRequired,
    maxCPUCore: PropTypes.number.isRequired,
    maxMemory: PropTypes.number.isRequired,
    maxDiskSpace: PropTypes.number.isRequired,
};

export default withFormik({
    enableReinitialize: true,
    mapPropsToValues,
    handleSubmit,
})(withI18N(EditToolDialog, messages));
