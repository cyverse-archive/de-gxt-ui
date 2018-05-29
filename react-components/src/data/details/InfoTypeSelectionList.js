/**
 * @author sriram
 *
 */
import React, {Component} from "react";
import MenuItem from "@material-ui/core/MenuItem";
import Select from "@material-ui/core/Select";
import InputLabel from "@material-ui/core/InputLabel";
import {css} from "aphrodite";
import styles from "../style";
import withI18N, {getMessage} from "../../util/I18NWrapper";
import intlData from "../messages";
import build from "../../util/DebugIDUtil";
import FormControl from "@material-ui/core/FormControl";


class InfoTypeSelectionList extends Component {
    handleChange = (event) => {
        this.props.onInfoTypeSelect(event.target.value);
   };

    render() {
        const items = [];
        for (let i = 0; i < this.props.infoTypes.length; i++) {
            items.push(<MenuItem value={this.props.infoTypes[i]}
                                 key={this.props.infoTypes[i]}><span
                id={build(this.props.id, this.props.infoTypes[i])}>{this.props.infoTypes[i]}</span></MenuItem>);
        }
        const menuProps = {
            PaperProps: {
                style: {
                    maxHeight: 200,
                    width: 250,
                },
            },
        };
        return (
            <FormControl>
                <InputLabel>{getMessage("selectInfoType")}</InputLabel>
                <Select
                    value={this.props.selectedValue}
                    onChange={this.handleChange}
                    className={css(styles.infoTypeStyle)}
                    MenuProps={menuProps}>
                    {items}
                </Select>
            </FormControl>
        );
    }

}

export default withI18N(InfoTypeSelectionList, intlData);
