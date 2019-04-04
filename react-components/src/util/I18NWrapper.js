/**
 @author sriram
 */
import React from "react";
import {
    FormattedHTMLMessage,
    FormattedMessage,
    IntlProvider,
} from "react-intl";

const withI18N = (WrappedComponent, intlData) => {
    return class extends React.Component {
        render() {
            const { messages, ...rest } = this.props;
            let combinedMessages = { ...messages, ...intlData.messages };
            return (
                <IntlProvider
                    locale={intlData.locales}
                    defaultLocale="en"
                    messages={combinedMessages}
                >
                    <WrappedComponent messages={combinedMessages} {...rest} />
                </IntlProvider>
            );
        }
    };
};

const getMessage = (id, options) => {
    if (options && options.values) {
        return <FormattedMessage id={id} values={options.values} />;
    }
    return <FormattedMessage id={id} />;
};

const formatMessage = (intl, id, values) => {
    return intl.formatMessage({ id }, values);
};

const formatHTMLMessage = (id, html) => {
    return <FormattedHTMLMessage id={id} defaultMessage={html} />;
};

export default withI18N;

export { getMessage, formatMessage, formatHTMLMessage };
