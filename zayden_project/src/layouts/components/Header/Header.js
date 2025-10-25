import { Link, useNavigate } from 'react-router-dom';
import styles from './Header.module.scss';
import images from '~/assets/images';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
  faArrowRightFromBracket,
  faBell,
  faChevronDown,
  faCircleQuestion,
  faComment,
  faUsersLine,
  faEarthAsia,
  faGear,
  faHouseCircleCheck,
  faUser,
  faUserMinus,
} from '@fortawesome/free-solid-svg-icons';
import Button from '~/components/Button/Button';
import { Search } from '../Search';
import Tippy from '@tippyjs/react';
import { Image } from '~/components/Image';
import { Menu } from '~/components/Popper/Menu';
import httpRequest from '~/utils/httpRequest';
import { useContext } from 'react';
import { UserInfoContext } from '~/Provider/UserInfoProvider';
import { MenuItem } from './MenuItem';
import { SocketContext } from '~/Provider/SocketProvider';

const MENU_ITEMS = [
  {
    icon: <FontAwesomeIcon icon={faUser} />,
    title: 'View profile',
    to: '/profile',
  },
  {
    icon: <FontAwesomeIcon icon={faGear} />,
    title: 'Settings',
    to: '/settings',
  },
  {
    icon: <FontAwesomeIcon icon={faEarthAsia} />,
    title: 'English',
    children: {
      title: 'Language',
      data: [
        {
          type: 'language',
          code: 'en',
          title: 'English',
        },
        {
          type: 'language',
          code: 'vi',
          title: 'Vietnamese',
        },
      ],
    },
  },
  {
    icon: <FontAwesomeIcon icon={faCircleQuestion} />,
    title: 'Feedback and help',
    to: '/feedback',
  },
  {
    type: 'logout',
    icon: <FontAwesomeIcon icon={faArrowRightFromBracket} />,
    title: 'Log out',
    separate: true,
  },
];

function Header() {
  const { logout } = useContext(UserInfoContext);
  const { userInfo } = useContext(UserInfoContext);
  const navigate = useNavigate();
  const socket = useContext(SocketContext);

  const handleLogout = async () => {
    if (socket) {
      socket.disconnect();
    }

    try {
      await httpRequest.post('identity/auth/logout', {
        token: localStorage.getItem('token'),
      });
    } catch (error) {
      console.log(error);
    } finally {
      logout();
      navigate('/login');
    }
  };

  const handleMenuChange = (menuItem) => {
    switch (menuItem.type) {
      case 'language':
        // Handle change language
        break;
      case 'logout':
        handleLogout();
        break;
      default:
    }
  };

  return (
    <header className={styles.wrapper}>
      <div className={styles.inner}>
        <div className={styles.leftBlock}>
          <Link to="/" className={styles.logoLink}>
            <img src={images.logo} alt="Zayden" className={styles.logo} />
          </Link>
          <Search />
        </div>

        <nav className={styles.centerBlock}>
          <MenuItem
            title="Home"
            to="/"
            icon={<FontAwesomeIcon icon={faHouseCircleCheck} />}
          />
          <MenuItem
            title="Following"
            to="/following"
            icon={<FontAwesomeIcon icon={faUsersLine} />}
          />
          <MenuItem
            title="My Post"
            to="/mypost"
            icon={<FontAwesomeIcon icon={faUserMinus} />}
          />
          <span className={styles.indicator}></span>
        </nav>

        <div className={styles.rightBlock}>
          <Button primary to="/createpost">
            Create Post
          </Button>

          <>
            <Tippy content="Messages" placement="bottom" duration={0}>
              <button className={styles.actionBtn}>
                <FontAwesomeIcon icon={faComment} />
              </button>
            </Tippy>
            <Tippy content="Notifications" placement="bottom" duration={0}>
              <button className={styles.actionBtn}>
                <FontAwesomeIcon icon={faBell} />
              </button>
            </Tippy>
          </>

          <Menu items={MENU_ITEMS} onChange={handleMenuChange}>
            <div className={styles.avatarWrapper}>
              <Image
                src={userInfo?.avatar}
                alt="User avatar"
                className={styles.avatar}
              />
              <FontAwesomeIcon
                icon={faChevronDown}
                className={styles.arrowIcon}
              />
            </div>
          </Menu>
        </div>
      </div>
    </header>
  );
}

export default Header;
