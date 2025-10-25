import httpRequest from '~/utils/httpRequest';

const authHeader = () => ({
  Authorization: `Bearer ${localStorage.getItem('token')}`,
});

export const friendApi = {
  checkStatus: async (userId) => {
    try {
      const res = await httpRequest(`friend/status/${userId}`, {
        headers: authHeader(),
      });
      return res.data;
    } catch (err) {
      throw err;
    }
  },

  addFriend: async (friendId) => {
    try {
      const res = await httpRequest.post(
        'friend/addFriend',
        { friendId },
        { headers: authHeader() },
      );
      return res.data;
    } catch (err) {
      throw err;
    }
  },

  acceptFriend: async (friendId) => {
    try {
      const res = await httpRequest.post(
        'friend/acceptFriend',
        { senderId: friendId },
        { headers: authHeader() },
      );
      return res.data;
    } catch (err) {
      throw err;
    }
  },

  cancelRequest: async (friendId) => {
    try {
      const res = await httpRequest.delete(`friend/cancel/${friendId}`, {
        headers: authHeader(),
      });
      return res.data;
    } catch (err) {
      throw err;
    }
  },

  rejectRequest: async (friendId) => {
    try {
      const res = await httpRequest.delete(`friend/reject/${friendId}`, {
        headers: authHeader(),
      });
      return res.data;
    } catch (err) {
      throw err;
    }
  },

  unfriend: async (friendId) => {
    try {
      const res = await httpRequest.delete(`friend/unfriend/${friendId}`, {
        headers: authHeader(),
      });
      return res.data;
    } catch (err) {
      throw err;
    }
  },
};
