import { Home } from '~/pages/Home';
import { Profile } from '~/pages/Profile';
import { Chats } from '~/pages/Chats';
import { CreatePost } from '~/pages/CreatePost';
import { Login } from '~/pages/Login';
import { Signup } from '~/pages/Signup';
import { Following } from '~/pages/Following';
import { MyPost } from '~/pages/MyPost';
import { HeaderOnly } from '~/layouts/HeaderOnly';
import { UserProfile } from '~/pages/UserProfile';

const publicRoutes = [
  { path: '/login', component: Login, layout: null },
  { path: '/signup', component: Signup, layout: null },
];

const privateRoutes = [
  { path: '/', component: Home },
  { path: '/profile', component: Profile },
  { path: '/profile/:username', component: UserProfile },
  { path: '/following', component: Following },
  { path: '/mypost', component: MyPost },
  { path: '/chats', component: Chats, layout: HeaderOnly },
  { path: '/createpost', component: CreatePost },
];

export { publicRoutes, privateRoutes };
