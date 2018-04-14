/**
 * @author psarando
 */
import React, { Component } from 'react';
import {
    MediaCard,
    MediaCardGroup,
    SummaryText,
} from "cyverse-ui";

class ToolDetails extends Component {
    constructor(props) {
        super(props);

        this.state = {
            selectedToolIndex: 0
        };

        // This binding is necessary to make `this` work in the callback
        this.onToolSelectionChange = this.onToolSelectionChange.bind(this);
    }

    onToolSelectionChange(event, selectedToolIndex) {
        this.setState({
            selectedToolIndex: selectedToolIndex
        });
    }

    render() {
        let tools      = this.props.app.tools,
            appearance = this.props.appearance,
            cardClass = appearance.css().detailsCard(),
            labelClass = appearance.css().label(),
            valueClass = appearance.css().value();

        return (
            <MediaCardGroup>
                {
                    tools.map( (toolInfo, index) => (
                <MediaCard
                    key={index}
                    className={cardClass}
                    title={toolInfo.name}
                    summary={ <SummaryText children={toolInfo.description} /> }

                    detail = {
                <table>
                    <tbody>
                    <tr>
                        <td>
                            {appearance.detailsLabel()}
                        </td>
                    </tr>
                    <tr>
                        <td className={labelClass}>
                            {appearance.toolNameLabel()}
                        </td>
                        <td className={valueClass}>
                            {toolInfo.name}
                        </td>
                    </tr>
                    <tr>
                        <td className={labelClass}>
                            {appearance.descriptionLabel()}
                        </td>
                        <td className={valueClass}>
                            {toolInfo.description}
                        </td>
                    </tr>
                    <tr>
                        <td className={labelClass}>
                            {appearance.imageLabel()}
                        </td>
                        <td className={valueClass}>
                            {toolInfo.image}
                        </td>
                    </tr>
                    <tr>
                        <td className={labelClass}>
                            {appearance.toolVersionLabel()}
                        </td>
                        <td className={valueClass}>
                            {toolInfo.version}
                        </td>
                    </tr>
                    <tr>
                        <td className={labelClass}>
                            {appearance.toolAttributionLabel()}
                        </td>
                        <td className={valueClass}>
                            {toolInfo.attribution}
                        </td>
                    </tr>
                    </tbody>
                </table>
                    }
                />
                        )
                    )
                }
            </MediaCardGroup>
        );
    }
}

export default ToolDetails;
