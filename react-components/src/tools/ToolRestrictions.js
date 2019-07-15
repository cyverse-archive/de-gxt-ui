import React from "react";

import ids from "./ids";
import SimpleExpansionPanel from "./SimpleExpansionPanel";

import {
    build,
    FormNumberField,
    FormSelectField,
    FormTextField,
    getMessage,
} from "@cyverse-de/ui-lib";
import { Field } from "formik";
import { MenuItem, Typography } from "@material-ui/core";
import PropTypes from "prop-types";

const ONE_GB = 1024 * 1024 * 1024;

function convertToGB(size) {
    let gbValue = Math.round(size / ONE_GB);
    return gbValue + " GB";
}

function buildLimitList(startValue, maxValue) {
    let limits = [];
    limits.push(0);
    limits.push(startValue);

    let value = startValue;
    while (value < maxValue) {
        value *= 2;
        limits.push(value);
    }
    return limits;
}

function Restrictions(props) {
    const { parentId, maxCPUCore, maxMemory, maxDiskSpace } = props;

    const maxCPUCoreList = buildLimitList(1, maxCPUCore);
    const memoryLimitList = buildLimitList(2 * ONE_GB, maxMemory);
    const minDiskSpaceList = buildLimitList(ONE_GB, maxDiskSpace);

    return (
        <SimpleExpansionPanel
            header={getMessage("restrictions")}
            parentId={parentId}
        >
            <Field
                name="container.max_cpu_cores"
                label={getMessage("maxCPUCores")}
                id={build(parentId, ids.EDIT_TOOL_DLG.MAX_CPU_CORES)}
                component={FormSelectField}
            >
                {maxCPUCoreList.map((size, index) => (
                    <MenuItem
                        key={index}
                        value={size}
                        id={build(
                            parentId,
                            ids.EDIT_TOOL_DLG.MAX_CPU_CORES,
                            size
                        )}
                    >
                        {size}
                    </MenuItem>
                ))}
            </Field>
            <Field
                name="container.memory_limit"
                label={getMessage("memoryLimit")}
                id={build(parentId, ids.EDIT_TOOL_DLG.MEMORY_LIMIT)}
                renderValue={convertToGB}
                component={FormSelectField}
            >
                {memoryLimitList.map((size, index) => (
                    <MenuItem
                        key={index}
                        value={size}
                        id={build(
                            parentId,
                            ids.EDIT_TOOL_DLG.MEMORY_LIMIT,
                            size
                        )}
                    >
                        {convertToGB(size)}
                    </MenuItem>
                ))}
            </Field>
            <Field
                name="container.min_disk_space"
                label={getMessage("minDiskSpace")}
                id={build(parentId, ids.EDIT_TOOL_DLG.MIN_DISK_SPACE)}
                renderValue={convertToGB}
                component={FormSelectField}
            >
                {minDiskSpaceList.map((size, index) => (
                    <MenuItem
                        key={index}
                        value={size}
                        id={build(
                            parentId,
                            ids.EDIT_TOOL_DLG.MIN_DISK_SPACE,
                            size
                        )}
                    >
                        {convertToGB(size)}
                    </MenuItem>
                ))}
            </Field>
            <Typography variant="caption">
                {getMessage("restrictionsSupport")}
            </Typography>
            <Field
                name="container.pids_limit"
                label={getMessage("pidsLimit")}
                id={build(parentId, ids.EDIT_TOOL_DLG.PIDS_LIMIT)}
                disabled
                component={FormNumberField}
            />
            <Field
                name="container.network_mode"
                label={getMessage("networkMode")}
                id={build(parentId, ids.EDIT_TOOL_DLG.NETWORK_MODE)}
                disabled
                component={FormTextField}
            />
            <Field
                name="time_limit_seconds"
                label={getMessage("timeLimit")}
                id={build(parentId, ids.EDIT_TOOL_DLG.TIME_LIMIT)}
                disabled
                component={FormNumberField}
            />
        </SimpleExpansionPanel>
    );
}

Restrictions.propTypes = {
    parentId: PropTypes.string.isRequired,
    maxCPUCore: PropTypes.number.isRequired,
    maxDiskSpace: PropTypes.number.isRequired,
    maxMemory: PropTypes.number.isRequired,
};

export default Restrictions;
