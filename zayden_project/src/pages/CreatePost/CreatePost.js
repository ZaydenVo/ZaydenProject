import { useForm } from 'react-hook-form';
import Button from '~/components/Button/Button';
import styles from './CreatePost.module.scss';
import httpRequest from '~/utils/httpRequest';

function CreatePost() {
  const { register, handleSubmit, reset } = useForm();

  const onSubmit = async (data) => {
    const formData = new FormData();

    formData.append('file', data.file?.[0] || '');
    formData.append('title', data.title);
    formData.append('content', data.content);

    try {
      await httpRequest.post('post/create', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
          Authorization: `Bearer ${localStorage.getItem('token')}`,
        },
      });
      alert('Post created!');
      reset();
    } catch (err) {
      alert('Failed to create post!');
    }
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className={styles.wrapper}>
      <div className={styles.field}>
        <label className={styles.label}>Upload file/image</label>
        <input
          type="file"
          accept="image/*"
          {...register('file')}
          className={styles.file}
        />
      </div>
      <div className={styles.field}>
        <label className={styles.label}>Title</label>
        <input
          {...register('title', { required: 'Title is required!' })}
          className={styles.title}
          placeholder="Enter title..."
        />
      </div>
      <div className={styles.field}>
        <label className={styles.label}>Content</label>
        <textarea
          {...register('content', { required: 'Content is required!' })}
          className={styles.content}
          placeholder="Enter content..."
        />
      </div>
      <Button type="submit" outline className={styles.button}>
        Create Post
      </Button>
    </form>
  );
}

export default CreatePost;
