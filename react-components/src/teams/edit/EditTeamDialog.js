/**
 * @author aramsey
 *
 * A dialog which allows users to create, update, or view a team, which is a group.
 *
 * Groups can have members and groups can have privileges upon the group.
 *
 * The form allows users to set public privileges meaning privileges for all users who are not a member
 * of the group. Beyond that, while membership and privileges are 2 separate functionalities,
 * the Edit Teams form merges these two together for the sake of simplicity i.e. adding a member requires
 * the user to also set privileges for that member.
 *
 * A list of privileges is in {@link Privilege}
 *
 */

import React, { useState } from "react";

import ids from "../ids";
import messages from "../messages";
import Privilege from "../../models/Privilege";
import {
    getMemberPrivileges,
    getUpdateRequest,
    getUserPrivileges,
    privilegeToPrivilegeList,
    simplifyPrivileges,
    subjectIsMember,
} from "./TeamHelperFunctions";

import {
    build,
    DEConfirmationDialog,
    DEDialogHeader,
    FormMultilineTextField,
    FormSelectField,
    FormTextField,
    getMessage,
    LoadingMask,
    withI18N,
} from "@cyverse-de/ui-lib";
import { Field, FieldArray, Form, withFormik } from "formik";
import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    makeStyles,
    Paper,
    Toolbar,
    Typography,
} from "@material-ui/core";
import PropTypes from "prop-types";
import styles from "../styles";
import Members from "./MembersSection";
import { AllPermissions } from "./PermissionOptions";
import SendJoinTeamRequestDialog from "./SendJoinTeamRequestDialog";

const useStyles = makeStyles(styles);

function EditTeamDialog(props) {
    const {
        handleSubmit,
        open,
        loading,
        parentId,
        presenter,
        team,
        privileges,
        members,
        selfSubject,
        collaboratorsUtil,
        publicUsersId,
        groupNameRestrictedChars,
    } = props;
    const classes = useStyles();

    const [leaveDlgOpen, setLeaveDlgOpen] = useState(false);
    const [deleteDlgOpen, setDeleteDlgOpen] = useState(false);
    const [
        sendJoinTeamRequestDlgOpen,
        setSendJoinTeamRequestDlgOpen,
    ] = useState(false);
    const [
        sendJoinTeamRequestDlgLoading,
        setSendJoinTeamRequestDlgLoading,
    ] = useState(false);

    const selfPrivilegeArray = getUserPrivileges(selfSubject.id, privileges);
    const isAdmin = !team || selfPrivilegeArray.includes(Privilege.ADMIN.value);
    const isMember = subjectIsMember(selfSubject, members);
    const hasRead = isAdmin || (members !== null && members.length > 0);
    const teamShortName = team ? collaboratorsUtil.getGroupShortName(team) : "";

    const onJoinTeamSelected = () =>
        presenter.joinTeamSelected(team.name, () =>
            setSendJoinTeamRequestDlgOpen(true)
        );

    const onSendJoinTeamRequest = (message) => {
        setSendJoinTeamRequestDlgLoading(true);
        presenter.sendRequestToJoin(
            team.name,
            message,
            () => {
                setSendJoinTeamRequestDlgOpen(false);
                setSendJoinTeamRequestDlgLoading(false);
            },
            () => setSendJoinTeamRequestDlgLoading(false)
        );
    };

    const onPublicPrivsChange = (event, onChange) => {
        if (team === null) {
            onChange(event);
        } else {
            let updatedPrivilege = {
                name: event.target.value,
                subject: {
                    id: publicUsersId,
                },
            };
            presenter.updatePrivilegesToTeam(
                team.name,
                getUpdateRequest([updatedPrivilege]),
                null,
                () => onChange(event)
            );
        }
    };

    return (
        <Dialog open={open} fullWidth={true} maxWidth="lg" id={parentId}>
            <DEDialogHeader
                messages={messages.messages}
                heading={
                    team
                        ? getMessage("editTeam", {
                              values: {
                                  name: teamShortName,
                              },
                          })
                        : getMessage("createTeam")
                }
                onClose={() => presenter.closeEditTeamDlg()}
            />
            <DialogContent>
                {team && (
                    <Toolbar classes={{ root: classes.dialogToolbar }}>
                        {isMember && (
                            <Button
                                variant="contained"
                                id={build(parentId, ids.BUTTONS.LEAVE_TEAM)}
                                onClick={() => setLeaveDlgOpen(true)}
                            >
                                {getMessage("leaveTeamBtn")}
                            </Button>
                        )}
                        {!isMember && (
                            <Button
                                variant="contained"
                                id={build(parentId, ids.BUTTONS.JOIN_TEAM)}
                                onClick={onJoinTeamSelected}
                            >
                                {getMessage("joinTeamBtn")}
                            </Button>
                        )}
                        <SendJoinTeamRequestDialog
                            open={sendJoinTeamRequestDlgOpen}
                            loading={sendJoinTeamRequestDlgLoading}
                            teamName={teamShortName}
                            onSendRequest={onSendJoinTeamRequest}
                            onClose={() => setSendJoinTeamRequestDlgOpen(false)}
                        />
                        <div className={classes.grow} />
                        {isAdmin && (
                            <Button
                                variant="contained"
                                id={build(parentId, ids.BUTTONS.DELETE_TEAM)}
                                className={classes.deleteBtn}
                                onClick={() => setDeleteDlgOpen(true)}
                            >
                                {getMessage("deleteTeamBtn")}
                            </Button>
                        )}
                        <DEConfirmationDialog
                            dialogOpen={leaveDlgOpen}
                            debugId={ids.EDIT_TEAM.LEAVE_TEAM_DLG}
                            onOkBtnClick={() =>
                                presenter.leaveTeamSelected(team.name, () =>
                                    setLeaveDlgOpen(false)
                                )
                            }
                            okLabel={getMessage("leaveTeamBtn")}
                            onCancelBtnClick={() => setLeaveDlgOpen(false)}
                            heading={getMessage("leaveTeamHeading", {
                                values: { name: teamShortName },
                            })}
                            messages={messages.messages}
                            message={
                                <LoadingMask loading={loading}>
                                    <div>{getMessage("leaveTeam")}</div>
                                </LoadingMask>
                            }
                        />
                        <DEConfirmationDialog
                            dialogOpen={deleteDlgOpen}
                            debugId={ids.EDIT_TEAM.DELETE_TEAM_DLG}
                            onOkBtnClick={() =>
                                presenter.deleteTeamSelected(team.name, () =>
                                    setDeleteDlgOpen(false)
                                )
                            }
                            okLabel={getMessage("deleteTeamBtn")}
                            onCancelBtnClick={() => setDeleteDlgOpen(false)}
                            heading={getMessage("deleteTeamHeading", {
                                values: { name: teamShortName },
                            })}
                            messages={messages.messages}
                            message={
                                <LoadingMask loading={loading}>
                                    <div>{getMessage("deleteTeam")}</div>
                                </LoadingMask>
                            }
                        />
                    </Toolbar>
                )}
                <LoadingMask loading={loading}>
                    <Field
                        parentId={parentId}
                        selfId={selfSubject.id}
                        team={team}
                        collaboratorsUtil={collaboratorsUtil}
                        presenter={presenter}
                        isAdmin={isAdmin}
                        hasRead={hasRead}
                        groupNameRestrictedChars={groupNameRestrictedChars}
                        onPublicPrivsChange={onPublicPrivsChange}
                        component={EditTeamForm}
                    />
                </LoadingMask>
            </DialogContent>
            <DialogActions>
                <Button
                    variant="contained"
                    id={build(parentId, ids.BUTTONS.CANCEL)}
                    onClick={() => presenter.closeEditTeamDlg()}
                >
                    {getMessage("cancel")}
                </Button>
                {isAdmin && (
                    <Button
                        variant="contained"
                        id={build(parentId, ids.BUTTONS.SAVE)}
                        type="submit"
                        color="primary"
                        onClick={handleSubmit}
                    >
                        {getMessage("save")}
                    </Button>
                )}
            </DialogActions>
        </Dialog>
    );
}

