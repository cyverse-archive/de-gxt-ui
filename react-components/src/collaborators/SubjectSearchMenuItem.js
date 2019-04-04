import CollaboratorListIcon from "./CollaboratorListIcon";
import getRegExp from "../util/getRegExp";
import Highlighter from "../util/Highlighter";
import styles from "./styles";
import TeamIcon from "./TeamIcon";

import Divider from "@material-ui/core/Divider";
import Paper from "@material-ui/core/Paper";
import React, { Component, Fragment } from "react";
import Typography from "@material-ui/core/Typography";
import { withStyles } from "@material-ui/core/styles";

/**
 * A custom menu item used for displaying Subjects in SubjectSearchField
 */
class SubjectSearchMenuItem extends Component {
    render() {
        const {
            data,
            innerProps,
            innerRef,
            selectProps: { inputValue },
            classes,
            collaboratorsUtil,
        } = this.props;

        return (
            <Fragment>
                <Paper
                    className={classes.searchField}
                    innerRef={innerRef}
                    component="div"
                    elevation={1}
                    {...innerProps}
                >
                    <OptionBody
                        collaboratorsUtil={collaboratorsUtil}
                        searchTerm={inputValue}
                        option={data}
                    />
                </Paper>
                <Divider />
            </Fragment>
        );
    }
}

function OptionBody(props) {
    const { collaboratorsUtil, searchTerm, option } = props;
    let name = collaboratorsUtil.getSubjectDisplayName(option);
    let { trimmedSearch, pattern, regex } = getRegexSearchTerm(searchTerm);
    return (
        <Fragment>
            <Typography component="p">
                {collaboratorsUtil.isTeam(option) && <TeamIcon />}
                {collaboratorsUtil.isCollaboratorList(option) && (
                    <CollaboratorListIcon />
                )}
                <b>
                    <Highlighter search={regex}>{name}</Highlighter>
                </b>
            </Typography>

            <Typography component="p">
                <i>
                    <Highlighter search={regex}>
                        {option.institution
                            ? option.institution
                            : option.description}
                    </Highlighter>
                </i>
            </Typography>
            {option.id.search(regex) > -1 && (
                <Typography component="p">
                    Username:{" "}
                    <Highlighter search={regex}>
                        {censorUsername(trimmedSearch)}
                    </Highlighter>
                </Typography>
            )}
            {option.email && option.email.search(regex) > -1 && (
                <Typography component="p">
                    Email:{" "}
                    <Highlighter search={regex}>
                        {censorEmail(option.email, pattern, regex)}
                    </Highlighter>
                </Typography>
            )}
        </Fragment>
    );
}

function getRegexSearchTerm(searchTerm) {
    //remove starting or ending * and duplicated *s
    let trimmedSearch = searchTerm
        .replace(/^\*+|\*+$/g, "")
        .replace(/\*+/, "*");
    let searchGroups = trimmedSearch.split("*");
    let groupedRegexStr = "(" + searchGroups.join(")(.*?)(") + ")";
    let regex = getRegExp(groupedRegexStr, "i");

    return {
        trimmedSearch: trimmedSearch,
        pattern: groupedRegexStr,
        regex: regex,
    };
}

function censorUsername(searchTerm) {
    let array = searchTerm.split("*");
    return "***" + array.join("***") + "***";
}

function censorEmail(email, pattern) {
    let domainIndex = email.search(/@(?!.*@)/);
    let groupedRegex = getRegExp("(.*?)" + pattern, "ig");
    let matches = groupedRegex.exec(email);

    let newEmail = "";
    let startPos = 0;
    let endPos = 0;
    for (let i = 1; i < matches.length; i++) {
        let match = matches[i];
        startPos = endPos;
        endPos += match.length;
        if (startPos > domainIndex || endPos > domainIndex) {
            newEmail += isSecret(i)
                ? "***" + email.slice(domainIndex)
                : email.slice(startPos);
            break;
        } else {
            newEmail += isSecret(i) ? "***" : match;
        }
    }
    if (endPos <= domainIndex) {
        newEmail += "***" + email.slice(domainIndex);
    }
    return newEmail;
}

function isSecret(index) {
    return index % 2;
}

export default withStyles(styles)(SubjectSearchMenuItem);
