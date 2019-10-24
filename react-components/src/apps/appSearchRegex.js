export default function getAppsSearchRegex(search) {
    return search
        ? new RegExp(search.replace("*", ".*").replace("?", "."), "i")
        : search;
}
