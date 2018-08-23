/**
 *
 * @author Sriram
 *
 */

import React, { Component } from 'react';
import AnalysisCommentsDialog from "../../../../src/analysis/view/dialogs/AnalysisCommentsDialog";

class AnalysisCommentsDialogTest extends Component {
    render() {
        return (
            <AnalysisCommentsDialog dialogOpen={true} comments="This is a test!"/>
        );
    }
}

export default AnalysisCommentsDialogTest;