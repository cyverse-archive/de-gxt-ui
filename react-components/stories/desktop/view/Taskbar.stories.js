/**
 *
 * @author sriram
 *
 */
import React, {Component} from "react";
import Taskbar from "../../../src/desktop/view/Taskbar";

class TaskbarTest extends Component {
    render() {

        const windowConfigs = [{
            "tag": "0",
            "type": "DATA",
            "windowTitle": "Data",
            "minimized": false,

        },
       {
            "tag": "",
            "type": "APPS",
            "windowTitle": "Apps",
            "minimized": true,

        }];
        return (
            <div>
                <Taskbar windows={windowConfigs}/>
            </div>
        );
    }
}

export default TaskbarTest;