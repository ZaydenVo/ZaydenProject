import { SuggestedAccounts } from './SuggestedAccounts';
import { useEffect, useState } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { MenuItem } from './MenuItem';
import styles from './Sidebar.module.scss';
import {
  faHouseCircleCheck,
  faIdCard,
  faComments,
} from '@fortawesome/free-solid-svg-icons';
import httpRequest from '~/utils/httpRequest';

function Sidebar() {
  const [suggestedAccounts, setSuggestedAccounts] = useState([]);
  const [followingAccounts, setFollowingAccounts] = useState([]);

  useEffect(() => {
    const fetchAccounts = async () => {
      const token = localStorage.getItem('token');
      if (!token) return;

      try {
        const [suggestedRes, followingRes] = await Promise.all([
          httpRequest('friend/suggested', {
            headers: { Authorization: `Bearer ${token}` },
          }),
          httpRequest('friend/following', {
            headers: { Authorization: `Bearer ${token}` },
          }),
        ]);

        setSuggestedAccounts(suggestedRes.data.result);
        setFollowingAccounts(followingRes.data.result);
      } catch (err) {
        console.log(err);
      }
    };
    fetchAccounts();
  }, []);

  return (
    <div>
      <aside className={styles.wrapper}>
        <nav>
          <MenuItem
            title="News feed"
            to="/"
            icon={<FontAwesomeIcon icon={faHouseCircleCheck} />}
          />
          <MenuItem
            title="Profile"
            to="/profile"
            icon={<FontAwesomeIcon icon={faIdCard} />}
          />
          <MenuItem
            title="Chats"
            to="/chats"
            icon={<FontAwesomeIcon icon={faComments} />}
          />
        </nav>
        <SuggestedAccounts
          label="Suggested Accounts"
          showPreview
          accountsInfo={suggestedAccounts}
        />
        <SuggestedAccounts
          label="Following Accounts"
          accountsInfo={followingAccounts}
        />
      </aside>
    </div>
  );
}

export default Sidebar;
