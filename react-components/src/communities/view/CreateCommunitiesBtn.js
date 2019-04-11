import build from "../../util/DebugIDUtil";
import withI18N, { getMessage } from "../../util/I18NWrapper";
import ids from "../ids";
import messages from "../messages";

import AddIcon from "@material-ui/icons/Add";
import Button from "@material-ui/core/Button/Button";
import PropTypes from "prop-types";
import React from "react";

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
