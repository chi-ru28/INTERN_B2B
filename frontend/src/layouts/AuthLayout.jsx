import React from 'react';
import { Outlet } from 'react-router-dom';

const AuthLayout = () => {
  return (
    <div style={{ 
      minHeight: '100vh', 
      display: 'flex', 
      alignItems: 'center', 
      justifyContent: 'center',
      background: 'radial-gradient(circle at top right, rgba(99, 102, 241, 0.15), transparent 40%), radial-gradient(circle at bottom left, rgba(16, 185, 129, 0.1), transparent 40%)'
    }}>
      <div style={{ width: '100%', maxWidth: '480px', padding: '2rem' }}>
        <h1 style={{ textAlign: 'center', marginBottom: '2rem', fontSize: '2.5rem' }} className="glow-text">
          B2B.Enterprise
        </h1>
        <Outlet />
      </div>
    </div>
  );
};

export default AuthLayout;
