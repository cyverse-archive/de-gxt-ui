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
    const {dialogOpen, details, intl} = props;
    const appInfoLabel = formatMessage(intl, "appInformationLbl");
    const toolInfoLabel = formatMessage(intl, "toolInformationLbl");

    const [value, setValue] = useState(0);

    const handleChange = (event, value) => {
        setValue(value);
    };

    return (
        <Dialog open={dialogOpen} fullWidth={true}>
            <DEDialogHeader heading={details.name}/>
            <DialogContent style={{minHeight: 500}}>
                <Tabs value={value} onChange={handleChange}>
                    <Tab label={appInfoLabel}/>
                    <Tab label={toolInfoLabel}/>
                </Tabs>
                {value === 0 && <AppDetails details={details}/>}
                {value === 1 && <ToolDetails details={details.tools}/>}
            </DialogContent>
        </Dialog>
    );
}

AppInfoDialog.propTypes = {};

export default (withI18N(injectIntl(AppInfoDialog), intlData));

