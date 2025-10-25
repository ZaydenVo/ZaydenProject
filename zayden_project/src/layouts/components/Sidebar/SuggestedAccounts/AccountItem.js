import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCheckCircle } from '@fortawesome/free-solid-svg-icons';
import styles from './SuggestedAccounts.module.scss';
import { Wrapper as PopperWrapper } from '~/components/Popper';
import { AccountPreview } from './AccountPreview';
import Tippy from '@tippyjs/react/headless';
import { Image } from '~/components/Image';
import { Link } from 'react-router-dom';

function AccountItem({ accountInfo, showPreview = false }) {
  const renderPreview = (props) => (
    <div tabIndex="-1" {...props}>
      <PopperWrapper>
        <div className={styles.preview}>
          <AccountPreview accountInfo={accountInfo} />
        </div>
      </PopperWrapper>
    </div>
  );

  const accountItem = (
    <Link
      to={`/profile/${accountInfo.username}`}
      className={styles.accountItem}
    >
      <Image className={styles.avatar} src={accountInfo.avatar} alt="" />
      <div className={styles.itemInfo}>
        <p className={styles.username}>
          <strong>{accountInfo.username}</strong>
          <FontAwesomeIcon className={styles.check} icon={faCheckCircle} />
        </p>
        <p
          className={styles.fullname}
        >{`${accountInfo.firstName} ${accountInfo.lastName}`}</p>
      </div>
    </Link>
  );

  return showPreview ? (
    <Tippy
      interactive
      delay={[800, 0]}
      offset={[30, 0]}
      render={renderPreview}
      placement="bottom"
    >
      {accountItem}
    </Tippy>
  ) : (
    accountItem
  );
}

export default AccountItem;
