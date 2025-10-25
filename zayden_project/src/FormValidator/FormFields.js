const fields = [
  { name: 'username', label: 'Username', placeholder: 'Enter username' },
  {
    name: 'password',
    label: 'Password',
    type: 'password',
    placeholder: 'Enter password',
  },
  {
    name: 'confirmPassword',
    label: 'Confirm password',
    type: 'password',
    placeholder: 'Confirm password',
  },
  { name: 'firstName', label: 'First name', placeholder: 'Enter first name' },
  { name: 'lastName', label: 'Last name', placeholder: 'Enter last name' },
  { name: 'dob', label: 'Day of birth', type: 'date' },
  { name: 'email', label: 'Email', placeholder: 'Enter email' },
  {
    name: 'city',
    label: 'City',
    type: 'select',
    options: [
      { label: 'Ha Noi', value: 'hni' },
      { label: 'Ho Chi Minh', value: 'hcm' },
    ],
  },
];

export default fields;
