import React, { Component } from "react";

import ManageTools from "../../src/tools/ManageTools";

class ManageToolsTest extends Component {
    render() {
        const logger =
            this.props.logger ||
            ((data) => {
                console.log(data);
            });

        const presenter = {
            onToolSelectionChanged: (tool) =>
                logger("Tool selection changed", tool),
            onShowToolInfo: (toolId) => logger("Show tool info", toolId),
            onNewToolSelected: () => logger("Add Tool selected"),
            onRequestToolSelected: () => logger("Request Tool selected"),
            onEditToolSelected: (toolId) =>
                logger("Edit Tool selected", toolId),
            onDeleteToolsSelected: (toolId, toolName) =>
                logger("Delete Tool selected", toolId, toolName),
            useToolInNewApp: (useToolInNewApp) =>
                logger("Use Tool in App selected", useToolInNewApp),
            onShareToolsSelected: (tool) =>
                logger("Share with Collaborators selected", tool),
            onRequestToMakeToolPublicSelected: (tool) =>
                logger("Make Tool Public selected", tool),
            loadTools: (
                isPublic,
                searchTerm,
                order,
                orderBy,
                rowsPerPage,
                page
            ) =>
                logger(
                    "Load tools selected",
                    isPublic,
                    searchTerm,
                    order,
                    orderBy,
                    rowsPerPage,
                    page
                ),
            searchTools: (searchTerm) => logger("Search for tools", searchTerm),
        };

        const parentId = "gwt-debug-manageToolsWindow";

        const toolList = {
            total: 50,
            tools: [
                {
                    description:
                        "Cyverse Jupyter Lab beta with updated iJab plugin and jupyterlab_irods",
                    permission: "read",
                    interactive: true,
                    name: "jupyterlab",
                    type: "interactive",
                    restricted: false,
                    is_public: true,
                    id: "a66a456a-6f0e-4760-abd1-c7538fcbcd28",
                    container: {
                        image: {
                            name: "gims.cyverse.org:5000/jupyter-lab",
                            tag: "beta",
                            deprecated: false,
                        },
                    },
                    version: "0.0.3",
                    implementation: {
                        implementor: "Upendra Kumar Devisetty",
                        implementor_email: "upendra@cyverse.org",
                    },
                    time_limit_seconds: 0,
                },
                {
                    description: "Jupyterlab with Qiime2",
                    permission: "read",
                    interactive: true,
                    name: "qiime2",
                    type: "interactive",
                    restricted: false,
                    is_public: true,
                    id: "326f3e62-b113-4d49-85bf-d3e314beda5d",
                    container: {
                        image: {
                            name: "gims.cyverse.org:5000/jupyterlabqiime2",
                            tag: "2018.11",
                            deprecated: false,
                        },
                    },
                    version: "0.0.1",
                    implementation: {
                        implementor: "Upendra Kumar Devisetty",
                        implementor_email: "upendra@cyverse.org",
                    },
                    time_limit_seconds: 0,
                },
                {
                    permission: "read",
                    interactive: true,
                    name: "spacetracejupyterlab",
                    type: "interactive",
                    restricted: false,
                    is_public: true,
                    id: "8c555b56-3b8c-11e9-8420-008cfa5ae621",
                    container: {
                        image: {
                            name: "docker.cyverse.org/spacetracejupyter",
                            tag: "1.0",
                            deprecated: false,
                        },
                    },
                    attribution: "Todd Wickizer",
                    version: "0.0.1",
                    implementation: {
                        implementor: "Upendra Kumar Devisetty",
                        implementor_email: "upendra@cyverse.org",
                    },
                    time_limit_seconds: 0,
                },
                {
                    description: "A test tool for OSG.",
                    permission: "own",
                    interactive: false,
                    name: "osg-word-count",
                    type: "osg",
                    restricted: true,
                    is_public: false,
                    id: "6189b450-5d1e-11e8-a92d-5a03816fc427",
                    container: {
                        image: {
                            name: "discoenv/osg-word-count",
                            tag: "1.0.0",
                            deprecated: false,
                        },
                    },
                    version: "1.0.0",
                    implementation: {
                        implementor: "Sarah Roberts",
                        implementor_email: "sarahr@cyverse.org",
                    },
                    time_limit_seconds: 86400,
                },
            ],
        };

        const searchTerm = "";

        const order = "asc";

        const orderBy = "name";

        const rowsPerPage = 100;

        const page = 0;

        return (
            <ManageTools
                loading={false}
                parentId={parentId}
                toolList={toolList}
                presenter={presenter}
                searchTerm={searchTerm}
                order={order}
                orderBy={orderBy}
                rowsPerPage={rowsPerPage}
                page={page}
            />
        );
    }
}

export default ManageToolsTest;
