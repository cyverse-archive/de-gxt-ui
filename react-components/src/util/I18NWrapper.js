/**
 @author sriram
 */
import React from "react";
import {FormattedMessage, IntlProvider} from "react-intl";

export default function withI18N(WrappedComponent, intlData) {
    return class extends React.Component {
        render() {
            return (
                <IntlProvider locale={intlData.locales} defaultLocale='en' messages={intlData.messages}>
                    <WrappedComponent {...intlData} {...this.props}/>
                </IntlProvider>
            );
        }
    }
}

export function getMessage(id, options) {
    if (options && options.values) {
        return (
            <FormattedMessage id={id} values={options.values}/>
        );
    }
    return (
        <FormattedMessage id={id}/>
    );
}

