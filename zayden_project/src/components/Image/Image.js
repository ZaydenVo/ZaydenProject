import images from '~/assets/images';
import styles from './Image.module.scss';
import clsx from 'clsx';
import { useEffect, useState } from 'react';

function Image({
  src,
  alt,
  fallback: customFallback = images.noImage,
  className,
  ...props
}) {
  const [fallback, setFallback] = useState('');

  useEffect(() => {
    setFallback('');
  }, [src]);

  const handleError = () => {
    setFallback(customFallback);
  };

  const validSrc = src || customFallback;

  return (
    <img
      className={clsx(className, styles.wrapper)}
      src={fallback || validSrc}
      alt={alt}
      {...props}
      onError={handleError}
    />
  );
}

export default Image;
