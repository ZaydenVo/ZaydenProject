import { Image } from '~/components/Image';
import styles from './MessageItem.module.scss';
import clsx from 'clsx';

function MessageItem({ data, conversationAvatar, className }) {
  return (
    <div className={clsx(styles.wrapper, className)}>
      {data.me ? (
        ''
      ) : (
        <Image src={conversationAvatar} className={styles.avatar} />
      )}
      <div className={styles.message}>{data.message}</div>
    </div>
  );
}

export default MessageItem;
