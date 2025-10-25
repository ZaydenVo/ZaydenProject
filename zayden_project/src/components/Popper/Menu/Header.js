import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import styles from './Menu.module.scss';
import { faArrowLeft } from '@fortawesome/free-solid-svg-icons';

function Header({ title, onBack }) {
  return (
    <header className={styles.header}>
      <button className={styles.backBtn} onClick={onBack}>
        <FontAwesomeIcon icon={faArrowLeft} />
      </button>
      <h4 className={styles.headerTitle}>{title}</h4>
    </header>
  );
}

export default Header;
