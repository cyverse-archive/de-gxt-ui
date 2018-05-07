/**
 @author sriram
 */
import React, {Component} from "react";
export default function withI18N(WrappedComponent, intlData) {
    return class extends React.Component {
        constructor(props) {
            super(props);
        }

        render() {
            return (
                <WrappedComponent {...intlData} {...this.props}/>
            );
        }
    }
}