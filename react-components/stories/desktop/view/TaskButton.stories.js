/**
 *
 * @author sriram
 *
 */
import React, {Component} from "react";
import TaskButton from "../../../src/desktop/view/TaskButton";

class TaskButtonTest extends Component {
    render() {
        const windowConfig1 = {
            "tag": "0",
            "type": "DATA",
            "windowTitle": "Data",
            "minimized": false,

        };
        const windowConfig2 = {
            "tag": "",
            "type": "APPS",
            "windowTitle": "Apps",
            "minimized": true,

        }


        return (
            <div>
                <TaskButton windowConfig={windowConfig1}/>
                <TaskButton windowConfig={windowConfig2}/>
            </div>
        );
    }
}
export default TaskButtonTest;