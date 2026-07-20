import React from 'react';
import { Outlet } from 'react-router-dom';
import Navbar from '../components/Navbar';

const CustomerLayout = () => {
  return (
    <div style={{ minHeight: '100vh', display: 'flex', flexDirection: 'column' }}>
      <Navbar />
      <main style={{ flexGrow: 1 }}>
        <Outlet />
      </main>
      
      <footer style={{ borderTop: '1px solid var(--color-border)', padding: 'var(--spacing-lg) 0', textAlign: 'center', color: 'var(--color-text-muted)' }}>
        <div className="container">
          &copy; {new Date().getFullYear()} B2B Enterprise Platform. All rights reserved.
        </div>
      </footer>
    </div>
  );
};

export default CustomerLayout;
