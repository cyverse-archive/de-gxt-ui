import React, { Fragment } from "react";

import ids from "../ids";
import messages from "../messages";
import { options } from "./Operators";
import SelectOperator from "./SelectOperator";
import styles from "../styles";
import SubjectSearchField from "../../../collaborators/SubjectSearchField";
import UserPanel from "./UserPanel";

import {
    build,
    FormSelectField,
    getFormError,
    getMessage,
    withI18N,
} from "@cyverse-de/ui-lib";
import { Field, FieldArray, getIn } from "formik";
import { FormHelperText, MenuItem, withStyles } from "@material-ui/core";

/**
 * A component which allows users to specify a list of users which must have certain
 * permissions in QueryBuilder
 */

const PERMISSION_DEFAULT = {
    permission: "read",
    users: [],
};

const Permissions = withI18N(PermissionsClause, messages);

function PermissionsClause(props) {
    const operators = [
        options.AreAtLeast,
        options.Are,
        options.AreNotAtLeast,
        options.AreNot,
    ];

    const permissions = [
        {
            value: "read",
            label: getMessage("read"),
        },
        {
            value: "write",
            label: getMessage("write"),
        },
        {
            value: "own",
            label: getMessage("own"),
        },
    ];

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
                name={`${name}.permission`}
                id={build(parentId, ids.permissionList)}
                render={({
                    field,
                    form: { setFieldValue, ...form },
                    ...props
                }) => {
                    return (
                        <FormSelectField
                            {...props}
                            form={form}
                            field={field}
                            fullWidth={false}
                        >
                            {permissions &&
                                permissions.map((permission, index) => {
                                    return (
                                        <MenuItem
                                            key={index}
                                            value={permission.value}
                                        >
                                            {permission.label}
                                        </MenuItem>
                                    );
                                })}
                        </FormSelectField>
                    );
                }}
            />
            <FieldArray
                name={`${name}.users`}
                render={(arrayHelpers) => (
                    <SubjectSearch
                        presenter={presenter}
                        collaboratorsUtil={collaboratorsUtil}
                        parentId={parentId}
                        {...arrayHelpers}
                    />
                )}
            />
        </Fragment>
    );
}

const SubjectSearch = withStyles(styles)(renderSubjectSearch);

function renderSubjectSearch(props) {
    const {
        name,
        presenter,
        collaboratorsUtil,
        push,
        remove,
        parentId,
        form: { values, touched, errors },
        classes,
    } = props;

    let users = getIn(values, name);
    const errorMsg = getFormError(name, touched, errors);

    return (
        <div className={classes.autocompleteField}>
            <SubjectSearchField
                presenter={presenter}
                collaboratorsUtil={collaboratorsUtil}
                parentId={parentId}
                onSelect={push}
            />
            <FormHelperText error>{errorMsg}</FormHelperText>
            {users && users.length > 0 && (
                <UserPanel
                    users={users}
                    id={build(parentId, ids.userList)}
                    collaboratorsUtil={collaboratorsUtil}
                    onDelete={(index) => {
                        remove(index);
                    }}
                />
            )}
        </div>
    );
}

export { Permissions, PERMISSION_DEFAULT };
