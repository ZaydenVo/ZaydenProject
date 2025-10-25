import { useState, useEffect, createContext } from 'react';
import httpRequest from '~/utils/httpRequest';

const UserInfoContext = createContext();

function UserInfoProvider({ children }) {
  const [userInfo, setUserInfo] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const storedUser = localStorage.getItem('userInfo');
    const token = localStorage.getItem('token');

    if (storedUser) {
      setUserInfo(JSON.parse(storedUser));
      setIsLoading(false);
    } else if (token) {
      (async () => {
        try {
          const userInfoRes = await httpRequest.get('profile/users/myInfo', {
            headers: { Authorization: `Bearer ${token}` },
          });
          setUserInfo(userInfoRes.data.result);
          localStorage.setItem(
            'userInfo',
            JSON.stringify(userInfoRes.data.result),
          );
        } catch (err) {
          console.log(err);
          localStorage.removeItem('token');
          localStorage.removeItem('userInfo');
        } finally {
          setIsLoading(false);
        }
      })();
    } else {
      setIsLoading(false);
    }
  }, []);

  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('userInfo');
    setUserInfo(null);
  };

  return (
    <UserInfoContext.Provider
      value={{ userInfo, setUserInfo, logout, isLoading }}
    >
      {children}
    </UserInfoContext.Provider>
  );
}

export { UserInfoContext, UserInfoProvider };
