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
        this.state = {
            btnText: props.btnText,
        };
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
            this.setState({ btnText: this.props.copiedBtnText });
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
        const { multiline, text, debugIdPrefix } = this.props;
        return (
            <div>
                <TextField
                    id={`${debugIdPrefix}.CopyTextArea.TextField`}
                    value={text}
                    multiLine={multiline}
                    readOnly="readonly"
                    margin="normal"
                    style={{ width: "100%" }}
                />
                {hasClipboardAPI() && (
                    <Button
                        variant="raised"
                        id={`${debugIdPrefix}.CopyTextArea.Button`}
                        onClick={this.onCopyText}
                        style={{ padding: 2 }}
                    >
                        {this.state.btnText}
                    </Button>
                )}
            </div>
        );
    }
}

CopyTextArea.defaultProps = {
    multiline: false,
    btnText: "Copy",
    copiedBtnText: "Copied!",
};

CopyTextArea.propTypes = {
    debugIdPrefix: PropTypes.string,
    multiline: PropTypes.bool,
    text: PropTypes.string.isRequired,
    btnText: PropTypes.string,
    copiedBtnText: PropTypes.string,
};

export default CopyTextArea;
