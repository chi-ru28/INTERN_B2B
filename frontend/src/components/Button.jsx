import React from 'react';

const Button = ({ children, variant = 'primary', onClick, style, className = '' }) => {
  const baseClass = `btn btn-${variant} ${className}`;
  
  return (
    <button className={baseClass} onClick={onClick} style={style}>
      {children}
    </button>
  );
};

export default Button;
