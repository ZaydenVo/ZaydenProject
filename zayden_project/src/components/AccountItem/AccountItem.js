import styles from './AccountItem.module.scss';
import { Image } from '../Image';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCheckCircle } from '@fortawesome/free-solid-svg-icons';
import { Link } from 'react-router-dom';

function AccountItem({ data, to, onClick }) {
  const Wrapper = to ? Link : 'div';

  return (
    <Wrapper to={to} className={styles.wrapper} onClick={onClick}>
      <Image className={styles.avatar} src={data.avatar} alt={data.avatar} />
      <div className={styles.info}>
        <h4 className={styles.name}>
          <span>{data.firstName + ' ' + data.lastName}</span>
          {data.tick && (
            <FontAwesomeIcon
              className={styles.checkIcon}
              icon={faCheckCircle}
            />
          )}
        </h4>
        <span className={styles.dataname}>{data.username}</span>
      </div>
    </Wrapper>
  );
}

export default AccountItem;
