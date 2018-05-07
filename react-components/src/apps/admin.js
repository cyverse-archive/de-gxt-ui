import AppStats from "./admin/AppStats";
import I18N from "../util/I18NWrapper";
import intlData from "../apps/messages";


const AppStatsWithI18N = I18N(AppStats, intlData);
export {
    AppStatsWithI18N,
};
