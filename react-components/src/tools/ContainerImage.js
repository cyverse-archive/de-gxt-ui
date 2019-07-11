import React from "react";

import ids from "./ids";
import SimpleExpansionPanel from "./SimpleExpansionPanel";
import { nonEmptyField } from "./Validations";

import {
    build,
    FormTextField,
    getMessage,
    getFormError,
} from "@cyverse-de/ui-lib";
import { Field } from "formik";

function ContainerImage(props) {
    const {
        isOSGTool,
        parentId,
        field: { name },
        form: { errors, touched },
    } = props;

    let hasErrors = !!getFormError(name, touched, errors);
    return (
        <SimpleExpansionPanel
            header={getMessage("containerImage")}
            parentId={build(parentId, ids.EDIT_TOOL_DLG.CONTAINER_IMAGE)}
            hasErrors={hasErrors}
        >
            <Field
                name={`${name}.name`}
                label={getMessage("imageName")}
                id={build(parentId, ids.EDIT_TOOL_DLG.IMAGE_NAME)}
                required
                validate={nonEmptyField}
                component={FormTextField}
            />
            <Field
                name={`${name}.url`}
                label={getMessage("dockerHubURL")}
                id={build(parentId, ids.EDIT_TOOL_DLG.DOCKER_URL)}
                component={FormTextField}
            />
            <Field
                name={`${name}.tag`}
                label={getMessage("tag")}
                id={build(parentId, ids.EDIT_TOOL_DLG.TAG)}
                required
                validate={nonEmptyField}
                component={FormTextField}
            />
            {isOSGTool && (
                <Field
                    name={`${name}.osg_image_path`}
                    label={getMessage("osgImagePath")}
                    required
                    validate={(value) => isOSGTool && nonEmptyField(value)}
                    id={build(parentId, ids.EDIT_TOOL_DLG.OSG_IMAGE_PATH)}
                    component={FormTextField}
                />
            )}
        </SimpleExpansionPanel>
    );
}

export default ContainerImage;
