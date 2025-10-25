import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPaperPlane, faTrash } from '@fortawesome/free-solid-svg-icons';
import { useContext, useEffect, useRef, useState } from 'react';
import clsx from 'clsx';
import styles from './Chats.module.scss';
import { AccountItem } from '~/components/AccountItem';
import { useDebounce } from '~/hooks';
import httpRequest from '~/utils/httpRequest';
import { ConversationItem } from './ConversationItem';
import { MessageItem } from './MessageItem';
import { SocketContext } from '~/Provider/SocketProvider';

function Chats() {
  const [searchValue, setSearchValue] = useState('');
  const [searchResult, setSearchResult] = useState([]);
  const [conversations, setConversations] = useState([]);
  const [chatMessages, setChatMessages] = useState([]);
  const [conversationInfo, setConversationInfo] = useState(null);
  const [unreadCounts, setUnreadCounts] = useState({});
  const [message, setMessage] = useState('');
  const socket = useContext(SocketContext);
  const messagesRef = useRef(null);

  const valueDebounce = useDebounce(searchValue, 500);

  useEffect(() => {
    if (!messagesRef.current) return;
    messagesRef.current.scrollTop = messagesRef.current.scrollHeight;
  }, [chatMessages]);

  useEffect(() => {
    const fetchConversations = async () => {
      try {
        const res = await httpRequest.get(
          'chat/conversations/my-conversation',
          {
            headers: {
              Authorization: `Bearer ${localStorage.getItem('token')}`,
            },
          },
        );
        setConversations(res.data.result);
      } catch (err) {
        console.log(err);
      }
    };
    fetchConversations();
  }, []);

  useEffect(() => {
    if (!valueDebounce.trim()) {
      setSearchResult([]);
      return;
    }

    const fetchSearchResult = async () => {
      try {
        const res = await httpRequest.post('search', {
          text: valueDebounce,
          type: 'users',
        });
        setSearchResult(res.data.result.users);
      } catch (err) {
        console.log(err);
      }
    };
    fetchSearchResult();
  }, [valueDebounce]);

  const handleChange = (e) => {
    setSearchValue(e.target.value);
  };

  const getChatMessages = async (conversationId) => {
    try {
      const res = await httpRequest(
        `chat/message?conversationId=${conversationId}`,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
          },
        },
      );
      const messages = res.data.result;
      setChatMessages(messages);
    } catch (err) {
      console.log(err);
    }
  };

  const handleClick = (participant) => {
    const fetchCreateConversation = async () => {
      try {
        const res = await httpRequest.post(
          'chat/conversations/create',
          {
            type: 'PRIVATE',
            participantIds: [participant.userId],
          },
          {
            headers: {
              Authorization: `Bearer ${localStorage.getItem('token')}`,
            },
          },
        );
        const conversation = res.data.result;
        setConversations((prev) => [...prev, conversation]);
        setConversationInfo(conversation);
        getChatMessages(conversation.id);
        setSearchValue('');
      } catch (err) {
        console.log(err);
      }
    };
    fetchCreateConversation();
  };

  const handleGetChatMessages = (conversation) => {
    setConversationInfo(conversation);
    getChatMessages(conversation.id);

    setUnreadCounts((prev) => ({
      ...prev,
      [conversation.id]: 0,
    }));
  };

  const handleMessage = (e) => {
    setMessage(e.target.value);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!conversationInfo?.id || !message.trim()) return;
    try {
      await httpRequest.post(
        'chat/message/create',
        {
          conversationId: conversationInfo.id,
          message: message,
        },
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
          },
        },
      );
      setMessage('');
    } catch (err) {
      console.log(err);
    }
  };

  useEffect(() => {
    if (!socket) return;

    const handleMessage = (chatMessage) => {
      const parsedMessage =
        typeof chatMessage === 'string' ? JSON.parse(chatMessage) : chatMessage;

      if (parsedMessage.conversationId === conversationInfo?.id) {
        setChatMessages((prev) => [parsedMessage, ...prev]);
      } else {
        setUnreadCounts((prev) => ({
          ...prev,
          [parsedMessage.conversationId]:
            (prev[parsedMessage.conversationId] || 0) + 1,
        }));
      }
    };

    socket.on('message', handleMessage);

    return () => {
      socket.off('message', handleMessage);
    };
  }, [socket, conversationInfo]);

  const handleDelete = async () => {
    if (!window.confirm('Are you sure you want to delete this conversation?'))
      return;
    try {
      const res = await httpRequest.delete(
        `chat/conversations/delete/${conversationInfo.id}`,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
          },
        },
      );
      alert(res.data.result);
      setConversations((prev) =>
        prev.filter((p) => p.id !== conversationInfo.id),
      );
      setConversationInfo(null);
    } catch (err) {
      console.log(err);
    }
  };

  return (
    <div className={styles.wrapper}>
      <aside className={styles.conversations}>
        <header className={styles.header}>Conversations</header>
        <div className={styles.searchConversations}>
          <input
            value={searchValue}
            className={styles.search}
            placeholder="Search Conversation"
            onChange={handleChange}
          />
        </div>
        {searchResult.length > 0 ? (
          searchResult.map((result) => (
            <AccountItem
              key={result.id}
              data={result}
              onClick={() => handleClick(result)}
            />
          ))
        ) : (
          <ul className={styles.conversationItems}>
            {conversations.map((conversation) => (
              <ConversationItem
                key={conversation.id}
                data={conversation}
                unreadCount={unreadCounts[conversation.id] || 0}
                onClick={() => handleGetChatMessages(conversation)}
              />
            ))}
          </ul>
        )}
      </aside>
      {conversationInfo ? (
        <main className={styles.chatBlock}>
          <header className={styles.conversationInfo}>
            <ConversationItem data={conversationInfo} />
            <FontAwesomeIcon
              icon={faTrash}
              className={styles.deleteBtn}
              onClick={handleDelete}
            />
          </header>
          <section className={styles.messages}>
            {chatMessages.map((message) => (
              <MessageItem
                key={message.id}
                data={message}
                conversationAvatar={conversationInfo.conversationAvatar}
                className={clsx(styles.message, {
                  [styles.me]: message.me,
                  [styles.other]: !message.me,
                })}
              />
            ))}
            <div ref={messagesRef} />
          </section>
          <form className={styles.chatFooter} onSubmit={handleSubmit}>
            <textarea
              value={message}
              className={styles.messageInput}
              placeholder="Enter message..."
              onChange={handleMessage}
              onKeyDown={(e) => {
                if (e.key === 'Enter' && !e.shiftKey) {
                  e.preventDefault();
                  handleSubmit(e);
                }
              }}
            ></textarea>
            <button type="submit" className={styles.sendBtn}>
              <FontAwesomeIcon icon={faPaperPlane} />
            </button>
          </form>
        </main>
      ) : (
        ''
      )}
    </div>
  );
}

export default Chats;
