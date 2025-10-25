import styles from './AccountPreview.module.scss';
import Button from '~/components/Button/Button';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCheckCircle } from '@fortawesome/free-solid-svg-icons';
import { Image } from '~/components/Image';
import { friendApi } from '~/services';

function AccountPreview({ accountInfo }) {
  const handleAddFriend = async (friendId) => {
    try {
      await friendApi.addFriend(friendId);
      alert('Friend request sent successfully!');
    } catch (err) {
      console.log(err);
      alert(err.response?.data?.message || 'Something went wrong!');
    }
  };

  return (
    <div className={styles.wrapper}>
      <header className={styles.header}>
        <Image className={styles.avatar} src={accountInfo.avatar} alt="" />
        <Button
          className={styles.followBtn}
          primary
          onClick={() => handleAddFriend(accountInfo.userId)}
        >
          Add Friend
        </Button>
      </header>
      <div className={styles.body}>
        <p className={styles.nickName}>
          <strong>{accountInfo.username}</strong>
          <FontAwesomeIcon className={styles.check} icon={faCheckCircle} />
        </p>
        <p
          className={styles.fullname}
        >{`${accountInfo.firstName} ${accountInfo.lastName}`}</p>
        <p className={styles.analytics}>
          <strong className={styles.value}>9.2M </strong>
          <span className={styles.label}>Followers</span>
          <strong className={styles.value}>11.2M </strong>
          <span className={styles.label}>Likes</span>
        </p>
      </div>
    </div>
  );
}

export default AccountPreview;
