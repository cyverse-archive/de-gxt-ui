/**
 * @author psarando
 */
import React, { Component } from "react";
import Button from "@material-ui/core/Button";
import TextField from "@material-ui/core/TextField";
import { copySelection, hasClipboardAPI } from "../clipboardFunctions";
import PropTypes from "prop-types";

class CopyTextArea extends Component {
    constructor(props) {
        super(props);
        // This binding is necessary to make `this` work in the callback
        this.onCopyText = this.onCopyText.bind(this);
    }

    onCopyText(e) {
        e.preventDefault();
        let ele = document.getElementById(
            `${this.props.debugIdPrefix}.CopyTextArea.TextField`
        );
        if (ele) {
            ele.select();
        }
        if (copySelection()) {
            this.setState({ btnText: this.props.copiedText });
        }
    }

    componentDidMount() {
        if (!hasClipboardAPI()) {
            let ele = document.getElementById(
                `${this.props.debugIdPrefix}.CopyTextArea.TextField`
            );
            if (ele) {
                ele.select();
            }
        }
    }

    render() {
        const {multiline, btnText} = this.props;
        return (
            <div>
                <TextField
                    id={`${this.props.debugIdPrefix}.CopyTextArea.TextField`}
                    value={this.props.text}
                    multiLine={true}
                    readOnly="readonly"
                    margin="normal"
                    style={{ width: "100%" }}
                />
                {hasClipboardAPI() && (
                    <Button
                        variant="raised"
                        id={`${this.props.debugIdPrefix}.CopyTextArea.Button`}
                        onClick={this.onCopyText}
                        style={{ padding: 2 }}
                    >
                        {btnText}
                    </Button>
                )}
            </div>
        );
    }
}

CopyTextArea.defaultProps = {
    multiline: false,
    btnText: "Copy",
};

CopyTextArea.propTypes = {
    multiline: PropTypes.bool,
    text: PropTypes.string.isRequired,
    btnText: PropTypes.string,
};

export default CopyTextArea;
