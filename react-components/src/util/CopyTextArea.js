/**
 * @author psarando
 */
import React, {Component} from "react";
import Button from "@material-ui/core/Button";
import TextField from "@material-ui/core/TextField";
import {hasClipboardAPI, copySelection} from "../clipboardFunctions";

class CopyTextArea extends Component {
    constructor(props) {
        super(props);

        this.state = {
            btnText: props.btnText
        };

        // This binding is necessary to make `this` work in the callback
        this.onCopyText = this.onCopyText.bind(this);
    }

    onCopyText(e) {
        e.preventDefault();
        let ele = document.getElementById(`${this.props.debugIdPrefix}.CopyTextArea.TextField`);
        ele.select();
        if (copySelection()) {
            this.setState({btnText: this.props.copiedText});
        }
    }

    componentDidMount() {
        if (!hasClipboardAPI()) {
            let ele = document.getElementById(`${this.props.debugIdPrefix}.CopyTextArea.TextField`);
            ele.select();
        }
    }

    render() {
        return (
            <div>
                <TextField id={`${this.props.debugIdPrefix}.CopyTextArea.TextField`}
                           value={this.props.text}
                           multiLine={true}
                           readOnly="readonly"
                           margin ="normal"
                           style={{width: '100%'}}/>
                {hasClipboardAPI() && (
                    <Button variant="raised" id={`${this.props.debugIdPrefix}.CopyTextArea.Button`}
                            onClick={this.onCopyText}
                            style={{padding: 2}}>
                        {this.state.btnText}
                    </Button>
                )}
            </div>
        );
    }
}

export default CopyTextArea;
