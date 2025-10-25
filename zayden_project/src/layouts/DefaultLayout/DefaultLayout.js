import styles from './DefaultLayout.module.scss';
import { Header } from '../components/Header';
import { Sidebar } from '../components/Sidebar';

function DefaultLayout({ children }) {
  return (
    <div className={styles.wrapper}>
      <Header />
      <div className={styles.container}>
        <Sidebar />
        <div className={styles.content}>{children}</div>
      </div>
    </div>
  );
}

export default DefaultLayout;
