import clsx from 'clsx';
import Button from '~/components/Button/Button';
import styles from './Menu.module.scss';

function MenuItem({ data, onClick }) {
  return (
    <Button
      className={clsx(styles.menuItem, { [styles.separate]: data.separate })}
      leftIcon={data.icon}
      to={data.to}
      onClick={onClick}
    >
      {data.title}
    </Button>
  );
}

export default MenuItem;
