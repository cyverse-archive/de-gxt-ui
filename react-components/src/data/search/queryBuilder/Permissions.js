import messages from "../messages";
import { options } from "./Operators";
import SelectOperator from "./SelectOperator";
import styles from "../styles";
import SubjectSearchField from "../../../collaborators/SubjectSearchField";
import withI18N, { getMessage } from "../../../util/I18NWrapper";

import Chip from "@material-ui/core/Chip";
import { Field, FieldArray } from "redux-form";
import Grid from "@material-ui/core/Grid";
import injectSheet from "react-jss";
import MenuItem from "@material-ui/core/MenuItem";
import Paper from "@material-ui/core/Paper";
import React, { Component, Fragment } from "react";
import Select from "@material-ui/core/Select";
import Tooltip from "@material-ui/core/Tooltip";

class Permissions extends Component {
    render() {
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
            helperProps: {
                presenter,
                classes
            }
        } = this.props;

        return (
            <Fragment>
                <SelectOperator operators={operators}/>
                <Field name='permission'
                       permissions={permissions}
                       component={renderSelect}/>
                <FieldArray name='users'
                            presenter={presenter}
                            classes={classes}
                            component={renderSubjectSearch}/>
            </Fragment>
        )
    }
}

function renderSelect(props) {
    let {
        input,
        permissions
    } = props;

    if (input.value === "") {
        input.onChange(permissions[0].value)
    }

    return (
        <Grid item>
            <Select value={input.value}
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
        fields
    } = props;

    return (
        <Grid item>
            <SubjectSearchField presenter={presenter}
                                onSelect={(collaborator) => fields.push(collaborator)}/>
            {fields.getAll() && <UserPanel classes={classes}
                                           fields={fields}
                                           onDelete={fields.remove}/>}
        </Grid>
    )
}

function UserPanel(props) {
    let {
        fields,
        onDelete,
        classes
    } = props;
    let users = fields.getAll();
    let chips = users && users.map((user, index) =>
        <Tooltip title={user.institution ? user.institution : user.description}>
            <Chip key={user.id}
                  className={classes.userChip}
                  onDelete={() => onDelete(index)}
                  label={user.name}/>
        </Tooltip>
    );

    return (
        <Paper className={classes.permissionUsers}>
            {chips}
        </Paper>
    )
}

export default injectSheet(styles)(withI18N(Permissions, messages));