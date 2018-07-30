import Autocomplete from "../util/Autocomplete";
import getRegExp from "../util/getRegExp";
import Highlighter from "../util/Highlighter";
import messages from "./messages";
import styles from "./styles";
import withI18N, { getMessage } from "../util/I18NWrapper";

import Divider from "@material-ui/core/Divider";
import Paper from "@material-ui/core/Paper";
import PropTypes from "prop-types";
import React, { Component, Fragment } from 'react';
import Typography from "@material-ui/core/Typography";
import { withStyles } from '@material-ui/core/styles';

/**
 * A component that allows users to search for Collaborators (individuals, teams, collaboration
 * lists)
 */
class SubjectSearchField extends Component {

    constructor(props) {
        super(props);

        [
            'getSubjects'
        ].forEach((fn) => this[fn] = this[fn].bind(this));
    }

    getSubjects(input, callback) {
        if (input.length > 2) {
            this.props.presenter.searchCollaborators(input, (data) => {
                callback(null, {options: data})
            });
        } else {
            callback(null, null);
        }
    }

    render() {
        let {
            onSelect
        } = this.props;

        let CustomOption = withStyles(styles)(Option);

        return (
            <Autocomplete variant='async'
                          optionComponent={CustomOption}
                          placeholder={getMessage("searchHelpText")}
                          filterOptions={(options) => {
                              // Do no filtering, just return all options
                              return options;
                          }}
                          loadOptions={this.getSubjects}
                          onChange={onSelect}/>
        )
    }
}

class Option extends React.Component {
    handleClick = event => {
        this.props.onSelect(this.props.option, event);
    };

    render() {
        let {
            option,
            isFocused,
            onFocus,
            inputValue,
            classes
        } = this.props;

        return (
            <Fragment>
                <Paper className={classes.searchField}
                       onFocus={onFocus}
                       selected={isFocused}
                       onClick={this.handleClick}
                       component="div"
                       elevation={1}>
                    {getOptionBody(inputValue, option)}
                </Paper>
                <Divider/>
            </Fragment>
        );
    }
}

function getOptionBody(searchTerm, option) {
    let {trimmedSearch, pattern, regex} = getRegexSearchTerm(searchTerm);
    return (
        <Fragment>
            <Typography component="p">
                <b>
                    <Highlighter search={regex}>{option.name}</Highlighter>
                </b>
            </Typography>

            <Typography component="p">
                <i>
                    <Highlighter search={regex}>
                        {option.institution ? option.institution : option.description}
                    </Highlighter>
                </i>
            </Typography>
            {
                option.id.search(regex) > -1 &&
                <Typography component="p">
                    Username:{' '}
                    <Highlighter search={regex}>
                        {censorUsername(trimmedSearch)}
                    </Highlighter>
                </Typography>
            }
            {
                option.email && option.email.search(regex) > -1 &&
                <Typography component="p">
                    Email:{' '}
                    <Highlighter search={regex}>
                        {censorEmail(option.email, pattern, regex)}
                    </Highlighter>
                </Typography>
            }
        </Fragment>
    );
}

function getRegexSearchTerm(searchTerm) {
    //remove starting or ending * and duplicated *s
    let trimmedSearch = searchTerm.replace(/^\*+|\*+$/g, '').replace(/\*+/, '*');
    let searchGroups = trimmedSearch.split('*');
    let groupedRegexStr = '(' + searchGroups.join(')(.*?)(') + ')';
    let regex = getRegExp(groupedRegexStr, 'i');

    return {
        trimmedSearch: trimmedSearch,
        pattern: groupedRegexStr,
        regex: regex
    };
}

function censorUsername(searchTerm) {
    let array = searchTerm.split("*");
    return "***" + array.join("***") + "***";
}

function censorEmail(email, pattern) {
    let domainIndex = email.search(/@(?!.*@)/);
    let groupedRegex = getRegExp('(.*?)' + pattern, 'ig');
    let matches = groupedRegex.exec(email);

    let newEmail = '';
    let startPos = 0;
    let endPos = 0;
    for (let i = 1; i < matches.length; i++) {
        let match = matches[i];
        startPos = endPos;
        endPos += match.length;
        if (startPos > domainIndex || endPos > domainIndex) {
            newEmail += isSecret(i) ? '***' + email.slice(domainIndex) : email.slice(startPos);
            break;
        } else {
            newEmail += isSecret(i) ? '***' : match;
        }
    }
    if (endPos <= domainIndex) {
        newEmail += '***' + email.slice(domainIndex);
    }
    return newEmail;
}

function isSecret(index) {
    return index % 2;
}

SubjectSearchField.propTypes = {
    onSelect: PropTypes.func.isRequired,
    presenter: PropTypes.shape({
        searchCollaborators: PropTypes.func.isRequired
    })
};

export default withI18N(SubjectSearchField, messages);