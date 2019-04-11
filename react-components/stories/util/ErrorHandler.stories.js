import ErrorHandler from "../../src/util/ErrorHandler";

import React, { Component } from "react";

class ErrorHandlerTest extends Component {
    render() {
        const errorSummary = "Everything is broken forever, come back later.";

        const errorDetails =
            "Error: ERR_ILLEGAL_ARGUMENT<br>" +
            "Message: (not (map? nil))<br><br><br>" +
            "GWT Version: 2.8.0<br>GXT Version:: 4.0.2<br>" +
            "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Safari/537.36<br>" +
            "Date: Wed Jan 16 11:07:28 GMT-700 2019<br>Host: http://127.0.0.1:8080/de/";

        return (
            <ErrorHandler
                errorSummary={errorSummary}
                errorDetails={errorDetails}
            />
        );
    }
}

export default ErrorHandlerTest;
