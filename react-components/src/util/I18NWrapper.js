/**
 @author sriram
 */
import React from "react";
import { FormattedMessage, IntlProvider } from "react-intl";

const withI18N = (WrappedComponent, intlData) => {
    return class extends React.Component {
        render() {
            return (
                <IntlProvider locale={intlData.locales} defaultLocale='en' messages={intlData.messages}>
                    <WrappedComponent {...intlData} {...this.props}/>
                </IntlProvider>
            );
        }
    }
};

const getMessage = (id, options) => {
    if (options && options.values) {
        return (
            <FormattedMessage id={id} values={options.values}/>
        );
    }
    return (
        <FormattedMessage id={id}/>
    );
};

const formatMessage = (intl, id, values) => {
    return intl.formatMessage({id}, values);
};

export default withI18N;

export {
    getMessage,
    formatMessage,
};
