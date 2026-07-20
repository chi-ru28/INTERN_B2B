import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar';
import Home from './pages/Home';
import Catalog from './pages/Catalog';
import Orders from './pages/Orders';
import Profile from './pages/Profile';
import Settings from './pages/Settings';

import { CartProvider } from './context/CartContext';

function App() {
  return (
    <CartProvider>
      <Router>
        <div style={{ minHeight: '100vh', display: 'flex', flexDirection: 'column' }}>
          <Navbar />
          <main style={{ flexGrow: 1 }}>
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/catalog" element={<Catalog />} />
              <Route path="/orders" element={<Orders />} />
              <Route path="/profile" element={<Profile />} />
              <Route path="/settings" element={<Settings />} />
            </Routes>
          </main>
          
          <footer style={{ borderTop: '1px solid var(--color-border)', padding: 'var(--spacing-lg) 0', textAlign: 'center', color: 'var(--color-text-muted)' }}>
            <div className="container">
              &copy; {new Date().getFullYear()} B2B Enterprise Platform. All rights reserved.
            </div>
          </footer>
        </div>
      </Router>
    </CartProvider>
  );
}

export default App;
