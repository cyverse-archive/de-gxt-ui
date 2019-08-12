import React from "react";
import PropTypes from "prop-types";
import ids from "../ids";
import messages from "../messages";
import { build, getMessage, withI18N } from "@cyverse-de/ui-lib";

import AddIcon from "@material-ui/icons/Add";
import { Button } from "@material-ui/core";

/**
 * @author aramsey
 * A simple button that allows users to create communities
 */
function CreateCommunitiesBtn(props) {
    const { parentId, ...custom } = props;
    return (
        <div>
            <Button
                variant="contained"
                id={build(parentId, ids.BUTTONS.CREATE)}
                {...custom}
            >
                <AddIcon />
                {getMessage("createCommunity")}
            </Button>
        </div>
    );
}

CreateCommunitiesBtn.propTypes = {
    parentId: PropTypes.string.isRequired,
};

export default withI18N(CreateCommunitiesBtn, messages);
