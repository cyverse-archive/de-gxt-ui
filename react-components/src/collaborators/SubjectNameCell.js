import React from "react";
import PropTypes from "prop-types";

import TeamIcon from "./TeamIcon";
import CollaboratorListIcon from "./CollaboratorListIcon";

function SubjectNameCell(props) {
    const { collaboratorsUtil, subject } = props;

    return (
        <>
            {collaboratorsUtil.isTeam(subject) && <TeamIcon />}
            {collaboratorsUtil.isCollaboratorList(subject) && (
                <CollaboratorListIcon />
            )}
            {collaboratorsUtil.getSubjectDisplayName(subject)}
        </>
    );
}

SubjectNameCell.propTypes = {
    collaboratorsUtil: PropTypes.object.isRequired,
    subject: PropTypes.object.isRequired,
};

export default SubjectNameCell;
