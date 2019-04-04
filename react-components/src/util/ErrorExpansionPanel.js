/**
 * @author aramsey
 */
import messages from "./messages";
import styles from "./style";
import withI18N, { getMessage } from "./I18NWrapper";

import ExpandMoreIcon from "@material-ui/icons/ExpandMore";
import ExpansionPanel from "@material-ui/core/ExpansionPanel";
import ExpansionPanelSummary from "@material-ui/core/ExpansionPanelSummary";
import ExpansionPanelDetails from "@material-ui/core/ExpansionPanelDetails";
import PropTypes from "prop-types";
import React from "react";
import Typography from "@material-ui/core/Typography";
import { withStyles } from "@material-ui/core/styles";

function ErrorExpansionPanel(props) {
    const { errMsg, username, userAgent, date, host, classes } = props;

    return (
        <ExpansionPanel defaultExpanded>
            <ExpansionPanelSummary
                expandIcon={<ExpandMoreIcon />}
                classes={{ root: classes.expansionPanelHeader }}
            >
                {getMessage("errorDetailsTitle")}
            </ExpansionPanelSummary>
            <ExpansionPanelDetails classes={{ root: classes.expansionPanel }}>
                <Typography classes={{ root: classes.errorMessage }}>
                    {errMsg.replace("<br>", "\n")}
                </Typography>
                <Typography>
                    {getMessage("username")}: {username}
                </Typography>
                <Typography>
                    {getMessage("userAgent")}: {userAgent}
                </Typography>
                <Typography>
                    {getMessage("date")}: {date}
                </Typography>
                <Typography>
                    {getMessage("host")}: {host}
                </Typography>
                <Typography classes={{ root: classes.supportDetails }}>
                    {getMessage("includeSupportDetails")}
                </Typography>
            </ExpansionPanelDetails>
        </ExpansionPanel>
    );
}

ErrorExpansionPanel.propTypes = {
    errMsg: PropTypes.string.isRequired,
    username: PropTypes.string.isRequired,
    userAgent: PropTypes.string.isRequired,
    date: PropTypes.string.isRequired,
    host: PropTypes.string.isRequired,
};

export default withStyles(styles)(withI18N(ErrorExpansionPanel, messages));
