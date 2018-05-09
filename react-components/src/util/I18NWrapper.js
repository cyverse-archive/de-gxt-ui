/**
 @author sriram
 */
import React from "react";
export default function withI18N(WrappedComponent, intlData) {
    return class extends React.Component {
        render() {
            return (
                <WrappedComponent {...intlData} {...this.props}/>
            );
        }
    }
}