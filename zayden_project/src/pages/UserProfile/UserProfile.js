import { useEffect, useState } from 'react';
import styles from './UserProfile.module.scss';
import { useParams } from 'react-router-dom';
import httpRequest from '~/utils/httpRequest';
import { Image } from '~/components/Image';
import InfoItem from '../Profile/InfoItem';
import Button from '~/components/Button/Button';
import { friendApi } from '~/services';

function UserProfile() {
  const { username } = useParams();
  const [userInfo, setUserInfo] = useState(null);
  const [friendStatus, setFriendStatus] = useState('NONE');

  const cityMap = {
    hni: 'Ha Noi',
    hcm: 'Ho Chi Minh',
  };

  useEffect(() => {
    const fetchUserProfile = async () => {
      try {
        const res = await httpRequest.get(
          `/profile/users/profile/${username}`,
          {
            headers: {
              Authorization: `Bearer ${localStorage.getItem('token')}`,
            },
          },
        );
        setUserInfo(res.data.result);
      } catch (err) {
        console.log(err);
      }
    };
    fetchUserProfile();
  }, [username]);

  const infoItems = userInfo
    ? Object.entries(userInfo)
        .filter(([key]) => !['id', 'userId', 'avatar'].includes(key))
        .map(([key, value]) => ({
          label: key.charAt(0).toUpperCase() + key.slice(1),
          value: key === 'city' ? cityMap[value] || value : value,
        }))
    : [];

  useEffect(() => {
    const getFriendStatus = async () => {
      try {
        if (!userInfo?.userId) return;
        const res = await friendApi.checkStatus(userInfo.userId);

        const statusMap = res.result;

        if (!statusMap || !statusMap.status) {
          setFriendStatus('NONE');
          return;
        }

        if (
          statusMap.status === 'PENDING' &&
          statusMap.senderId === userInfo.userId
        )
          setFriendStatus('PENDING_RECEIVED');
        else if (statusMap.status === 'PENDING')
          setFriendStatus('PENDING_SENT');
        else setFriendStatus(statusMap.status);
      } catch (err) {
        console.log(err);
      }
    };
    getFriendStatus();
  }, [username, userInfo]);

  const handleAddFriend = async (friendId) => {
    try {
      await friendApi.addFriend(friendId);
      alert('Friend request sent successfully!');
      setFriendStatus('PENDING_SENT');
    } catch (err) {
      console.log(err);
      alert(err.response?.data?.message || 'Something went wrong!');
    }
  };

  const handleAcceptFriend = async (friendId) => {
    try {
      await friendApi.acceptFriend(friendId);
      alert('Friend request accepted successfully!');
      setFriendStatus('FRIEND');
    } catch (err) {
      console.log(err);
      alert(err.response?.data?.message || 'Something went wrong!');
    }
  };

  const handleCancelRequest = async (friendId) => {
    try {
      const res = await friendApi.cancelRequest(friendId);
      alert(res.result);
      setFriendStatus('NONE');
    } catch (err) {
      console.log(err);
      alert(err.response?.data?.message || 'Something went wrong!');
    }
  };

  const handleRejectRequest = async (friendId) => {
    try {
      const res = await friendApi.rejectRequest(friendId);
      alert(res.result);
      setFriendStatus('NONE');
    } catch (err) {
      console.log(err);
      alert(err.response?.data?.message || 'Something went wrong!');
    }
  };

  const handleUnfriendRequest = async (friendId) => {
    try {
      const res = await friendApi.unfriend(friendId);
      alert(res.result);
      setFriendStatus('NONE');
    } catch (err) {
      console.log(err);
      alert(err.response?.data?.message || 'Something went wrong!');
    }
  };

  const friendActionBtn = () => {
    switch (friendStatus) {
      case 'NONE':
        return (
          <Button
            className={styles.FriendActionBtn}
            outline
            onClick={() => handleAddFriend(userInfo.userId)}
          >
            Add Friend
          </Button>
        );
      case 'PENDING_SENT':
        return (
          <Button
            className={styles.FriendActionBtn}
            outline
            onClick={() => handleCancelRequest(userInfo.userId)}
          >
            Cancel Request
          </Button>
        );
      case 'PENDING_RECEIVED':
        return (
          <>
            <Button
              className={styles.FriendActionBtn}
              outline
              onClick={() => handleAcceptFriend(userInfo.userId)}
            >
              Accept Request
            </Button>
            <Button
              className={styles.FriendActionBtn}
              outline
              onClick={() => handleRejectRequest(userInfo.userId)}
            >
              Reject Request
            </Button>
          </>
        );
      case 'FRIEND':
        return (
          <Button
            className={styles.FriendActionBtn}
            outline
            onClick={() => handleUnfriendRequest(userInfo.userId)}
          >
            Unfriend
          </Button>
        );
      default:
        return null;
    }
  };

  return (
    <div className={styles.wrapper}>
      <Image
        src={userInfo?.avatar}
        alt="User avatar"
        className={styles.avatar}
      />
      <div className={styles.userInfo}>
        {infoItems.map((item) => (
          <InfoItem key={item.label} label={item.label} value={item.value} />
        ))}
        {friendActionBtn()}
      </div>
    </div>
  );
}

export default UserProfile;
