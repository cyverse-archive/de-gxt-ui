/**
 @author sriram
 */
export default function build(baseID, id) {
    const DOT = ".";
    return baseID ? baseID + DOT + id : id;
}
