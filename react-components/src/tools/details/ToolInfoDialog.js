/**
 *  @author sriram
 *
 **/

import React, { useEffect, useState } from "react";
import messages from "../messages";
import {
    DEDialogHeader,
    formatMessage,
    getMessage,
    LoadingMask,
    withI18N,
} from "@cyverse-de/ui-lib";
import {
    Dialog,
    DialogContent,
    makeStyles,
    Paper,
    Tab,
    Tabs,
    Typography,
} from "@material-ui/core";
import { AppGridListing } from "../../apps/listing";
import { injectIntl } from "react-intl";
import Grid from "@material-ui/core/Grid";

const NOT_APPLICABLE = "N/A";

const useStyles = makeStyles((theme) => ({
    detailsLabel: {
        fontWeight: "bold",
        fontSize: 10,
        width: 90,
    },
    detailsValue: {
        fontSize: 10,
        paddingLeft: 2,
    },
}));

function ToolInfoDialog(props) {
    const {
        selectedTool,
        dialogOpen,
        onClose,
        baseDebugId,
        presenter,
        intl,
    } = props;
    const [tabIndex, setTabIndex] = useState(0);
    const [loading, setLoading] = useState(false);
    const [apps, setApps] = useState(null);
    const [tool, setTool] = useState(null);

    useEffect(() => {
        setLoading(true);
        let promises = [];
        const toolInfoPromise = new Promise((resolve, reject) => {
            presenter.getToolInfo(
                selectedTool.id,
                (toolInfo) => {
                    setTool(toolInfo);
                    resolve("");
                },
                (errorCode, errorMessage) => {
                    reject(errorMessage);
                }
            );
        });

        const appsUsingToolPromise = new Promise((resolve, reject) => {
            presenter.getAppsForTool(
                selectedTool.id,
                (appsUsed) => {
                    setApps(appsUsed);
                    resolve("");
                },
                (errorCode, errorMessage) => {
                    reject(errorMessage);
                }
            );
        });

        promises.push(toolInfoPromise);
        promises.push(appsUsingToolPromise);

        Promise.all(promises)
            .then((value) => {
                setLoading(false);
            })
            .catch((error) => {
                setLoading(false);
            });
    }, [selectedTool]);

    const handleTabChange = (event, value) => {
        setTabIndex(value);
    };
    const classes = useStyles();

    let labelClass = classes.detailsLabel,
        valueClass = classes.detailsValue;

    return (
        <React.Fragment>
            <Dialog open={dialogOpen} maxWidth="sm" id={baseDebugId}>
                <DEDialogHeader
                    heading={selectedTool ? selectedTool.name : ""}
                    onClose={onClose}
                    id={baseDebugId}
                />
                <DialogContent style={{ minHeight: 600 }}>
                    <LoadingMask loading={loading}>
                        <Tabs
                            indicatorColor="primary"
                            textColor="primary"
                            value={tabIndex}
                            onChange={handleTabChange}
                            variant="scrollable"
                            scrollButtons="auto"
                        >
                            <Tab
                                label={formatMessage(
                                    intl,
                                    "toolInformationLbl"
                                )}
                            />
                            <Tab
                                label={formatMessage(intl, "appsUsingToolLbl")}
                            />
                        </Tabs>
                        {tabIndex === 0 && tool && (
                            <Paper id={baseDebugId} style={{ padding: 5 }}>
                                <Grid
                                    container
                                    spacing={2}
                                    style={{ paddingLeft: 5 }}
                                >
                                    <Grid item xs={12}>
                                        <span className={labelClass}>
                                            {getMessage("toolAttributionLabel")}
                                            :
                                        </span>
                                        <span className={valueClass}>
                                            {selectedTool.attribution}
                                        </span>
                                    </Grid>
                                    <Grid item xs={12}>
                                        <span className={labelClass}>
                                            {getMessage("descriptionLabel")}:
                                        </span>
                                        <span className={valueClass}>
                                            {selectedTool.description}
                                        </span>
                                    </Grid>
                                    <Grid item xs={12}>
                                        <Typography variant="h6">
                                            {getMessage("restrictionsLabel")}
                                        </Typography>
                                    </Grid>
                                    <Grid item xs={12}>
                                        <span className={labelClass}>
                                            {getMessage("memoryLimitLabel")}:
                                        </span>
                                        <span className={valueClass}>
                                            {tool.container.memory_limit
                                                ? tool.container.memory_limit
                                                : NOT_APPLICABLE}
                                        </span>
                                    </Grid>
                                    <Grid item xs={12}>
                                        <span className={labelClass}>
                                            {getMessage("pidsLimitLabel")}:
                                        </span>
                                        <span className={valueClass}>
                                            {tool.container.pids_limit
                                                ? tool.container.pids_limit
                                                : NOT_APPLICABLE}
                                        </span>
                                    </Grid>
                                    <Grid item xs={12}>
                                        <span className={labelClass}>
                                            {getMessage("networkingLabel")}:
                                        </span>
                                        <span className={valueClass}>
                                            {tool.container.network_mode
                                                ? tool.container.network_mode
                                                : NOT_APPLICABLE}
                                        </span>
                                    </Grid>
                                    <Grid item xs={12}>
                                        <span className={labelClass}>
                                            {getMessage("secondsLimitLabel")}:
                                        </span>
                                        <span className={valueClass}>
                                            {tool.time_limit_seconds
                                                ? tool.time_limit_seconds
                                                : NOT_APPLICABLE}
                                        </span>
                                    </Grid>
                                </Grid>
                            </Paper>
                        )}
                        {tabIndex === 1 && (
                            <AppGridListing
                                apps={apps}
                                sortField="name"
                                sortDir="asc"
                                selectable={false}
                                deletable={false}
                                enableMenu={false}
                                isSelected={() => false}
                                selectedApps={[]}
                                onSortChange={() => {}}
                                resetAppSelection={() => {}}
                                handleAppSelection={() => {}}
                            />
                        )}
                    </LoadingMask>
                </DialogContent>
            </Dialog>
        </React.Fragment>
    );
}

export default withI18N(injectIntl(ToolInfoDialog), messages);
