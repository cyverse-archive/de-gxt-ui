/**
 * @author psarando
 */
import React, { Component } from 'react';
import RaisedButton from 'material-ui/RaisedButton';
import TextField from 'material-ui/TextField';
import {hasClipboardAPI, copySelection} from '../clipboardFunctions'

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

        this.refs.copyTextArea.select();
        if (copySelection()) {
            this.setState({btnText: this.props.copiedText});
        }
    }

    componentDidMount() {
        if (!hasClipboardAPI()) {
            this.refs.copyTextArea.select();
        }
    }

    render() {
        return (
            <div>
                <TextField id={`${this.props.debugIdPrefix}.CopyTextArea.TextField`}
                           ref="copyTextArea"
                           value={this.props.text}
                           multiLine={true}
                           readOnly="readonly" />
                {hasClipboardAPI() && (
                    <RaisedButton id={`${this.props.debugIdPrefix}.CopyTextArea.Button`}
                                  onClick={this.onCopyText}>{
                        this.state.btnText
                    }</RaisedButton>
                )}
            </div>
        );
    }
}

export default CopyTextArea;
