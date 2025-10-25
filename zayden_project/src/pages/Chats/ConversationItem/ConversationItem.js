import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import styles from './ConversationItem.module.scss';
import { Image } from '~/components/Image';

function ConversationItem({ data, onClick, unreadCount }) {
  return (
    <div className={styles.wrapper} onClick={onClick}>
      <Image
        className={styles.avatar}
        src={data.conversationAvatar}
        alt={data.avatar}
      />
      <div className={styles.info}>
        <span className={styles.conversationName}>{data.conversationName}</span>
        {unreadCount > 0 && (
          <span className={styles.unreadBadge}>{unreadCount}</span>
        )}
      </div>
    </div>
  );
}

export default ConversationItem;
