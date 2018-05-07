import BasicDetails from "./details/BasicDetails";
import InfoTypeSelectionList from "./details/InfoTypeSelectionList"
import Tag from "./details/Tag"
import I18N from "../util/I18NWrapper";
import intlData from "../data/messages";

const BasicDetailsWithI18N = I18N(BasicDetails, intlData);

export {
    BasicDetailsWithI18N, InfoTypeSelectionList, Tag
};
