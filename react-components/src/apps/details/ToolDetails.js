/**
 * @author sriram
 *
 */
import React, { Component } from "react";
import ExpansionPanel from "@material-ui/core/ExpansionPanel";
import ExpansionPanelSummary from "@material-ui/core/ExpansionPanelSummary";
import ExpansionPanelDetails from "@material-ui/core/ExpansionPanelDetails";
import ExpandMoreIcon from "@material-ui/icons/ExpandMore";
import intlData from "../messages";
import styles from "../style";
import injectSheet from "react-jss";
import withI18N, { getMessage } from "../../util/I18NWrapper";
import Typography from "@material-ui/core/Typography";
import build from "../../util/DebugIDUtil";

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
            labelClass = classes.toolDetailsLabel,
            valueClass = classes.toolDetailsValue;
        const {baseDebugId} = this.props;
        return (
                    tools.map((toolInfo, index) => (
                        <ExpansionPanel key={index} id={build(baseDebugId, toolInfo.name)}>
                            <ExpansionPanelSummary expandIcon={<ExpandMoreIcon/>}>
                                <Typography variant="caption"> {toolInfo.name}:
                                    {toolInfo.description}</Typography>
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
                                            {toolInfo.name}
                                        </td>
                                    </tr>
                                    <tr>
                                        <td className={labelClass}>
                                            {getMessage("descriptionLabel")}
                                        </td>
                                        <td className={valueClass}>
                                            {toolInfo.description}
                                        </td>
                                    </tr>
                                    <tr>
                                        <td className={labelClass}>
                                            {getMessage("imageLabel")}
                                        </td>
                                        <td className={valueClass}>
                                            {toolInfo.image}
                                        </td>
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
                ))
        );
    }
}

export default injectSheet(styles)(withI18N(ToolDetailsV1, intlData));
