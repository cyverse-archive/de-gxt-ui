import React, {Component} from 'react';
import {
    IntlProvider,
    FormattedMessage
} from 'react-intl';

export function hasI18N(WrappedComponent, i18nData) {
    return class extends Component {

        render() {
            return (
                <IntlProvider locale='en' defaultLocale='en' messages={i18nData}>
                    <WrappedComponent i18nData={i18nData} {...this.props} />
                </IntlProvider>
            )
        }
    }
}

export function getMessage(id) {
    return (
        <FormattedMessage id={id}/>
    )
}