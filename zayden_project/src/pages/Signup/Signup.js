import styles from './Signup.module.scss';
import { Image } from '~/components/Image';
import images from '~/assets/images';
import Button from '~/components/Button/Button';
import { useFormCustom } from '~/hooks';
import { FieldInput } from '~/components/FieldInput';
import httpRequest from '~/utils/httpRequest';
import { useNavigate } from 'react-router-dom';
import { validate } from '~/FormValidator';
import { fields } from '~/FormValidator';

function Signup() {
  const navigate = useNavigate();

  const initialValues = {
    username: '',
    password: '',
    confirmPassword: '',
    firstName: '',
    lastName: '',
    dob: '',
    email: '',
    city: '',
  };

  const handleSignup = async () => {
    try {
      const response = await httpRequest.post('identity/users/registration', {
        username: values.username,
        password: values.password,
        firstName: values.firstName,
        lastName: values.lastName,
        dob: values.dob,
        email: values.email,
        city: values.city,
      });

      if (response.status === 200 || response.status === 201) {
        alert('Register successful! Please log in!!!');
        navigate('/login');
      }
    } catch (error) {
      if (error.response && error.response.data) {
        const errorMessage = error.response.data.message || 'Please try again!';
        alert(errorMessage);
      } else {
        alert('Please try again later!');
      }
    }
  };

  const { values, errors, handleChange, handleSubmit } = useFormCustom(
    initialValues,
    validate,
    handleSignup,
  );

  return (
    <div className={styles.loginPage}>
      <Image src={images.logo1} alt="loginlogo" className={styles.logo} />
      <form onSubmit={handleSubmit} className={styles.form}>
        <h3 className={styles.header}>Sign up</h3>
        {fields.map((field) => (
          <FieldInput
            key={field.name}
            label={field.label}
            name={field.name}
            type={field.type || 'text'}
            value={values[field.name]}
            error={errors[field.name]}
            onChange={handleChange}
            placeholder={field.placeholder}
            options={field.options}
          />
        ))}
        <Button type="submit" className={styles.button} primary>
          Sign up
        </Button>
      </form>
    </div>
  );
}

export default Signup;
