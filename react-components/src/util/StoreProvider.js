import store from "../store";

import React, { Component } from "react";
import { Provider } from "react-redux";

export default function withStoreProvider(WrappedComponent) {
    return class extends Component {
        render() {
            return (
                <Provider store={store}>
                    <WrappedComponent {...this.props} />
                </Provider>
            );
        }
    };
}
