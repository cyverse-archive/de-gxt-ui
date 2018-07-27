import Autocomplete from "../util/Autocomplete";
import messages from "./messages";
import styles from "./styles";
import withI18N, { getMessage } from "../util/I18NWrapper";

import Divider from "@material-ui/core/Divider";
import Highlight from "react-highlighter";
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
                    <Typography variant='headline' component='h3'>
                        <Highlight search={inputValue}>{option.name}</Highlight>
                    </Typography>
                    {getSecondaryText(inputValue, option)}
                </Paper>
                <Divider/>
            </Fragment>
        );
    }
}

function getSecondaryText(inputValue, option) {
    return (
        <Fragment>
            <Typography component="p">
                <i>
                    <Highlight search={inputValue}>
                        {option.institution ? option.institution : option.description}
                    </Highlight>
                </i>
            </Typography>
            {
                option.id.search(inputValue) > -1 &&
                <Typography component="p">
                    Username:{' '}
                    <Highlight search={inputValue}>
                        {option.id}
                    </Highlight>
                </Typography>
            }
            {
                option.email && option.email.search(inputValue) > -1 &&
                <Typography component="p">
                    Email:{' '}
                    <Highlight search={inputValue}>
                        {option.email}
                    </Highlight>
                </Typography>
            }
        </Fragment>
    );
}

SubjectSearchField.propTypes = {
    onSelect: PropTypes.func.isRequired,
    presenter: PropTypes.shape({
        searchCollaborators: PropTypes.func.isRequired
    })
};

export default withI18N(SubjectSearchField, messages);