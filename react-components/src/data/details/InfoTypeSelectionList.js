/**
 * @author sriram
 *
 */
import React, { Component } from "react";
import styles from "../style";
import intlData from "../messages";
import { build, getMessage, withI18N } from "@cyverse-de/ui-lib";

import {
    withStyles,
    FormControl,
    MenuItem,
    Select,
    InputLabel,
} from "@material-ui/core";

class InfoTypeSelectionList extends Component {
    handleChange = (event) => {
        this.props.onInfoTypeSelect(event.target.value);
    };

    render() {
        const items = [];
        for (let i = 0; i < this.props.infoTypes.length; i++) {
            items.push(
                <MenuItem
                    value={this.props.infoTypes[i]}
                    key={this.props.infoTypes[i]}
                >
                    <span id={build(this.props.id, this.props.infoTypes[i])}>
                        {this.props.infoTypes[i]}
                    </span>
                </MenuItem>
            );
        }
        const menuProps = {
            PaperProps: {
                style: {
                    maxHeight: 200,
                    width: 250,
                },
            },
        };
        const classes = this.props.classes;
        return (
            <FormControl>
                <InputLabel>{getMessage("selectInfoType")}</InputLabel>
                <Select
                    value={this.props.selectedValue}
                    onChange={this.handleChange}
                    className={classes.infoTypeStyle}
                    MenuProps={menuProps}
                >
                    {items}
                </Select>
            </FormControl>
        );
    }
}

export default withStyles(styles)(withI18N(InfoTypeSelectionList, intlData));
