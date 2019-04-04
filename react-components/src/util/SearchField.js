import styles from "./style";

import injectSheet from "react-jss";
import InputAdornment from "@material-ui/core/InputAdornment";
import PropTypes from "prop-types";
import React, { Component } from "react";
import Search from "@material-ui/icons/Search";
import TextField from "@material-ui/core/TextField";
import CyVersePalette from "./CyVersePalette";

/**
 * @author aramsey
 * A search field with customizable search timer and icons (adornments)
 */
class SearchField extends Component {
    constructor(props) {
        super(props);

        this.state = {
            inputValue: this.props.value,
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

    componentDidUpdate(prevProps) {
        if (this.props.value !== prevProps.value) {
            this.setState({ inputValue: this.props.value });
        }
    }

    handleChange(event) {
        const inputValue = event.target.value;
        if (!inputValue) {
            //handlekeypress not called on backspace. So when the search field is cleared,
            //parent components should be notified.
            this.setState({ inputValue: inputValue }, this.handleSearch);
        } else {
            this.setState({ inputValue });
        }
    }

    handleKeyPress(event) {
        clearTimeout(this.keyPressTimer);
        if (event.key === "Enter") {
            this.handleSearch();
            event.preventDefault();
        } else {
            this.keyPressTimer = setTimeout(
                this.handleSearch,
                this.props.keyPressTimer
            );
        }
    }

    handleSearch() {
        this.props.handleSearch(this.state.inputValue);
    }

    handleShow(event) {
        this.setState({ anchorEl: event.currentTarget });
    }

    handleHide() {
        this.setState({ anchorEl: null });
    }

    render() {
        let { inputValue } = this.state;
        let {
            id,
            label,
            placeholder,
            startAdornment,
            endAdornment,
            children,
            classes,
            height,
        } = this.props;

        return (
            <div>
                <TextField
                    id={id}
                    style={{ height: height, flexDirection: "unset" }}
                    variant="outlined"
                    label={label}
                    placeholder={placeholder}
                    value={inputValue}
                    onKeyPress={this.handleKeyPress}
                    onChange={this.handleChange}
                    InputProps={{
                        disableUnderline: true,
                        startAdornment: startAdornment,
                        endAdornment: endAdornment,
                        className: classes.searchInput,
                    }}
                />
                {children}
            </div>
        );
    }
}

SearchField.propTypes = {
    id: PropTypes.any,
    height: PropTypes.number,
    handleSearch: PropTypes.func.isRequired,
    label: PropTypes.any,
    placeholder: PropTypes.any,
    startAdornment: PropTypes.object,
    endAdornment: PropTypes.object,
    keyPressTimer: PropTypes.number,
    value: PropTypes.string,
};

SearchField.defaultProps = {
    label: "",
    height: 38,
    helperText: "",
    keyPressTimer: 1000,
    startAdornment: (
        <InputAdornment position="start">
            <Search style={{ color: CyVersePalette.darkBlue }} />
        </InputAdornment>
    ),
    endAdornment: null,
};

export default injectSheet(styles)(SearchField);
