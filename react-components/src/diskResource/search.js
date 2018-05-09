import SearchFormBase from './search/SearchFormBase';
import SaveSearchButtonBase from './search/SaveSearchButtonBase';
import {hasI18N} from '../util/hasI18N';
import messages from '../diskResource/search/messages';

const SearchForm = hasI18N(SearchFormBase, messages);
const SaveSearchButton = hasI18N(SaveSearchButtonBase, messages);

export {
    SearchForm,
    SaveSearchButton
};
