/**
 *  @author sriram
 *
 **/

import React, { useState } from 'react';
import Dialog from "@material-ui/core/Dialog";
import DialogContent from "@material-ui/core/DialogContent";
import withI18N, { formatMessage } from "../../util/I18NWrapper";
import intlData from "../messages";
import DEDialogHeader from "../../util/dialog/DEDialogHeader";
import Tabs from "@material-ui/core/Tabs";
import Tab from "@material-ui/core/Tab";

import ToolDetails from "./ToolDetails";
import AppDetails from "./AppDetails";
import { injectIntl } from "react-intl";

function AppInfoDialog(props) {
    const {dialogOpen, app, presenter, intl} = props;
    const appInfoLabel = formatMessage(intl, "appInformationLbl");
    const toolInfoLabel = formatMessage(intl, "toolInformationLbl");

    const [value, setValue] = useState(0);

    const handleChange = (event, value) => {
        setValue(value);
    };

    return (
        <Dialog open={dialogOpen} fullWidth={true}>
            <DEDialogHeader heading={app.name} onClose={() => presenter.onClose()}/>
            <DialogContent style={{minHeight: 600}}>
                <Tabs value={value} onChange={handleChange}>
                    <Tab label={appInfoLabel}/>
                    <Tab label={toolInfoLabel}/>
                </Tabs>
                {value === 0 && <AppDetails details={app}/>}
                {value === 1 && <ToolDetails details={app.tools}/>}
            </DialogContent>
        </Dialog>
    );
}

AppInfoDialog.propTypes = {};

export default (withI18N(injectIntl(AppInfoDialog), intlData));

