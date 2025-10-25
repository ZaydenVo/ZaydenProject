import styles from './FieldInput.module.scss';

function FieldInput({
  label,
  name,
  value,
  type = 'text',
  error,
  onChange,
  placeholder,
  options,
}) {
  return (
    <div className={styles.field}>
      <label className={styles.fieldTitle}>{label}</label>
      {type === 'select' ? (
        <select
          name={name}
          value={value}
          onChange={onChange}
          className={styles.select}
        >
          <option value="" className={styles.defaultSelect}>
            ----- Select {label} -----
          </option>
          {options.map((opt) => (
            <option key={opt.value} value={opt.value}>
              {opt.label}
            </option>
          ))}
        </select>
      ) : (
        <input
          name={name}
          value={value}
          className={styles.input}
          type={type}
          placeholder={placeholder}
          onChange={onChange}
        />
      )}

      {!!error && <span className={styles.formMessage}>{error}</span>}
    </div>
  );
}

export default FieldInput;
