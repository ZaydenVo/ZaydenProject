const validate = (values) => {
  const errors = {};

  if (!values.username.trim()) {
    errors.username = 'Username is required!';
  }
  if (!values.password.trim()) {
    errors.password = 'Password is required!';
  } else if (values.password.length < 6) {
    errors.password = 'Password must be at least 6 characters!';
  }
  if (!values.confirmPassword.trim()) {
    errors.confirmPassword = 'Confirm Password is required!';
  } else if (values.confirmPassword !== values.password) {
    errors.confirmPassword = 'Password do not match!';
  }
  if (!values.dob) {
    errors.dob = 'Date of Birth is required!';
  } else {
    const today = new Date();
    const dob = new Date(values.dob);
    let age = today.getFullYear() - dob.getFullYear();
    const m = today.getMonth() - dob.getMonth();
    if (m < 0 || (m === 0 && today.getDate() < dob.getDate())) {
      age--;
    }
    if (age < 18) {
      errors.dob = 'You must be at least 18 years old!';
    }
  }
  if (!values.email.trim()) {
    errors.email = 'Email is required!';
  } else {
    const emailRegex = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
    if (!emailRegex.test(values.email)) {
      errors.email = 'Invalid email address!';
    }
  }

  return errors;
};

export default validate;
