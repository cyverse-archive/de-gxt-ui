/**
 * @author sriram
 *
 */
import React, { Component } from "react";
import intlData from "../messages";
import style from "../style";
import { build, getMessage, Highlighter, withI18N } from "@cyverse-de/ui-lib";

import ExpandMoreIcon from "@material-ui/icons/ExpandMore";

import {
    withStyles,
    ExpansionPanel,
    ExpansionPanelSummary,
    ExpansionPanelDetails,
    Typography,
} from "@material-ui/core";

class ToolDetailsV1 extends Component {
    constructor(props) {
        super(props);

        this.state = {
            selectedToolIndex: 0,
        };

        // This binding is necessary to make `this` work in the callback
        this.onToolSelectionChange = this.onToolSelectionChange.bind(this);
    }

    onToolSelectionChange(event, selectedToolIndex) {
        this.setState({
            selectedToolIndex: selectedToolIndex,
        });
    }

    render() {
        const classes = this.props.classes;
        let tools = this.props.details,
            labelClass = classes.detailsLabel,
            valueClass = classes.detailsValue;
        const { baseDebugId, searchRegexPattern } = this.props;
        return tools.map((toolInfo, index) => (
            <ExpansionPanel
                key={index}
                id={build(baseDebugId, index, toolInfo.name)}
            >
                <ExpansionPanelSummary expandIcon={<ExpandMoreIcon />}>
                    <Typography variant="caption">
                        <Highlighter search={searchRegexPattern}>
                            {toolInfo.name}
                        </Highlighter>
                        :
                        <Highlighter search={searchRegexPattern}>
                            {toolInfo.description}
                        </Highlighter>
                    </Typography>
                </ExpansionPanelSummary>
                <ExpansionPanelDetails>
                    <table>
                        <tbody>
                            <tr>
                                <td>{getMessage("detailsLabel")}</td>
                            </tr>
                            <tr>
                                <td className={labelClass}>
                                    {getMessage("toolNameLabel")}
                                </td>
                                <td className={valueClass}>
                                    <Highlighter search={searchRegexPattern}>
                                        {toolInfo.name}
                                    </Highlighter>
                                </td>
                            </tr>
                            <tr>
                                <td className={labelClass}>
                                    {getMessage("descriptionLabel")}
                                </td>
                                <td className={valueClass}>
                                    <Highlighter search={searchRegexPattern}>
                                        {toolInfo.description}
                                    </Highlighter>
                                </td>
                            </tr>
                            <tr>
                                <td className={labelClass}>
                                    {getMessage("imageLabel")}
                                </td>
                                <td className={valueClass}>{toolInfo.image}</td>
                            </tr>
                            <tr>
                                <td className={labelClass}>
                                    {getMessage("toolVersionLabel")}
                                </td>
                                <td className={valueClass}>
                                    {toolInfo.version}
                                </td>
                            </tr>
                            <tr>
                                <td className={labelClass}>
                                    {getMessage("toolAttributionLabel")}
                                </td>
                                <td className={valueClass}>
                                    {toolInfo.attribution}
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </ExpansionPanelDetails>
            </ExpansionPanel>
        ));
    }
}

export default withStyles(style)(withI18N(ToolDetailsV1, intlData));
