/**
 *  @author sriram
 *
 **/

import React, { useEffect, useState } from "react";

import PropTypes from "prop-types";
import { injectIntl } from "react-intl";

import build from "../../util/DebugIDUtil";
import intlData from "../messages";
import ids from "../ids";
import withI18N, { formatMessage } from "../../util/I18NWrapper";

import AppDetails from "./AppDetails";
import AppDoc from "./AppDoc";
import DEDialogHeader from "../../util/dialog/DEDialogHeader";
import DEConfirmationDialog from "../../util/dialog/DEConfirmationDialog";
import ToolDetails from "./ToolDetails";

import Dialog from "@material-ui/core/Dialog";
import DialogContent from "@material-ui/core/DialogContent";
import Tabs from "@material-ui/core/Tabs";
import Tab from "@material-ui/core/Tab";

export const EDIT_MODE = "edit";
export const VIEW_MODE = "view";

function AppInfoDialog(props) {

    const {
        dialogOpen,
        app,
        presenter,
        docEditable,
        searchRegexPattern,
        baseDebugId,
        intl,
    } = props;
    const appInfoLabel = formatMessage(intl, "appInformationLbl");
    const toolInfoLabel = formatMessage(intl, "toolInformationLbl");
    const appDocLabel = formatMessage(intl, "appDocLabel");

    const [tabIndex, setTabIndex] = useState(0);
    const [dirty, setDirty] = useState(false);
    const [confirmDialogOpen, setConfirmDialogOpen] = useState(false);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(false);
    const [mode, setMode] = useState(VIEW_MODE);
    const [documentation, setDocumentation] = useState(null);
    const [references, setReferences] = useState(null);

    useEffect(() => {
        function handleSuccess(doc) {
            setLoading(false);
            setDocumentation(doc.documentation);
            setReferences(doc.references);
        }

        function handleFailure(statusCode, message) {
            setLoading(false);
            setError(true);
        }

        if (!app.wiki_url) {
            setLoading(true);
            presenter.getAppDoc(app, handleSuccess, handleFailure);
        }
    }, []);

    const handleTabChange = (event, value) => {
        setTabIndex(value);
    };

    const onClose = () => {
        if (dirty) {
            setConfirmDialogOpen(true);
        } else {
            presenter.onClose();
        }
    };

    const saveDoc = () => {
        setLoading(true);
        presenter.onSaveMarkdownSelected(
            app.id,
            app.system_id,
            documentation,
            () => {
                setLoading(false);
                setMode(VIEW_MODE);
                setDirty(false);
                setConfirmDialogOpen(false);
            },
            (statusCode, errorMessage) => {
                setLoading(false);
                setError(true);
                setConfirmDialogOpen(false);
            }
        );
    };

    const onModeChange = (mode) => {
        setMode(mode);
    };

    const onDocChange = (updatedDoc) => {
        setDirty(true);
        setDocumentation(updatedDoc);
    };

    const onDiscardChanges = () => {
        setDirty(false);
        setConfirmDialogOpen(false);
        presenter.onClose();
    };

    return (
        <React.Fragment>
            <Dialog open={dialogOpen} fullWidth={true} id={baseDebugId}>
                <DEDialogHeader heading={app.name} onClose={onClose} />
                <DialogContent style={{ minHeight: 600 }}>
                    <Tabs
                        indicatorColor="primary"
                        textColor="primary"
                        value={tabIndex}
                        onChange={handleTabChange}
                    >
                        <Tab label={appInfoLabel} />
                        <Tab label={toolInfoLabel} />
                        <Tab label={appDocLabel} />
                    </Tabs>
                    {tabIndex === 0 && (
                        <AppDetails
                            baseDebugId={baseDebugId}
                            searchRegexPattern={searchRegexPattern}
                            details={app}
                            presenter={presenter}
                        />
                    )}
                    {tabIndex === 1 && (
                        <ToolDetails
                            baseDebugId={build(
                                baseDebugId,
                                ids.DETAILS.APP_TOOLS
                            )}
                            searchRegexPattern={searchRegexPattern}
                            details={app.tools}
                        />
                    )}
                    {tabIndex === 2 && (
                        <AppDoc
                            baseDebugId={build(
                                baseDebugId,
                                ids.DETAILS.APP_DOCUMENTATION
                            )}
                            documentation={documentation}
                            references={references}
                            wiki_url={app.wiki_url}
                            appName={app.name}
                            onDocChange={(doc) => onDocChange(doc)}
                            editable={docEditable}
                            saveDoc={saveDoc}
                            loading={loading}
                            error={error}
                            mode={mode}
                            onModeChange={onModeChange}
                        />
                    )}
                </DialogContent>
            </Dialog>
            <DEConfirmationDialog
                dialogOpen={confirmDialogOpen}
                messages={intlData}
                debugId={baseDebugId}
                onOkBtnClick={saveDoc}
                onCancelBtnClick={onDiscardChanges}
                heading={formatMessage(intl, "save")}
                message={formatMessage(intl, "docSavePrompt")}
                okLabel={formatMessage(intl, "save")}
                cancelLabel={formatMessage(intl, "discardChanges")}
            />
        </React.Fragment>

    );
}

AppInfoDialog.propTypes = {
    dialogOpen: PropTypes.bool.isRequired,
    app: PropTypes.object.isRequired,
    presenter: PropTypes.object.isRequired,
    docEditable: PropTypes.bool.isRequired,
    searchRegexPattern: PropTypes.string.isRequired,
    baseDebugId: PropTypes.string.isRequired,
    intl: PropTypes.object.isRequired,
};

export default withI18N(injectIntl(AppInfoDialog), intlData);
