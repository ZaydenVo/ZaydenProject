import { PostItem } from '~/components/PostItem';
import styles from './Home.module.scss';
import { useEffect, useRef, useState } from 'react';
import httpRequest from '~/utils/httpRequest';

function Home() {
  const [posts, setPosts] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [hasMore, setHasMore] = useState(true);
  const loaderRef = useRef(null);
  const postsRef = useRef(posts);
  const isLoadingRef = useRef(false);

  postsRef.current = posts;

  useEffect(() => {
    const fetchLastestPost = async () => {
      try {
        const res = await httpRequest.get('/post/lastest', {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
          },
        });
        setPosts(res.data.result);
      } catch (err) {
        console.log('Error fetching lastest post: ', err);
      }
    };
    fetchLastestPost();
  }, []);

  const loadOlderPosts = async () => {
    if (isLoadingRef.current || !hasMore) return;
    const lastPostId = postsRef.current[postsRef.current.length - 1]?.id;
    if (!lastPostId) return;

    try {
      setIsLoading(true);
      isLoadingRef.current = true;
      const res = await httpRequest.get(
        `/post/lastestBefore?beforeId=${lastPostId}`,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
          },
        },
      );

      const newPosts = res.data.result;
      if (newPosts.length === 0) {
        setHasMore(false);
      } else {
        setPosts((prev) => [...prev, ...newPosts]);
      }
    } catch (err) {
      console.log('Error loading older posts: ', err);
    } finally {
      setIsLoading(false);
      isLoadingRef.current = false;
    }
  };

  useEffect(() => {
    if (!loaderRef.current) return;

    const observer = new IntersectionObserver(
      (entries) => {
        const [entry] = entries;
        if (entry.isIntersecting && hasMore && !isLoadingRef.current) {
          loadOlderPosts();
        }
      },
      {
        root: null,
        rootMargin: '0px',
        threshold: 0.1,
      },
    );

    observer.observe(loaderRef.current);

    return () => {
      if (loaderRef.current) observer.unobserve(loaderRef.current);
    };
  }, [loaderRef, hasMore]);

  return (
    <div className={styles.wrapper}>
      {posts.map((post) => (
        <PostItem
          key={post.id}
          username={post.username}
          created={post.created}
          title={post.title}
          file={post.file}
          content={post.content}
        />
      ))}
      <div ref={loaderRef} className={styles.loading}>
        {isLoading && <p className={styles.loading}>Loading...</p>}
      </div>
    </div>
  );
}

export default Home;
