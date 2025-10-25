import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import HeadlessTippy from '@tippyjs/react/headless';
import styles from './Search.module.scss';
import {
  faCircleXmark,
  faMagnifyingGlass,
  faSpinner,
} from '@fortawesome/free-solid-svg-icons';
import { Wrapper as PopperWrapper } from '~/components/Popper';
import { useEffect, useRef, useState } from 'react';
import { AccountItem } from '~/components/AccountItem';
import { useDebounce } from '~/hooks';
import httpRequest from '~/utils/httpRequest';

function Search() {
  const [searchValue, setSearchValue] = useState('');
  const [searchResult, setSearchResult] = useState([]);
  const [showResult, setShowResult] = useState(false);
  const [loading, setLoading] = useState(false);

  const inputRef = useRef();
  const valueDebounce = useDebounce(searchValue, 500);

  useEffect(() => {
    if (!valueDebounce.trim()) {
      setSearchResult([]);
      return;
    }

    const results = async () => {
      setLoading(true);

      try {
        const response = await httpRequest.post('search', {
          text: valueDebounce,
          type: 'users',
        });
        setSearchResult(response.data.result.users);
      } catch (error) {
        console.log(error);
      }

      setLoading(false);
    };

    results();
  }, [valueDebounce]);

  const handleClear = () => {
    setSearchValue('');
    setSearchResult([]);
    inputRef.current.focus();
  };

  const handleChange = (e) => {
    const searchValue = e.target.value;
    if (!searchValue.startsWith(' ') || searchValue.trim()) {
      setSearchValue(searchValue);
    }
  };

  const handleHideResult = () => {
    setShowResult(false);
  };

  return (
    <HeadlessTippy
      interactive={true}
      appendTo={document.body}
      visible={showResult && searchResult.length > 0}
      render={(attrs) => (
        <div className={styles.searchResult} tabIndex="-1" {...attrs}>
          <PopperWrapper>
            <h4 className={styles.searchTitle}>Accounts</h4>
            {searchResult.map((result) => (
              <AccountItem
                to={`/profile/${result.username}`}
                key={result.id}
                data={result}
              />
            ))}
          </PopperWrapper>
        </div>
      )}
      onClickOutside={handleHideResult}
    >
      <div className={styles.search}>
        <button className={styles.searchBtn}>
          <FontAwesomeIcon icon={faMagnifyingGlass} />
        </button>
        <input
          ref={inputRef}
          value={searchValue}
          placeholder="Search"
          spellCheck={false}
          onChange={handleChange}
          onFocus={() => setShowResult(true)}
        />
        {!!searchValue && !loading && (
          <button className={styles.clear} onClick={handleClear}>
            <FontAwesomeIcon icon={faCircleXmark} />
          </button>
        )}
        {loading && (
          <FontAwesomeIcon className={styles.loading} icon={faSpinner} />
        )}
      </div>
    </HeadlessTippy>
  );
}

export default Search;
