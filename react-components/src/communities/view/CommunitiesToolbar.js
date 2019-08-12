import React from "react";
import PropTypes from "prop-types";

import CreateCommunitiesBtn from "./CreateCommunitiesBtn";
import CommunityFilter from "./CommunityFilter";
import ids from "../ids";
import messages from "../messages";
import styles from "../styles";
import { build, getMessage, withI18N } from "@cyverse-de/ui-lib";

import { MenuItem, Select, Toolbar, withStyles } from "@material-ui/core";

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
        <Toolbar id={toolbarId} classes={{ root: classes.toolbar }}>
            <CreateCommunitiesBtn
                parentId={toolbarId}
                className={classes.toolbarItem}
                onClick={onCreateCommunityClicked}
            />
            <Select
                value={currentCommunityType}
                onChange={handleCommunityFilterChange}
            >
                <MenuItem value={CommunityFilter.MY_COMMUNITIES}>
                    {getMessage("myCommunities")}
                </MenuItem>
                <MenuItem value={CommunityFilter.ALL_COMMUNITIES}>
                    {getMessage("allCommunities")}
                </MenuItem>
            </Select>
        </Toolbar>
    );
}

CommunitiesToolbar.propTypes = {
    parentId: PropTypes.string.isRequired,
    currentCommunityType: PropTypes.string.isRequired,
    handleCommunityFilterChange: PropTypes.func.isRequired,
    onCreateCommunityClicked: PropTypes.func.isRequired,
};

export default withStyles(styles)(withI18N(CommunitiesToolbar, messages));
