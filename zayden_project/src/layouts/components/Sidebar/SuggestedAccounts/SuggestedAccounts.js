import AccountItem from './AccountItem';
import styles from './SuggestedAccounts.module.scss';

function SuggestedAccounts({ label, accountsInfo, showPreview }) {
  return (
    <div className={styles.wrapper}>
      <p className={styles.label}>{label}</p>
      {accountsInfo.map((accountInfo) => (
        <AccountItem
          key={accountInfo.id}
          showPreview={showPreview}
          accountInfo={accountInfo}
        />
      ))}
      {accountsInfo.length > 0 && <p className={styles.moreBtn}>See all</p>}
    </div>
  );
}

export default SuggestedAccounts;
