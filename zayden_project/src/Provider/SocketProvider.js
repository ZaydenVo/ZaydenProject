import { createContext, useEffect, useState } from 'react';
import { io } from 'socket.io-client';

export const SocketContext = createContext();

export const SocketProvider = ({ children }) => {
  const [socket, setSocket] = useState(null);
  const [token, setToken] = useState(localStorage.getItem('token'));

  useEffect(() => {
    const checkToken = setInterval(() => {
      const storedToken = localStorage.getItem('token');
      if (storedToken !== token) {
        setToken(storedToken);
      }
    }, 1000);
    return () => clearInterval(checkToken);
  }, [token]);

  useEffect(() => {
    if (!token) return;

    const newSocket = io(`http://localhost:8099?token=${token}`, {
      transports: ['websocket'],
    });
    setSocket(newSocket);

    return () => newSocket.disconnect();
  }, [token]);

  return (
    <SocketContext.Provider value={socket}>{children}</SocketContext.Provider>
  );
};
