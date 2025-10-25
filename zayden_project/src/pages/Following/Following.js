import PostsList from '~/components/PostsList/PostsList';
import httpRequest from '~/utils/httpRequest';

function Following() {
  const fetchFollowingPosts = async (pageNumber) => {
    const res = await httpRequest.get(
      `post/followingPosts?page=${pageNumber}&size=5`,
      {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`,
        },
      },
    );
    console.log(res);
    return res.data.result.data;
  };

  return <PostsList fetchPosts={fetchFollowingPosts} />;
}

export default Following;
