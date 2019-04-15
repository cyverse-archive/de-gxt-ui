/**
 *
 * @author sriram
 *
 */

import React from "react";
import Paper from "@material-ui/core/Paper";
import GridList from "@material-ui/core/GridList";
import GridListTile from "@material-ui/core/GridListTile";
import { QuickLaunch } from "@cyverse-de/de-components";
import Code from "@material-ui/icons/Code.js";
import IconButton from "@material-ui/core/IconButton";
import { Tooltip } from "@material-ui/core";
import withI18N, { formatMessage } from "../../util/I18NWrapper";
import { injectIntl } from "react-intl";
import intlData from "../messages";

function ListQuickLaunches(props) {
    const { quickLaunches, intl, userName } = props;
    const quickLaunchClickHandler = (id) => {
        console.log("Quick launch with id: " + id);
    };
    const embedCodeClickHandler = (id) => {
        console.log("Embed code with id: " + id);
    };
    const deleteQuickLaunchHandler = (id) => {
        console.log("Delete quick launch with id: " + id);
    };
    return (
        <Paper>
            <GridList cellHeight={50} cols={2}>
                {quickLaunches.map((qLaunch) => {
                    const id = qLaunch.id;
                    const onDelete =
                        userName === qLaunch.creator
                            ? (id) => deleteQuickLaunchHandler(id)
                            : undefined;
                    return (
                        <GridListTile key={id} cols={2}>
                            <Tooltip
                                title={formatMessage(intl, "qLaunchToolTip")}
                            >
                                <QuickLaunch
                                    label={qLaunch.name}
                                    isPublic={qLaunch.is_public}
                                    handleClick={() =>
                                        quickLaunchClickHandler(id)
                                    }
                                    handleDelete={onDelete}
                                />
                            </Tooltip>
                            <Tooltip
                                title={formatMessage(
                                    intl,
                                    "qLaunchEmbedToolTip"
                                )}
                            >
                                <IconButton
                                    style={{ margin: 1 }}
                                    onClick={() => embedCodeClickHandler(id)}
                                >
                                    <Code color="primary" />
                                </IconButton>
                            </Tooltip>
                        </GridListTile>
                    );
                })}
            </GridList>
        </Paper>
    );
}

export default withI18N(injectIntl(ListQuickLaunches), intlData);
