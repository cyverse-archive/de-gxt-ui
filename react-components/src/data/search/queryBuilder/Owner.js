import React, { Fragment } from "react";

import ids from "../ids";
import { options } from "./Operators";
import SelectOperator from "./SelectOperator";
import styles from "../styles";
import SubjectSearchField from "../../../collaborators/SubjectSearchField";
import UserPanel from "./UserPanel";

import { build, getFormError } from "@cyverse-de/ui-lib";
import { Field } from "formik";
import FormHelperText from "@material-ui/core/FormHelperText";
import { withStyles } from "@material-ui/core/styles";

/**
 * A component which allows users to specify an owner in QueryBuilder
 */
const OWNER_DEFAULT = { owner: "" };

function Owner(props) {
    const operators = [options.Is, options.IsNot];

    const {
        parentId,
        field: { name },
        presenter,
        collaboratorsUtil,
    } = props;

    return (
        <Fragment>
            <SelectOperator
                operators={operators}
                parentId={parentId}
                name={name}
            />
            <Field
                name={`${name}.owner`}
                presenter={presenter}
                collaboratorsUtil={collaboratorsUtil}
                parentId={parentId}
                component={SubjectSearch}
            />
        </Fragment>
    );
}

const SubjectSearch = withStyles(styles)(renderSubjectSearch);

function renderSubjectSearch(props) {
    const {
        presenter,
        collaboratorsUtil,
        parentId,
        classes,
        field,
        form: { setFieldValue, setFieldTouched, touched, errors },
    } = props;

    let fieldName = field.name;
    let collaborator = field.value;
    const errorMsg = getFormError(fieldName, touched, errors);

    return (
        <div className={classes.autocompleteField}>
            <SubjectSearchField
                presenter={presenter}
                collaboratorsUtil={collaboratorsUtil}
                parentId={parentId}
                onBlur={() => setFieldTouched(fieldName, true)}
                onSelect={(collaborator) =>
                    setFieldValue(fieldName, collaborator)
                }
            />
            {collaborator && (
                <UserPanel
                    users={collaborator ? [collaborator] : null}
                    id={build(parentId, ids.userList)}
                    collaboratorsUtil={collaboratorsUtil}
                    onDelete={() => setFieldValue(fieldName, null)}
                />
            )}
            <FormHelperText error>{errorMsg}</FormHelperText>
        </div>
    );
}

export { Owner, OWNER_DEFAULT };
