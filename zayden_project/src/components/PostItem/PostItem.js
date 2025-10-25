import { useState } from 'react';
import styles from './PostItem.module.scss';
import Button from '../Button/Button';

function PostItem({
  username,
  created,
  title,
  content,
  file,
  onDelete,
  deletePost = false,
}) {
  const [expanded, setExpanded] = useState(false);

  const MAX_LENGTH = 200;
  const isLong = content.length > MAX_LENGTH;
  const displayedContent = expanded ? content : content.slice(0, MAX_LENGTH);

  return (
    <article className={styles.wrapper}>
      <header>
        <h2 className={styles.title}>{title}</h2>

        <div className={styles.postInfo}>
          <span className={styles.username}>{username}</span>
          <time className={styles.created}>{created}</time>
        </div>
      </header>

      {file && <img className={styles.file} src={file} alt="" />}

      <section className={styles.content}>
        <p>
          {displayedContent}
          {!expanded && isLong && '...'}
        </p>

        {isLong && (
          <button
            className={styles.seeMoreBtn}
            onClick={() => setExpanded(!expanded)}
          >
            {expanded ? 'See less' : 'See more...'}
          </button>
        )}
      </section>
      {deletePost && (
        <Button outline className={styles.deletePost} onClick={onDelete}>
          Delete Post
        </Button>
      )}
    </article>
  );
}

export default PostItem;