function EditTeamForm(props) {
    const {
        parentId,
        selfId,
        team,
        presenter,
        isAdmin,
        hasRead,
        collaboratorsUtil,
        groupNameRestrictedChars,
        onPublicPrivsChange,
    } = props;
    const classes = useStyles();

    return (
        <Form>
            <Field
                name="name"
                label={getMessage("teamName")}
                id={build(parentId, ids.EDIT_TEAM.NAME)}
                InputProps={{
                    readOnly: !isAdmin,
                }}
                validate={(value) =>
                    validateGroupName(value, groupNameRestrictedChars)
                }
                component={FormTextField}
            />
            <Field
                name="description"
                label={getMessage("teamDesc")}
                id={build(parentId, ids.EDIT_TEAM.DESCRIPTION)}
                InputProps={{
                    readOnly: !isAdmin,
                }}
                component={FormMultilineTextField}
            />
            {hasRead && (
                <FieldArray
                    name="member_privileges"
                    render={(props) => (
                        <Members
                            {...props}
                            selfId={selfId}
                            isAdmin={isAdmin}
                            hasRead={hasRead}
                            parentId={parentId}
                            collaboratorsUtil={collaboratorsUtil}
                            presenter={presenter}
                            team={team}
                        />
                    )}
                />
            )}
            {isAdmin && (
                <Paper className={classes.paper}>
                    <Typography variant="subtitle1" gutterBottom={true}>
                        {getMessage("publicPrivileges")}
                    </Typography>
                    <Typography variant="body2" paragraph={true}>
                        {getMessage("publicPrivilegesMessage")}
                    </Typography>
                    <Field
                        name="public_privileges"
                        render={({
                            field: { onChange, ...field },
                            ...props
                        }) => {
                            return (
                                <FormSelectField
                                    {...props}
                                    field={field}
                                    label={getMessage("publicPrivileges")}
                                    id={build(
                                        parentId,
                                        ids.EDIT_TEAM.PUBLIC_PRIVILEGES
                                    )}
                                    inputProps={{ readOnly: !isAdmin }}
                                    onChange={(event) =>
                                        onPublicPrivsChange(event, onChange)
                                    }
                                >
                                    {AllPermissions(parentId)}
                                </FormSelectField>
                            );
                        }}
                    />
                </Paper>
            )}
        </Form>
    );
}

