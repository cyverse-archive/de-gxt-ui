import AppFields from "./AppFields";

const desc = (a, b, orderBy) => {
    if (
        orderBy === AppFields.NAME.key ||
        orderBy === AppFields.INTEGRATOR.key ||
        orderBy === AppFields.SYSTEM.key
    ) {
        if (b[`${orderBy}`] < a[`${orderBy}`]) {
            return -1;
        }
        if (b[`${orderBy}`] > a[`${orderBy}`]) {
            return 1;
        }
        return 0;
    }
    if (orderBy === AppFields.RATING.key) {
        if (b.rating.average < a.rating.average) {
            return -1;
        }
        if (b.rating.average > a.rating.average) {
            return 1;
        }
        return 0;
    }
};

export default function getAppsSorting(order, orderBy) {
    return order === "desc"
        ? (a, b) => desc(a, b, orderBy)
        : (a, b) => -desc(a, b, orderBy);
}
