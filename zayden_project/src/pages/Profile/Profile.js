import { useContext, useEffect, useState } from 'react';
import styles from './Profile.module.scss';
import { UserInfoContext } from '~/Provider/UserInfoProvider';
import { Image } from '~/components/Image';
import InfoItem from './InfoItem';
import Button from '~/components/Button/Button';
import { useNavigate } from 'react-router-dom';
import httpRequest from '~/utils/httpRequest';
import { FieldInput } from '~/components/FieldInput';
import { useFormCustom } from '~/hooks';
import { fields } from '~/FormValidator';

function Profile() {
  const { userInfo, setUserInfo, isLoading } = useContext(UserInfoContext);
  const [selectedAvatar, setSelectedAvatar] = useState(null);
  const [showUpdateForm, setShowUpdateForm] = useState(false);
  const [hideBtn, setHideBtn] = useState(false);
  const navigate = useNavigate();

  const cityMap = {
    hni: 'Ha Noi',
    hcm: 'Ho Chi Minh',
  };

  const initialValues = {
    firstName: '',
    lastName: '',
    dob: '',
    city: '',
  };

  useEffect(() => {
    if (userInfo) {
      setValues({
        firstName: userInfo.firstName || '',
        lastName: userInfo.lastName || '',
        dob: userInfo.dob || '',
        city: userInfo.city || '',
      });
    }
  }, [userInfo]);

  useEffect(() => {
    if (!isLoading && !userInfo && !localStorage.getItem('token'))
      navigate('/login');
  }, [userInfo, isLoading, navigate]);

  const infoItems = userInfo
    ? Object.entries(userInfo)
        .filter(([key]) => !['id', 'userId', 'avatar'].includes(key))
        .map(([key, value]) => ({
          label: key.charAt(0).toUpperCase() + key.slice(1),
          value: key === 'city' ? cityMap[value] || value : value,
        }))
    : [];

  const handleAvatarChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setSelectedAvatar(file);
    }
  };

  const handleSubmitAvatar = async () => {
    if (!selectedAvatar) return alert('Please choose an image!');
    if (!localStorage.getItem('token')) {
      alert('You are not logged in. Please login again!');
      navigate('/login');
      return;
    }

    const formData = new FormData();
    formData.append('file', selectedAvatar);

    try {
      const res = await httpRequest.put('profile/users/avatar', formData, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`,
          'Content-Type': 'multipart/form-data',
        },
      });
      alert('Avatar updated successfully!');

      setUserInfo(res.data.result);
      localStorage.setItem('userInfo', JSON.stringify(res.data.result));
    } catch (err) {
      if (err.response?.status === 401) {
        alert('Session expired. Please log in again!');
        localStorage.removeItem('token');
        navigate('/login');
      } else {
        alert('Failed to update avatar!');
      }
    }
  };

  const handleUpdate = async () => {
    try {
      if (!localStorage.getItem('token')) {
        alert('You are not logged in!');
        navigate('/login');
        return;
      }

      const response = await httpRequest.put(
        'profile/users/my-profile',
        {
          firstName: values.firstName,
          lastName: values.lastName,
          dob: values.dob,
          city: values.city,
        },
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
          },
        },
      );

      if (response.status === 200 || response.status === 201) {
        alert('Profile updated successfully!');
        setUserInfo(response.data.result);
        localStorage.setItem('userInfo', JSON.stringify(response.data.result));
        setShowUpdateForm(false);
      }

      setHideBtn(false);
    } catch (error) {
      if (error.response?.status === 401) {
        alert('Session expired. Please log in again!');
        localStorage.removeItem('token');
        navigate('/login');
      } else if (error.response?.data?.message) {
        alert(error.response.data.message);
      } else {
        alert('Please try again later!');
      }
    }
  };

  const { values, setValues, errors, handleChange, handleSubmit } =
    useFormCustom(initialValues, () => ({}), handleUpdate);

  const updateForm = (
    <form onSubmit={handleSubmit} className={styles.form}>
      {fields
        .filter(
          (field) =>
            !['username', 'password', 'confirmPassword', 'email'].includes(
              field.name,
            ),
        )
        .map((field) => (
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
        Update Profile
      </Button>
    </form>
  );

  return (
    <div className={styles.wrapper}>
      <div className={styles.avatarContainer}>
        <Image
          src={userInfo?.avatar}
          alt="User avatar"
          className={styles.avatar}
        />
        <label className={styles.updateAvtBtn}>Choose new avatar</label>
        <input
          type="file"
          accept="image/*"
          className={styles.fileInput}
          onChange={handleAvatarChange}
        />
        <Button
          outline
          className={styles.submitBtn}
          onClick={handleSubmitAvatar}
        >
          Update Avatar
        </Button>
      </div>
      <div className={styles.userInfo}>
        {showUpdateForm
          ? updateForm
          : infoItems.map((item) => (
              <InfoItem
                key={item.label}
                label={item.label}
                value={item.value}
              />
            ))}
        <Button
          outline
          invisible={hideBtn}
          className={styles.submitBtn}
          onClick={() => {
            setShowUpdateForm(true);
            setHideBtn(true);
          }}
        >
          Update Profile
        </Button>
      </div>
    </div>
  );
}

export default Profile;