function validateGroupName(value, groupNameRestrictedChars) {
    if (!value || value.length < 1) {
        return "Empty value";
    }

    let restrictedRegex = new RegExp("[" + groupNameRestrictedChars + "]", "g");
    let invalid = value.match(restrictedRegex);
    if (invalid) {
        return (
            "Team name cannot contain `" +
            groupNameRestrictedChars +
            "`. Invalid chars: `" +
            invalid.join("") +
            "`"
        );
    }
}

/**
 * By default, when creating a team, the user creating the team
 * should be a member and have admin permissions on the team.
 * We're also encouraging users to keep teams publicly discoverable
 * by adding a VIEW privilege (which they can change at any time)
 */
const DEFAULT_TEAM = (selfSubject) => ({
    name: "",
    description: "",
    member_privileges: [
        { name: Privilege.ADMIN.value, subject: { ...selfSubject } },
    ],
    public_privileges: Privilege.VIEW.value,
});

function mapPropsToValues(props) {
    const {
        team,
        collaboratorsUtil,
        selfSubject,
        privileges,
        members,
        publicUsersId,
    } = props;

    if (!team) {
        return DEFAULT_TEAM(selfSubject);
    }

    let simplifiedPrivs = simplifyPrivileges(privileges);

    let public_privileges = getUserPrivileges(publicUsersId, simplifiedPrivs);
    let public_privilege =
        public_privileges && public_privileges.length > 0
            ? public_privileges[0]
            : "";

    let member_privileges = getMemberPrivileges(
        members,
        public_privilege,
        simplifiedPrivs
    );

    return {
        name: collaboratorsUtil.getGroupShortName(team),
        description: team.description,
        member_privileges: member_privileges,
        public_privileges: public_privilege,
    };
}

/**
 * When creating a team, all values need to be saved.
 * When editing an already existing team, all member and privilege modifications happen immediately as
 * the user is changing things.  On save, only any changes to the team name or description need to be
 * saved.
 * @param values
 * @param props
 */
const handleSubmit = (values, { props }) => {
    const {
        team: originalTeam,
        presenter,
        collaboratorsUtil,
        selfSubject,
    } = props;

    const { name, description, public_privileges, member_privileges } = values;

    // Save new team - save everything
    if (!originalTeam) {
        let publicPrivs = privilegeToPrivilegeList(public_privileges);
        let memberIds = member_privileges.map(
            (privilege) => privilege.subject.id
        );
        // do not let the user update their own privileges e.g. the owner cannot delete their own admin privs
        let filteredMembers = member_privileges.filter(
            (privilege) => privilege.subject.id !== selfSubject.id
        );

        let privilegeUpdateList = getUpdateRequest(filteredMembers);
        presenter.saveTeamSelected(
            name,
            {
                name: name,
                description: description,
                public_privileges: publicPrivs,
            },
            privilegeUpdateList,
            memberIds
        );
    } else {
        // Update team name or description if updated, otherwise, close the dialog
        if (
            collaboratorsUtil.getGroupShortName(originalTeam) !== name ||
            originalTeam.description !== description
        ) {
            presenter.updateTeam(originalTeam.name, name, description);
        } else {
            presenter.closeEditTeamDlg();
        }
    }
};

EditTeamDialog.propTypes = {
    parentId: PropTypes.string.isRequired,
    loading: PropTypes.bool.isRequired,
    open: PropTypes.bool.isRequired,
    presenter: PropTypes.shape({
        saveTeamSelected: PropTypes.func.isRequired,
        updateTeam: PropTypes.func.isRequired,
        deleteTeamSelected: PropTypes.func.isRequired,
        leaveTeamSelected: PropTypes.func.isRequired,
        joinTeamSelected: PropTypes.func.isRequired,
        closeEditTeamDlg: PropTypes.func.isRequired,
        updatePrivilegesToTeam: PropTypes.func.isRequired,
        removeMemberAndPrivilege: PropTypes.func.isRequired,
    }),
    collaboratorsUtil: PropTypes.object.isRequired,
    team: PropTypes.object,
    privileges: PropTypes.array,
    members: PropTypes.array,
    selfSubject: PropTypes.object.isRequired,
    publicUsersId: PropTypes.string.isRequired,
};

export default withFormik({
    enableReinitialize: true,
    mapPropsToValues,
    handleSubmit,
})(withI18N(EditTeamDialog, messages));
