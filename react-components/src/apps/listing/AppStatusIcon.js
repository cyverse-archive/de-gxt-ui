import betaPng from "../../resources/images/betaSymbol.png";
import betaSvg from "../../resources/images/betaSymbol.svg";
import messages from "../messages";
import withI18N, { getMessage } from "../../util/I18NWrapper";

import Disabled from "@material-ui/icons/Block";
import Lock from "@material-ui/icons/Lock";
import PropTypes from "prop-types";
import React from "react";
import ToolTip from "@material-ui/core/Tooltip";

/**
 * @author aramsey
 *
 * An icon used to visually indicate whether an app is private, beta, or disabled
 */

function AppStatusIcon(props) {
    const { app, ...custom } = props;
    const isPrivate = !app.is_public;
    const isDisabled = app.disabled;
    const isBeta = app.beta;

    if (isPrivate) {
        return <PrivateIcon {...custom} />;
    }
    if (isDisabled) {
        return <DisabledIcon {...custom} />;
    }
    if (isBeta) {
        return <BetaIcon {...custom} />;
    }
    return null;
}

AppStatusIcon.propTypes = {
    app: PropTypes.shape({
        is_public: PropTypes.bool.isRequired,
        disabled: PropTypes.bool.isRequired,
        beta: PropTypes.bool.isRequired,
    }),
};

function PrivateIcon(props) {
    return (
        <ToolTip title={getMessage("privateToolTip")}>
            <Lock {...props} />
        </ToolTip>
    );
}

function DisabledIcon(props) {
    return (
        <ToolTip title={getMessage("disabledToolTip")}>
            <Disabled {...props} />
        </ToolTip>
    );
}

function BetaIcon(props) {
    return (
        <ToolTip title={getMessage("betaToolTip")}>
            <img src={betaSvg} alt={betaPng} />
        </ToolTip>
    );
}

export default withI18N(AppStatusIcon, messages);
