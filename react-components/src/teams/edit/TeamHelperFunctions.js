import Privilege from "../../models/Privilege";

/**
 * Returns the list of privileges the provided subject has based on the given
 * privileges array.
 *
 * @param subjectId - a username
 * @param simplifiedPrivs - an array of privilege objects expected to have a name key with a
 * value from {@link Privilege}, as well as a subject key with
 * a value containing a subject object.
 * @returns {Array|*} containing the privilege types/names, not the privilege objects
 */
function getUserPrivileges(subjectId, simplifiedPrivs) {
    if (!simplifiedPrivs || !simplifiedPrivs.length > 0) {
        return [];
    }

    return simplifiedPrivs.reduce((selfPrivileges, privilege) => {
        if (privilege.subject.id === subjectId) {
            selfPrivileges.push(privilege.name);
        }
        return selfPrivileges;
    }, []);
}

/**
 * The grouper privilege endpoints return the smallest list possible.  If the public user
 * has read permissions, for example, and the team creator assigns read permissions to individual
 * users, those privileges will not be returned from the privileges endpoint since those privileges
 * are already implied by the public user privileges.  This function will add those privileges
 * visually for users to view and modify as needed.
 *
 * @param members - an array of subject objects
 * @param public_privilege - string containing a privilege from {@link Privilege}
 * @param privileges - an array of privilege objects
 */
function getMemberPrivileges(members, public_privilege, privileges) {
    let privIds = privileges.map((priv) => priv.subject.id);
    let memberIds = members.map((member) => member.id);

    let member_privileges = privileges.filter(
        (privilege) => memberIds.indexOf(privilege.subject.id) > -1
    );

    members.forEach((member) => {
        if (privIds.indexOf(member.id) < 0) {
            member_privileges.push({
                name: public_privilege,
                subject: { ...member },
            });
        }
    });

    return member_privileges;
}
/**
 * Returns true if subject exists within members
 * @param subject - a subject object containing at minimum an id key
 * @param members - an array of subject objects
 * @returns {boolean}
 */
function subjectIsMember(subject, members) {
    if (!members || !members.length > 0) {
        return false;
    }

    return members.filter((member) => member.id === subject.id).length > 0;
}

function privilegeNum(privilege) {
    return Privilege[privilege.name.toUpperCase()].num;
}

const READOPTIN_PRIV_LIST = [Privilege.READ.value, Privilege.OPTIN.value];
/**
 * It's possible via the API to assign multiple privileges to the same person, however,
 * it's only logical to need more than one privilege in the case of READ and OPTIN. All
 * other privileges build on top of each other.
 * This function will sort all the privileges and only return the highest privilege
 * and/or transform a set of READ and OPTIN privileges to the fake, singular READOPTIN
 * privilege. This keeps the interface simple for users.
 *
 * @param privileges - an array of privilege objects expected to have a name key with a
 * value from {@link Privilege}, as well as a subject key with
 * a value containing a subject object
 */
function simplifyPrivileges(privileges) {
    // Sorts privileges by asc usernames, then desc privilege type
    let sortedPrivileges = privileges.sort((a, b) => {
        let comp = a.subject.id.localeCompare(b.subject.id);
        return comp !== 0 ? comp : privilegeNum(b) - privilegeNum(a);
    });
    let privilegeMap = {};

    //Creates a map with username keys and privilege values
    sortedPrivileges.reduce((obj, privilege) => {
        let subjectId = privilege.subject.id;
        if (!obj[subjectId]) {
            obj[subjectId] = { ...privilege };
        } else {
            let currentValue = obj[subjectId];
            if (
                READOPTIN_PRIV_LIST.includes(currentValue.name) &&
                READOPTIN_PRIV_LIST.includes(privilege.name)
            ) {
                let readOptInPriv = { ...privilege };
                readOptInPriv.name = Privilege.READOPTIN.value;
                obj[subjectId] = readOptInPriv;
            }
        }
        return obj;
    }, privilegeMap);

    return Object.values(privilegeMap);
}

/**
 * Takes a privilege type String value from {@link Privilege} and turns it into an array.
 * If the privilege type is the fake READOPTIN, it will split it into READ and OPTIN
 * @param privilegeName - string value from {@link Privilege}
 */
const privilegeToPrivilegeList = (privilegeName) => {
    if (privilegeName === Privilege.READOPTIN.value) {
        return READOPTIN_PRIV_LIST;
    } else {
        return privilegeName && privilegeName.length > 0 ? [privilegeName] : [];
    }
};

/**
 * Takes an array of privilege objects and converts it to a privilege update request as expected
 * by the backend. An empty array of privileges will remove all privileges for that user.
 * @param privileges - a list of privilege objects at minimum containing a name key with a value from
 * Privilege.js or PrivilegeType.java, and a subject key containing a subject object
 * @returns {updates: [{subject_id: *, privileges: *}]}
 */
function getUpdateRequest(privileges) {
    let requests = privileges.map((privilege) => {
        return {
            subject_id: privilege.subject.id,
            privileges: privilegeToPrivilegeList(privilege.name),
        };
    });
    return { updates: requests };
}

export {
    getUserPrivileges,
    getMemberPrivileges,
    subjectIsMember,
    simplifyPrivileges,
    privilegeToPrivilegeList,
    getUpdateRequest,
};
