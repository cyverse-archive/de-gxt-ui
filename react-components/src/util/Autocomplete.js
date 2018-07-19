import MenuItem from "@material-ui/core/MenuItem";
import PropTypes from "prop-types";
import React, { Component } from "react";
import Select from "react-select";
import { withStyles } from "@material-ui/core/styles";

/**
 * @author aramsey
 * Renders the Select component from react-select with some stylings for material-ui
 */

// Copied from https://material-ui.com/demos/autocomplete/
const styles = theme => ({
    // We had to use a lot of global selectors in order to style react-select.
    // We are waiting on https://github.com/JedWatson/react-select/issues/1679
    // to provide a much better implementation.
    // Also, we had to reset the default style injected by the library.
    '@global': {
        '.Select-input input': {
            cursor: 'default',
            display: 'block',
            fontFamily: 'inherit',
            fontSize: 'inherit',
            margin: 0,
            outline: 0,
            width: '100% !important',
        },
        '.Select-menu-outer': {
            backgroundColor: theme.palette.background.paper,
            boxShadow: theme.shadows[2],
            width: '100%',
            zIndex: 2,
            maxHeight: 125,
        },
        '.Select-menu': {
            maxHeight: 125,
            overflowY: 'auto',
        },

    }
});

function Autocomplete(props) {
    let {
        variant,
        ...custom
    } = props;

    let SelectComponent = null;

    switch (variant) {
        case 'creatable':
            SelectComponent = Select.Creatable;
            break;
        case 'async':
            SelectComponent = Select.Async;
            break;
        case 'asyncCreatable':
            SelectComponent = Select.AsyncCreatable;
            break;
        default:
            SelectComponent = Select;
            break;
    }

    return (
        <SelectComponent optionComponent={Option}
                   {...custom} />
    )
}

class Option extends Component {
    handleClick = event => {
        this.props.onSelect(this.props.option, event);
    };

    render() {
        const {children} = this.props;

        return (
            <MenuItem onClick={this.handleClick}>
                {children}
            </MenuItem>
        );
    }
}

Autocomplete.propTypes = {
    onInputChange: PropTypes.func, // when the input value updates
    onChange: PropTypes.func, // when an option is selected
    options: PropTypes.array, // array of options/suggestions
    labelKey: PropTypes.string, // label for each option
    valueKey: PropTypes.string, // value for each option
    placeholder: PropTypes.any, 
    variant: PropTypes.oneOf([
        'creatable',
        'async',
        'asyncCreatable',
        'default'
    ]).isRequired
};

Autocomplete.defaultProps = {
    variant: 'default'
};

export default withStyles(styles)(Autocomplete);