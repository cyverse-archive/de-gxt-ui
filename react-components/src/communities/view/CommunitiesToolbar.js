import build from "../../util/DebugIDUtil";
import CreateCommunitiesBtn from "./CreateCommunitiesBtn";
import ids from "../ids";
import messages from "../messages";
import styles from "../styles";
import withI18N, { getMessage } from "../../util/I18NWrapper";

import MenuItem from "@material-ui/core/MenuItem";
import PropTypes from "prop-types";
import React from "react";
import Select from "@material-ui/core/Select";
import Toolbar from "@material-ui/core/Toolbar";
import { withStyles } from "@material-ui/core/styles";

/**
 * @author aramsey
 *
 * A toolbar that is shown in the CommunitiesView
 */
function CommunitiesToolbar(props) {
    const {
        parentId,
        currentCommunityType,
        handleCommunityFilterChange,
        onCreateCommunityClicked,
        classes,
    } = props;

    let toolbarId = build(parentId, ids.TOOLBAR);
    return (
        <Toolbar id={toolbarId}>
            <CreateCommunitiesBtn parentId={toolbarId}
                                  className={classes.toolbarItem}
                                  onClick={onCreateCommunityClicked}/>
            <Select value={currentCommunityType}
                    onChange={handleCommunityFilterChange}>
                <MenuItem value={"MyCommunities"}>{getMessage('myCommunities')}</MenuItem>
                <MenuItem value={"AllCommunities"}>{getMessage('allCommunities')}</MenuItem>
            </Select>
        </Toolbar>
    )
}

CommunitiesToolbar.propTypes = {
    parentId: PropTypes.string.isRequired,
    currentCommunityType: PropTypes.string.isRequired,
    handleCommunityFilterChange: PropTypes.func.isRequired,
    onCreateCommunityClicked: PropTypes.func.isRequired,
};

export default withStyles(styles)(withI18N(CommunitiesToolbar, messages));