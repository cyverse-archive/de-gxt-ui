/**
 * @author psarando
 */
import React, { Component } from 'react';
import RaisedButton from 'material-ui/RaisedButton';
import TextField from 'material-ui/TextField';
import {hasClipboardAPI, copySelection} from '../../clipboardFunctions'

class CopyTextArea extends Component {
    constructor(props) {
        super(props);

        this.state = {
            btnText: props.btnText
        };

        // This binding is necessary to make `this` work in the callback
        this.onCopyText = this.onCopyText.bind(this);
    }

    renderCopyTextArea(text) {
        return <TextField ref="copyTextArea" value={text} multiLine={true} readOnly="readonly" />;
    }

    renderCopyButton(btnText) {
        if (hasClipboardAPI()) {
            return <RaisedButton ref="copyTextBtn" onClick={this.onCopyText}>{btnText}</RaisedButton>;
        }
    }

    onCopyText(e) {
        e.preventDefault();

        this.refs.copyTextArea.select();
        if (copySelection()) {
            this.setState({btnText: "Copied!"});
        }
    }

    componentDidMount() {
        if (!hasClipboardAPI()) {
            this.refs.copyTextArea.select();
        }
    }

    render() {
        let copyTextArea = this.renderCopyTextArea(this.props.text),
            copyButton = this.renderCopyButton(this.state.btnText);

        return (
            <div>
                {copyTextArea}
                {copyButton}
            </div>
        );
    }
}

export default CopyTextArea;
