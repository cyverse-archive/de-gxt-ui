import React, { Component } from "react";
import PropTypes from "prop-types";
import ids from "./ids";
import messages from "./messages";
import SubjectSearchMenuItem from "./SubjectSearchMenuItem";
import styles from "./styles";
import {
    Autocomplete,
    build,
    getMessage,
    hasProps,
    withI18N,
} from "@cyverse-de/ui-lib";

import { withStyles } from "@material-ui/core";

/**
 * A component that allows users to search for Collaborators (individuals, teams, collaboration
 * lists)
 */
class SubjectSearchField extends Component {
    constructor(props) {
        super(props);

        ["getSubjects"].forEach((fn) => (this[fn] = this[fn].bind(this)));
    }

    getSubjects(input, callback) {
        if (input.length > 2) {
            new Promise((resolve, reject) => {
                this.props.presenter.searchCollaborators(
                    input,
                    resolve,
                    reject
                );
            })
                .then((listing) => {
                    callback(listing);
                })
                .catch(() => {
                    callback(null);
                });
        } else {
            callback(null);
        }
    }

    render() {
        const { onSelect, parentId, collaboratorsUtil, onBlur } = this.props;

        return (
            <div id={build(parentId, ids.subjectSearchField)}>
                <Autocomplete
                    variant="async"
                    valueKey="id"
                    labelKey="displayName"
                    CustomOption={hasProps(SubjectSearchMenuItem, {
                        collaboratorsUtil: collaboratorsUtil,
                    })}
                    placeholder={getMessage("searchHelpText")}
                    filterOption={() => {
                        // Do no filtering, just return all options
                        return true;
                    }}
                    loadOptions={this.getSubjects}
                    collaboratorsUtil={collaboratorsUtil}
                    onChange={onSelect}
                    onBlur={onBlur}
                />
            </div>
        );
    }
}

SubjectSearchField.propTypes = {
    parentId: PropTypes.string.isRequired,
    onSelect: PropTypes.func.isRequired,
    presenter: PropTypes.shape({
        searchCollaborators: PropTypes.func.isRequired,
    }),
    collaboratorsUtil: PropTypes.shape({
        isTeam: PropTypes.func,
        isCollaboratorList: PropTypes.func,
        getSubjectDisplayName: PropTypes.func,
    }),
};

export default withStyles(styles)(withI18N(SubjectSearchField, messages));
