import React from 'react';
import { ShoppingCart, User } from 'lucide-react';
import { Link } from 'react-router-dom';

const Navbar = () => {
  return (
    <nav className="navbar glass">
      <div className="container flex-between">
        <Link to="/" className="logo">B2B.Enterprise</Link>
        <div className="nav-links flex-center">
          <Link to="/catalog">Catalog</Link>
          <Link to="/orders">Orders</Link>
          <button className="btn btn-secondary" style={{ marginLeft: 'var(--spacing-lg)', padding: '0.5rem', borderRadius: '50%' }}>
            <ShoppingCart size={20} />
          </button>
          <button className="btn btn-secondary" style={{ marginLeft: 'var(--spacing-sm)', padding: '0.5rem', borderRadius: '50%' }}>
            <User size={20} />
          </button>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
