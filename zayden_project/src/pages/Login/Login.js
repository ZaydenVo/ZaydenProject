import { useNavigate } from 'react-router-dom';
import { useContext } from 'react';
import { useFormCustom } from '~/hooks';
import images from '~/assets/images';
import styles from './Login.module.scss';
import { Image } from '~/components/Image';
import Button from '~/components/Button/Button';
import httpRequest from '~/utils/httpRequest';
import { UserInfoContext } from '~/Provider/UserInfoProvider';

function Login() {
  const { setUserInfo } = useContext(UserInfoContext);
  const navigate = useNavigate();
  const initialValues = { username: '', password: '' };

  const validate = (values) => {
    const errors = {};

    if (!values.username.trim()) {
      errors.username = 'Username is required!';
    }
    if (!values.password) {
      errors.password = 'Password is required!';
    }
    return errors;
  };

  const handleLogin = async () => {
    var response;
    try {
      response = await httpRequest.post('identity/auth/token', {
        username: values.username,
        password: values.password,
      });
      const token = response.data.result.token;
      localStorage.setItem('token', token);

      try {
        const infoResponse = await httpRequest.get('profile/users/myInfo', {
          headers: { Authorization: `Bearer ${response.data.result.token}` },
        });
        setUserInfo(infoResponse.data.result);
        localStorage.setItem(
          'userInfo',
          JSON.stringify(infoResponse.data.result),
        );
      } catch (error) {
        console.log(error);
      }

      navigate('/');
    } catch (error) {
      if (error.response && error.response.data) {
        const errorMessage = error.response.data.message || 'Please try again!';
        alert(errorMessage);
      } else {
        alert('Please try again later!');
      }
      return;
    }
  };

  const { values, errors, handleChange, handleSubmit } = useFormCustom(
    initialValues,
    validate,
    handleLogin,
  );

  return (
    <div className={styles.loginPage}>
      <Image src={images.logo1} alt="loginlogo" className={styles.logo} />
      <form onSubmit={handleSubmit} className={styles.form}>
        <h3 className={styles.header}>Log in</h3>
        <div className={styles.field}>
          <label className={styles.fieldTitle}>Username</label>
          <input
            name="username"
            value={values.username}
            className={styles.input}
            type="text"
            placeholder="Enter username"
            onChange={handleChange}
          />
          {!!errors.username && (
            <span className={styles.formMessage}>{errors.username}</span>
          )}
        </div>
        <div className={styles.field}>
          <label className={styles.fieldTitle}>Password</label>
          <input
            name="password"
            value={values.password}
            className={styles.input}
            type="password"
            placeholder="Enter password"
            onChange={handleChange}
          />
          {!!errors.password && (
            <span className={styles.formMessage}>{errors.password}</span>
          )}
        </div>
        <div className={styles.button}>
          <Button type="submit" className={styles.button1} primary>
            Log in
          </Button>
          <Button className={styles.button2} outline to="/signup">
            Sign up
          </Button>
        </div>
      </form>
    </div>
  );
}

export default Login;
