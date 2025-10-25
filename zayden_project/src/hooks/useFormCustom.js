import { useState } from 'react';

function useFormCustom(initialValues, validate, onSubmit) {
  const [values, setValues] = useState(initialValues || {});
  const [errors, setErrors] = useState({});

  const handleChange = (e) => {
    const { name, value } = e.target;
    setValues((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    const validationErrors = validate(values);
    setErrors(validationErrors || {});

    if (Object.keys(validationErrors).length === 0) {
      onSubmit(values);
    }
  };

  return {
    values,
    setValues,
    errors,
    handleChange,
    handleSubmit,
  };
}

export default useFormCustom;
