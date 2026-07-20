import React, { useContext, useState, useRef, useEffect } from 'react';
import { ShoppingCart, User, X, Trash2, LogOut, Settings, UserCircle } from 'lucide-react';
import { Link, useNavigate } from 'react-router-dom';
import { CartContext } from '../context/CartContext';
import { AuthContext } from '../context/AuthContext';

const Navbar = () => {
  const { cartItems, totalItems, removeFromCart, clearCart } = useContext(CartContext);
  const { user, logout } = useContext(AuthContext);
  const [isCartOpen, setIsCartOpen] = useState(false);
  const [isProfileOpen, setIsProfileOpen] = useState(false);
  const [toast, setToast] = useState({ show: false, message: '', type: 'success' });
  const cartRef = useRef(null);
  const profileRef = useRef(null);
  const navigate = useNavigate();

  // Close dropdowns when clicking outside
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (cartRef.current && !cartRef.current.contains(event.target)) {
        setIsCartOpen(false);
      }
      if (profileRef.current && !profileRef.current.contains(event.target)) {
        setIsProfileOpen(false);
      }
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  const showToast = (message, type = 'success') => {
    setToast({ show: true, message, type });
    setTimeout(() => setToast({ show: false, message: '', type: 'success' }), 3000);
  };

  const cartTotal = cartItems.reduce((sum, item) => sum + (parseFloat(item.price.replace(/,/g, '')) * item.quantity), 0);

  const handleCheckout = async () => {
    if (!user) {
      showToast('Please login to checkout', 'error');
      setTimeout(() => navigate('/login'), 1500);
      return;
    }
    
    if (cartItems.length === 0) return;
    
    try {
      const orderPayload = {
        orderLineItemsList: cartItems.map(item => ({
          skuCode: item.skuCode,
          price: parseFloat(item.price.replace(/,/g, '')),
          quantity: item.quantity
        }))
      };

      const response = await fetch('/api/orders', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(orderPayload)
      });

      if (response.ok) {
        clearCart();
        setIsCartOpen(false);
        showToast('Order placed successfully!', 'success');
        setTimeout(() => navigate('/orders'), 1500);
      } else {
        const errorText = await response.text();
        showToast(`Failed to place order: ${errorText}`, 'error');
      }
    } catch (error) {
      console.error('Checkout error:', error);
      showToast('Network error occurred during checkout.', 'error');
    }
  };

  const handleSignOut = () => {
    setIsProfileOpen(false);
    logout();
    showToast('Sign out successful!', 'success');
    navigate('/');
  };

  const openCartOrLogin = () => {
    if (!user) {
      showToast('Please login to view cart', 'error');
      navigate('/login');
      return;
    }
    setIsCartOpen(!isCartOpen);
    setIsProfileOpen(false);
  };

  return (
    <nav className="navbar glass">
      <div className="container flex-between">
        <Link to="/" className="logo">B2B.Enterprise</Link>
        <div className="nav-links flex-center" style={{ gap: '1rem' }}>
          <Link to="/catalog" style={{ marginRight: '0.5rem' }}>Catalog</Link>
          
          {user ? (
            <Link to="/orders" style={{ marginRight: '0.5rem' }}>Orders</Link>
          ) : (
            <>
              <Link to="/login" style={{ color: '#cbd5e1', textDecoration: 'none', fontWeight: '500' }}>Login</Link>
              <Link to="/register" className="btn btn-primary" style={{ padding: '0.4rem 1rem', fontSize: '0.9rem', textDecoration: 'none' }}>Sign Up</Link>
            </>
          )}
          
          <div ref={cartRef} style={{ position: 'relative', display: 'inline-block', marginLeft: 'var(--spacing-lg)' }}>
            <button className="btn btn-secondary" onClick={openCartOrLogin} style={{ padding: '0.5rem', borderRadius: '50%' }}>
              <ShoppingCart size={20} />
            </button>
            {user && totalItems > 0 && (
              <span className="cart-badge animate-scale-in">
                {totalItems > 99 ? '99+' : totalItems}
              </span>
            )}

            {/* Cart Dropdown */}
            {isCartOpen && user && (
              <div className="cart-dropdown animate-fade-in" style={{
                position: 'absolute', top: '100%', right: '0', marginTop: '1rem',
                width: '350px', backgroundColor: 'rgba(30, 41, 59, 0.95)', backdropFilter: 'blur(16px)',
                borderRadius: '16px', border: '1px solid rgba(255, 255, 255, 0.1)',
                boxShadow: '0 25px 50px -12px rgba(0, 0, 0, 0.5)', zIndex: 100, padding: '1.5rem',
                display: 'flex', flexDirection: 'column', gap: '1rem'
              }}>
                <div className="flex-between" style={{ borderBottom: '1px solid rgba(255,255,255,0.1)', paddingBottom: '1rem' }}>
                  <h3 style={{ margin: 0, fontSize: '1.25rem', color: '#f8fafc' }}>Your Cart</h3>
                  <button onClick={() => setIsCartOpen(false)} style={{ background: 'none', border: 'none', color: '#94a3b8', cursor: 'pointer' }}><X size={20} /></button>
                </div>

                {cartItems.length === 0 ? (
                  <p style={{ color: '#94a3b8', textAlign: 'center', margin: '2rem 0' }}>Your cart is empty.</p>
                ) : (
                  <>
                    <div style={{ maxHeight: '300px', overflowY: 'auto', display: 'flex', flexDirection: 'column', gap: '1rem', paddingRight: '0.5rem' }}>
                      {cartItems.map((item, index) => (
                        <div key={index} className="flex-between" style={{ alignItems: 'center' }}>
                          <div style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
                            <img src={item.imageUrl} alt={item.name} style={{ width: '40px', height: '40px', borderRadius: '8px', objectFit: 'cover', background: '#0f172a' }} />
                            <div>
                              <div style={{ fontSize: '0.9rem', color: '#e2e8f0', fontWeight: '500', maxWidth: '160px', overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>{item.name}</div>
                              <div style={{ fontSize: '0.8rem', color: '#94a3b8' }}>Qty: {item.quantity} × ${item.price}</div>
                            </div>
                          </div>
                          <button onClick={() => removeFromCart(item.id)} style={{ background: 'none', border: 'none', color: '#ef4444', cursor: 'pointer', padding: '0.25rem' }}>
                            <Trash2 size={16} />
                          </button>
                        </div>
                      ))}
                    </div>
                    
                    <div style={{ borderTop: '1px solid rgba(255,255,255,0.1)', paddingTop: '1rem' }}>
                      <div className="flex-between" style={{ marginBottom: '1rem', color: '#f8fafc', fontWeight: 'bold' }}>
                        <span>Total:</span>
                        <span>${cartTotal.toLocaleString('en-US', {minimumFractionDigits: 2, maximumFractionDigits: 2})}</span>
                      </div>
                      <div style={{ display: 'flex', gap: '0.5rem' }}>
                        <button onClick={clearCart} className="btn btn-secondary" style={{ flex: 1, padding: '0.75rem' }}>Clear</button>
                        <button onClick={handleCheckout} className="btn btn-primary" style={{ flex: 2, padding: '0.75rem' }}>Checkout</button>
                      </div>
                    </div>
                  </>
                )}
              </div>
            )}
          </div>
          
          {user && (
            <div ref={profileRef} style={{ position: 'relative', display: 'inline-block', marginLeft: 'var(--spacing-sm)' }}>
              <button className="btn btn-secondary" onClick={() => { setIsProfileOpen(!isProfileOpen); setIsCartOpen(false); }} style={{ padding: '0.5rem', borderRadius: '50%' }}>
                <User size={20} />
              </button>
              
              {/* Profile Dropdown */}
              {isProfileOpen && (
                <div className="profile-dropdown animate-fade-in" style={{
                  position: 'absolute', top: '100%', right: '0', marginTop: '1rem',
                  width: '240px', backgroundColor: 'rgba(30, 41, 59, 0.95)', backdropFilter: 'blur(16px)',
                  borderRadius: '16px', border: '1px solid rgba(255, 255, 255, 0.1)',
                  boxShadow: '0 25px 50px -12px rgba(0, 0, 0, 0.5)', zIndex: 100, padding: '1rem',
                  display: 'flex', flexDirection: 'column'
                }}>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '1rem', borderBottom: '1px solid rgba(255,255,255,0.1)', paddingBottom: '1rem', marginBottom: '0.5rem' }}>
                    <div style={{ width: '40px', height: '40px', borderRadius: '50%', background: 'linear-gradient(135deg, #6366f1, #8b5cf6)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: 'white', fontWeight: 'bold' }}>
                      {user.name.substring(0, 2).toUpperCase()}
                    </div>
                    <div>
                      <div style={{ color: '#f8fafc', fontWeight: '600', fontSize: '0.95rem' }}>{user.name}</div>
                      <div style={{ color: '#94a3b8', fontSize: '0.8rem' }}>{user.email}</div>
                    </div>
                  </div>
                  
                  <button className="dropdown-item" onClick={() => { setIsProfileOpen(false); navigate('/profile'); }} style={{ display: 'flex', alignItems: 'center', gap: '0.75rem', background: 'none', border: 'none', color: '#cbd5e1', padding: '0.75rem', borderRadius: '8px', cursor: 'pointer', textAlign: 'left', transition: 'background 0.2s' }}>
                    <UserCircle size={18} />
                    <span>My Profile</span>
                  </button>
                  <button className="dropdown-item" onClick={() => { setIsProfileOpen(false); navigate('/settings'); }} style={{ display: 'flex', alignItems: 'center', gap: '0.75rem', background: 'none', border: 'none', color: '#cbd5e1', padding: '0.75rem', borderRadius: '8px', cursor: 'pointer', textAlign: 'left', transition: 'background 0.2s' }}>
                    <Settings size={18} />
                    <span>Settings</span>
                  </button>
                  <div style={{ height: '1px', background: 'rgba(255,255,255,0.1)', margin: '0.5rem 0' }}></div>
                  <button className="dropdown-item" onClick={handleSignOut} style={{ display: 'flex', alignItems: 'center', gap: '0.75rem', background: 'none', border: 'none', color: '#ef4444', padding: '0.75rem', borderRadius: '8px', cursor: 'pointer', textAlign: 'left', transition: 'background 0.2s' }}>
                    <LogOut size={18} />
                    <span>Sign Out</span>
                  </button>
                </div>
              )}
            </div>
          )}
        </div>
      </div>
      
      {/* Toast Notification */}
      {toast.show && (
        <div style={{
          position: 'fixed',
          bottom: '2rem',
          right: '2rem',
          backgroundColor: toast.type === 'success' ? 'rgba(16, 185, 129, 0.95)' : 'rgba(239, 68, 68, 0.95)',
          color: 'white',
          padding: '1rem 1.5rem',
          borderRadius: '8px',
          boxShadow: '0 10px 25px -5px rgba(0, 0, 0, 0.5)',
          backdropFilter: 'blur(8px)',
          display: 'flex',
          alignItems: 'center',
          gap: '0.75rem',
          zIndex: 1000,
          animation: 'slide-up 0.3s ease-out forwards',
          fontWeight: '500'
        }}>
          {toast.message}
        </div>
      )}
    </nav>
  );
};

export default Navbar;
