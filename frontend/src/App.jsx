import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar';
import Home from './pages/Home';
import Catalog from './pages/Catalog';

function App() {
  return (
    <Router>
      <div style={{ minHeight: '100vh', display: 'flex', flexDirection: 'column' }}>
        <Navbar />
        <main style={{ flexGrow: 1 }}>
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/catalog" element={<Catalog />} />
            <Route path="/orders" element={<div className="container" style={{ paddingTop: '4rem' }}><h2>Orders History (Coming Soon)</h2></div>} />
          </Routes>
        </main>
        
        <footer style={{ borderTop: '1px solid var(--color-border)', padding: 'var(--spacing-lg) 0', textAlign: 'center', color: 'var(--color-text-muted)' }}>
          <div className="container">
            &copy; {new Date().getFullYear()} B2B Enterprise Platform. All rights reserved.
          </div>
        </footer>
      </div>
    </Router>
  );
}

export default App;
