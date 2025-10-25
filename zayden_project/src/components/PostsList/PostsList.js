import { useState, useRef, useEffect } from 'react';
import httpRequest from '~/utils/httpRequest';
import { PostItem } from '~/components/PostItem';
import styles from './PostsList.module.scss';

function PostsList({ fetchPosts, deletePost }) {
  const [posts, setPosts] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [hasMore, setHasMore] = useState(true);
  const [page, setPage] = useState(1);
  const loaderRef = useRef(null);
  const didInit = useRef(false);

  const pageRef = useRef(1);
  const isLoadingRef = useRef(false);

  pageRef.current = page;

  const getPosts = async (pageNumber) => {
    if (isLoadingRef.current || !hasMore) return;
    setIsLoading(true);
    isLoadingRef.current = true;

    try {
      const newPosts = await fetchPosts(pageNumber);

      if (newPosts.length === 0) {
        setHasMore(false);
      } else {
        setPosts((prev) => [...prev, ...newPosts]);
        setPage((prev) => prev + 1);
      }
    } catch (err) {
      console.log('Error loading older posts: ', err);
    } finally {
      setIsLoading(false);
      isLoadingRef.current = false;
    }
  };

  useEffect(() => {
    if (didInit.current) return;
    didInit.current = true;
    getPosts(1);
  }, []);

  useEffect(() => {
    if (!loaderRef.current || posts.length === 0) return;

    const observer = new IntersectionObserver(
      (entries) => {
        const [entry] = entries;
        if (entry.isIntersecting && hasMore && !isLoadingRef.current) {
          getPosts(pageRef.current);
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
  }, [posts, hasMore]);

  const handleDeletePost = async (postId) => {
    try {
      const res = await httpRequest.delete(`/post/${postId}`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`,
        },
      });
      alert(res.data.result);
      setPosts((prev) => prev.filter((post) => post.id !== postId));
    } catch (err) {
      console.log(err);
    }
  };

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
          onDelete={() => handleDeletePost(post.id)}
          deletePost={deletePost}
        />
      ))}
      <div ref={loaderRef} className={styles.loading}>
        {isLoading && <p className={styles.loading}>Loading...</p>}
      </div>
    </div>
  );
}

export default PostsList;
