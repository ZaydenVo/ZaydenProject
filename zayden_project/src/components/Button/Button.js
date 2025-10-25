import { Link } from 'react-router-dom';
import clsx from 'clsx';
import styles from './Button.module.scss';

function Button({
  className,
  to,
  href,
  leftIcon,
  primary,
  outline,
  text,
  rounded,
  disabled,
  invisible = false,
  small,
  large,
  children,
  onClick,
  ...passProps
}) {
  let Comp = 'button';
  const props = { onClick, ...passProps };

  if (disabled) {
    Object.keys(props).forEach((key) => {
      if (key.startsWith('on') && typeof props[key] === 'function') {
        delete props[key];
      }
    });
  }

  if (to) {
    props.to = to;
    Comp = Link;
  } else if (href) {
    props.href = href;
    Comp = 'a';
  }

  const classes = clsx(styles.wrapper, className, {
    [styles.primary]: primary,
    [styles.outline]: outline,
    [styles.small]: small,
    [styles.large]: large,
    [styles.text]: text,
    [styles.disabled]: disabled,
    [styles.rounded]: rounded,
    [styles.leftIcon]: leftIcon,
    [styles.invisible]: invisible,
  });

  return (
    <Comp className={classes} {...props}>
      {leftIcon && <span className={styles.leftIcon}>{leftIcon}</span>}
      <span>{children}</span>
    </Comp>
  );
}

export default Button;
