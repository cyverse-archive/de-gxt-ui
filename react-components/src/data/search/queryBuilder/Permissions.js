import build from "../../../util/DebugIDUtil";
import ids from "../ids";
import messages from "../messages";
import { options } from "./Operators";
import SelectOperator from "./SelectOperator";
import styles from "../styles";
import SubjectSearchField from "../../../collaborators/SubjectSearchField";
import withI18N, { getMessage } from "../../../util/I18NWrapper";

import Chip from "@material-ui/core/Chip";
import { Field, FieldArray } from "redux-form";
import Grid from "@material-ui/core/Grid";
import MenuItem from "@material-ui/core/MenuItem";
import Paper from "@material-ui/core/Paper";
import React, { Fragment } from "react";
import Select from "@material-ui/core/Select";
import Tooltip from "@material-ui/core/Tooltip";
import { withStyles } from "@material-ui/core/styles";

/**
 * A component which allows users to specify a list of users which must have certain
 * permissions in QueryBuilder
 */
function Permissions(props) {
    let operators = [
        options.Are,
        options.AreNot,
        options.AreAtLeast,
        options.AreNotAtLeast
    ];

    let permissions = [
        {
            value: 'read',
            label: getMessage('read')
        },
        {
            value: 'write',
            label: getMessage('write')
        },
        {
            value: 'own',
            label: getMessage('own')
        }
    ];

    let {
        parentId,
        helperProps: {
            presenter,
            classes
        }
    } = props;

    return (
        <Fragment>
            <SelectOperator operators={operators}
                            parentId={parentId}/>
            <Field name='permission'
                   permissions={permissions}
                   id={build(parentId, ids.permissionList)}
                   component={renderSelect}/>
            <FieldArray name='users'
                        presenter={presenter}
                        classes={classes}
                        parentId={parentId}
                        component={renderSubjectSearch}/>
        </Fragment>
    )
}

function renderSelect(props) {
    let {
        input,
        permissions,
        id
    } = props;

    if (input.value === "") {
        input.onChange(permissions[0].value)
    }

    return (
        <Grid item>
            <Select value={input.value}
                    id={id}
                    onChange={(event) => input.onChange(event.target.value)}>
                {permissions && permissions.map((permission, index) => {
                    return <MenuItem key={index} value={permission.value}>{permission.label}</MenuItem>
                })}
            </Select>
        </Grid>
    )
}

function renderSubjectSearch(props) {
    let {
        presenter,
        classes,
        fields,
        parentId
    } = props;

    return (
        <Grid item>
            <SubjectSearchField presenter={presenter}
                                parentId={parentId}
                                onSelect={(collaborator) => fields.push(collaborator)}/>
            {fields.getAll() && <UserPanel classes={classes}
                                           fields={fields}
                                           id={build(parentId, ids.userList)}
                                           onDelete={fields.remove}/>}
        </Grid>
    )
}

function UserPanel(props) {
    let {
        fields,
        onDelete,
        classes,
        id
    } = props;
    let users = fields.getAll();
    let chips = users && users.map((user, index) =>
        <Tooltip key={user.id}
                 title={user.institution ? user.institution : user.description}>
            <Chip key={user.id}
                  id={user.id}
                  className={classes.userChip}
                  onDelete={() => onDelete(index)}
                  label={user.name}/>
        </Tooltip>
    );

    return (
        <Paper className={classes.permissionUsers}
               id={id}>
            {chips}
        </Paper>
    )
}

export default withStyles(styles)(withI18N(Permissions, messages));