/**
 * @author psarando
 */
import React, { Component } from 'react';

import { getMessage } from "../util/I18NWrapper";
import { FormikSearchField } from "../util/FormField";

import ListItemText from '@material-ui/core/ListItemText';
import MenuItem from "@material-ui/core/MenuItem";

const customStyles = {
    menu: base => ({
        ...base,
        position: "relative",
        zIndex: 888888,
    }),
};

const AstroThesaurusOption = ({
    innerRef,
    isFocused,
    innerProps,
    data,
}) => (
    <MenuItem
        buttonRef={innerRef}
        selected={isFocused}
        {...innerProps}
    >
        <ListItemText primary={data.label} secondary={data.iri} />
    </MenuItem>
);

class AstroThesaurusSearchField extends Component {
    constructor(props) {
        super(props);

        this.loadOptions = this.loadOptions.bind(this);
    }

    loadOptions(inputValue, callback) {
        this.props.presenter.searchAstroThesaurusTerms(inputValue, callback);
    }

    formatCreateLabel(inputValue) {
        return getMessage("formatMetadataTermFreeTextOption", {values: { inputValue } });
    }

    render() {
        const { presenter, ...props } = this.props;

        return (
            <FormikSearchField loadOptions={this.loadOptions}
                               variant="asyncCreatable"
                               labelKey="label"
                               valueKey="label"
                               CustomOption={AstroThesaurusOption}
                               formatCreateLabel={this.formatCreateLabel}
                               styles={customStyles}
                               {...props}
            />
        );
    }
}

export default AstroThesaurusSearchField;
