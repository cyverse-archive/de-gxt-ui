import ErrorExpansionPanel from '../../src/util/ErrorExpansionPanel';

import React, { Component } from 'react';

class ErrorExpansionPanelTest extends Component {
    render() {
        const errMsg = "Error: ERR_ILLEGAL_ARGUMENT<br>Real Error: Everything is broken forever, run!";
        const username = "scienceRulez";
        const userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36";
        const date = new Date().toDateString();
        const host = "https://qa.cyverse.org/de/error";

        return (
            <ErrorExpansionPanel errMsg={errMsg}
                                 username={username}
                                 userAgent={userAgent}
                                 date={date}
                                 host={host}/>
        )
    }
}

export default ErrorExpansionPanelTest;