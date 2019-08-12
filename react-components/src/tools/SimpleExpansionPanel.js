import React, { useState } from "react";

import ids from "./ids";
import styles from "./styles";

import { build } from "@cyverse-de/ui-lib";

import {
    ExpansionPanel,
    ExpansionPanelDetails,
    ExpansionPanelSummary,
    Typography,
    withStyles,
} from "@material-ui/core";
import { ExpandMore } from "@material-ui/icons";
import PropTypes from "prop-types";

function SimpleExpansionPanel(props) {
    const {
        header,
        parentId,
        defaultExpanded,
        children,
        classes,
        hasErrors,
    } = props;
    const [expanded, setExpanded] = useState(defaultExpanded);

    const handleChange = (event, isExpanded) => {
        setExpanded(!!(isExpanded || hasErrors));
    };

    return (
        <ExpansionPanel
            defaultExpanded={defaultExpanded}
            expanded={expanded || hasErrors}
            onChange={handleChange}
        >
            <ExpansionPanelSummary
                expandIcon={
                    <ExpandMore id={build(parentId, ids.BUTTONS.EXPAND)} />
                }
            >
                <Typography variant="body1">{header}</Typography>
            </ExpansionPanelSummary>
            <ExpansionPanelDetails classes={{ root: classes.expansionDetails }}>
                <div>{children}</div>
            </ExpansionPanelDetails>
        </ExpansionPanel>
    );
}

SimpleExpansionPanel.defaultProps = {
    defaultExpanded: true,
};

SimpleExpansionPanel.propTypes = {
    header: PropTypes.any.isRequired,
    parentId: PropTypes.string.isRequired,
    defaultExpanded: PropTypes.bool,
    hasErrors: PropTypes.bool,
    children: PropTypes.any.isRequired,
};

export default withStyles(styles)(SimpleExpansionPanel);
