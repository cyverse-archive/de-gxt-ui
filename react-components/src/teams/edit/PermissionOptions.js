/**
 * @author aramsey
 *
 * Menu options corresponding to privileges that can be applied for a team
 */

import React from "react";

import Privilege from "../../models/Privilege";
import ids from "../ids";

import { build, getMessage } from "@cyverse-de/ui-lib";
import { MenuItem } from "@material-ui/core";

function MemberPermissions(parentId) {
    return [
        <MenuItem
            value={Privilege.ADMIN.value}
            id={build(parentId, ids.EDIT_TEAM.ADMIN)}
            key={build(parentId, ids.EDIT_TEAM.ADMIN)}
        >
            {getMessage("adminPrivilege")}
        </MenuItem>,
        <MenuItem
            value={Privilege.READOPTIN.value}
            id={build(parentId, ids.EDIT_TEAM.READ_OPT_IN)}
            key={build(parentId, ids.EDIT_TEAM.READ_OPT_IN)}
        >
            {getMessage("readOptinPrivilege")}
        </MenuItem>,
        <MenuItem
            value={Privilege.READ.value}
            id={build(parentId, ids.EDIT_TEAM.READ)}
            key={build(parentId, ids.EDIT_TEAM.READ)}
        >
            {getMessage("readPrivilege")}
        </MenuItem>,
    ];
}

function AllPermissions(parentId) {
    return MemberPermissions(parentId).concat([
        <MenuItem
            value={Privilege.OPTIN.value}
            id={build(parentId, ids.EDIT_TEAM.OPT_IN)}
            key={build(parentId, ids.EDIT_TEAM.OPT_IN)}
        >
            {getMessage("optinPrivilege")}
        </MenuItem>,
        <MenuItem
            value={Privilege.VIEW.value}
            id={build(parentId, ids.EDIT_TEAM.VIEW)}
            key={build(parentId, ids.EDIT_TEAM.VIEW)}
        >
            {getMessage("viewPrivilege")}
        </MenuItem>,
        <MenuItem
            value=""
            id={build(parentId, ids.EDIT_TEAM.NONE)}
            key={build(parentId, ids.EDIT_TEAM.NONE)}
        >
            <em>{getMessage("none")}</em>
        </MenuItem>,
    ]);
}

export { MemberPermissions, AllPermissions };
