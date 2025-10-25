import PostsList from '~/components/PostsList/PostsList';
import httpRequest from '~/utils/httpRequest';

function MyPost() {
  const fetchFollowingPosts = async (pageNumber) => {
    const res = await httpRequest.get(
      `post/my-posts?page=${pageNumber}&size=5`,
      {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`,
        },
      },
    );
    console.log(res);
    return res.data.result.data;
  };

  return <PostsList deletePost fetchPosts={fetchFollowingPosts} />;
}

export default MyPost;
