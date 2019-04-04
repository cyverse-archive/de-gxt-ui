import Autocomplete from "../util/Autocomplete";
import build from "../util/DebugIDUtil";
import hasProps from "../util/hasProps";
import ids from "./ids";
import messages from "./messages";
import SubjectSearchMenuItem from "./SubjectSearchMenuItem";
import styles from "./styles";
import withI18N, { getMessage } from "../util/I18NWrapper";

import PropTypes from "prop-types";
import React, { Component } from "react";
import { withStyles } from "@material-ui/core/styles";

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
        const { onSelect, parentId, collaboratorsUtil } = this.props;

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
