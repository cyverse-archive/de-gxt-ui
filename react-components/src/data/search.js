import SearchFormBase from './search/SearchFormBase';
import SaveSearchButtonBase from './search/SaveSearchButtonBase';
import {hasI18N} from '../util/hasI18N';
import messages from './search/messages';
import {withStoreProvider} from "../util/withStoreProvider";

const SearchForm = withStoreProvider(hasI18N(SearchFormBase, messages));
const SaveSearchButton = hasI18N(SaveSearchButtonBase, messages);

export {
    SearchForm,
    SaveSearchButton
};
