import { UserInfoProvider } from './UserInfoProvider';
import { SocketProvider } from './SocketProvider';

function AppProvider({ children }) {
  return (
    <UserInfoProvider>
      <SocketProvider>{children}</SocketProvider>
    </UserInfoProvider>
  );
}

export default AppProvider;
