import styles from "./style";

import injectSheet from "react-jss";
import InputAdornment from "@material-ui/core/InputAdornment";
import PropTypes from "prop-types";
import React, { Component } from "react";
import Search from "@material-ui/icons/Search";
import TextField from "@material-ui/core/TextField";

/**
 * @author aramsey
 * A search field with customizable search timer and icons (adornments)
 */
class SearchField extends Component {
    constructor(props) {
        super(props);

        this.state = {
            inputValue: ''
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleShow = this.handleShow.bind(this);
        this.handleHide = this.handleHide.bind(this);
        this.handleKeyPress = this.handleKeyPress.bind(this);
        this.handleSearch = this.handleSearch.bind(this);
    }

    componentWillMount() {
        this.keyPressTimer = null;
    }

    handleChange(event) {
        const inputValue = event.target.value;
        if (inputValue === "") {
            //handlekeypress on called on backspace. So when the search is field is cleared,
            //parent components should be notified.
            this.setState({inputValue: inputValue}, this.handleSearch);
        } else {
            this.setState({inputValue: inputValue});
        }

    }

    handleKeyPress(event) {
        clearTimeout(this.keyPressTimer);
        if (event.key === 'Enter') {
            this.handleSearch();
            event.preventDefault();
        } else {
            this.keyPressTimer = setTimeout(this.handleSearch, this.props.keyPressTimer)
        }
    }

    handleSearch() {
        this.props.handleSearch(this.state.inputValue);
    }

    handleShow(event) {
        this.setState({anchorEl: event.currentTarget});
    }

    handleHide() {
        this.setState({anchorEl: null});
    }

    render() {
        let { inputValue } = this.state;
        let {
            label,
            startAdornment,
            endAdornment,
            children,
            classes
        } = this.props;

        return (
            <div>
                <TextField
                    label={label}
                    value={inputValue}
                    onKeyPress={this.handleKeyPress}
                    onChange={this.handleChange}
                    InputProps={{
                        disableUnderline: true,
                        startAdornment: startAdornment,
                        endAdornment: endAdornment,
                        className: classes.searchInput
                    }}
                >
                </TextField>
                {children}
            </div>
        )
    }
}

SearchField.propTypes = {
    handleSearch: PropTypes.func.isRequired,
    label: PropTypes.any,
    helperText: PropTypes.any,
    startAdornment: PropTypes.object,
    endAdornment: PropTypes.object,
    keyPressTimer: PropTypes.number
};

SearchField.defaultProps = {
    label: '',
    helperText: "",
    keyPressTimer: 1000,
    startAdornment: <InputAdornment position='start'><Search/></InputAdornment>,
    endAdornment: null
};

export default injectSheet(styles)(SearchField);