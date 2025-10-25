import { NavLink } from 'react-router-dom';
import clsx from 'clsx';
import styles from './MenuItem.module.scss';

function MenuItem({ title, to, icon, className }) {
  return (
    <NavLink
      className={(nav) =>
        clsx(styles.menuItem, className, { [styles.active]: nav.isActive })
      }
      to={to}
    >
      {icon}
      <span className={styles.title}>{title}</span>
    </NavLink>
  );
}

export default MenuItem;
