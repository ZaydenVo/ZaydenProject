import Tippy from '@tippyjs/react/headless';
import styles from './Menu.module.scss';
import { useState } from 'react';
import MenuItem from './MenuItem';
import { Wrapper as PopperWrapper } from '~/components/Popper';
import Header from './Header';

const defaultFn = () => {};

function Menu({
  children,
  items = [],
  hideOnClick = false,
  onChange = defaultFn,
}) {
  const [history, setHistory] = useState([{ data: items }]);

  const current = history[history.length - 1];

  const renderItems = () => {
    return current.data.map((item, index) => {
      const isParent = !!item.children;

      return (
        <MenuItem
          key={index}
          data={item}
          onClick={() => {
            if (isParent) {
              setHistory((prev) => [...prev, item.children]);
            } else {
              onChange(item);
            }
          }}
        />
      );
    });
  };

  return (
    <Tippy
      trigger="click"
      onClickOutside={(instance) => instance.hide()}
      interactive
      offset={[16, 10]}
      delay={[0, 700]}
      hideOnClick={hideOnClick}
      placement="bottom-end"
      render={(attrs) => (
        <div className={styles.content} tabIndex="-1" {...attrs}>
          <PopperWrapper>
            {history.length > 1 && (
              <Header
                title={current.title}
                onBack={() => {
                  setHistory((prev) => prev.slice(0, prev.length - 1));
                }}
              />
            )}
            <div className={styles.menuBody}>{renderItems()}</div>
          </PopperWrapper>
        </div>
      )}
      onHide={() => setHistory((prev) => prev.slice(0, 1))}
    >
      {children}
    </Tippy>
  );
}

export default Menu;
