import React, { useState, useEffect, useContext } from 'react';
import { User, Mail, Shield, Clock, Package } from 'lucide-react';
import { AuthContext } from '../context/AuthContext';
import { Link } from 'react-router-dom';

const Profile = () => {
  const { user: authUser } = useContext(AuthContext);
  const [isEditing, setIsEditing] = useState(false);
  const [toast, setToast] = useState({ show: false, message: '', type: 'success' });
  
  const [user, setUser] = useState({
    name: authUser?.name || 'Demo User',
    role: authUser?.roles?.join(', ') || 'Customer',
    email: authUser?.email || 'user@b2b.enterprise'
  });
  
  useEffect(() => {
    if (authUser) {
      setUser({
        name: authUser.name || 'Demo User',
        role: authUser.roles?.join(', ') || 'Customer',
        email: authUser.email || 'user@b2b.enterprise'
      });
    }
  }, [authUser]);
  
  const [editForm, setEditForm] = useState({ ...user });

  useEffect(() => {
    setEditForm({ ...user });
  }, [user]);

  const showToast = (message, type = 'success') => {
    setToast({ show: true, message, type });
    setTimeout(() => setToast({ show: false, message: '', type: 'success' }), 3000);
  };

  const handleEditToggle = () => {
    if (isEditing) {
      // Cancel edit
      setEditForm({ ...user });
    }
    setIsEditing(!isEditing);
  };

  const handleSave = () => {
    setUser({ ...editForm });
    setIsEditing(false);
    showToast('Profile updated successfully!');
  };

  const handleResetPassword = () => {
    showToast(`Password reset link sent to ${user.email}!`);
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setEditForm(prev => ({ ...prev, [name]: value }));
  };

  const isGuest = authUser?.roles?.includes('ROLE_GUEST') || authUser?.email?.toLowerCase().includes('guest') || user.role.toLowerCase().includes('guest');

  if (isGuest) {
    return (
      <div className="container" style={{ paddingTop: 'var(--spacing-xl)', paddingBottom: 'var(--spacing-xl)', position: 'relative' }}>
        <h2 style={{ marginBottom: 'var(--spacing-lg)' }}>My Profile</h2>
        <div className="card animate-fade-in" style={{ maxWidth: '800px', margin: '0 auto', padding: '3rem 2rem', textAlign: 'center' }}>
          <div style={{ display: 'inline-flex', padding: '1.5rem', borderRadius: '50%', background: 'rgba(139, 92, 246, 0.1)', marginBottom: '1.5rem' }}>
            <Shield size={64} color="#8b5cf6" />
          </div>
          <h3 style={{ fontSize: '1.8rem', marginBottom: '1rem', color: '#f8fafc' }}>Guest Account</h3>
          <p style={{ color: '#94a3b8', marginBottom: '2rem', fontSize: '1.1rem', maxWidth: '500px', margin: '0 auto 2.5rem auto', lineHeight: '1.6' }}>
            This is a guest account. You do not have access to profile settings, order history, or other account features. 
            You only have access to view catalog items.
          </p>
          <Link to="/catalog" className="btn btn-primary" style={{ display: 'inline-flex', alignItems: 'center', gap: '0.75rem', padding: '0.75rem 1.5rem', fontSize: '1.1rem' }}>
            <Package size={20} />
            View Catalog
          </Link>
        </div>
      </div>
    );
  }

  return (
    <div className="container" style={{ paddingTop: 'var(--spacing-xl)', paddingBottom: 'var(--spacing-xl)', position: 'relative' }}>
      <h2 style={{ marginBottom: 'var(--spacing-lg)' }}>My Profile</h2>
      
      <div className="card animate-fade-in" style={{ maxWidth: '800px', margin: '0 auto', padding: '2rem' }}>
        <div style={{ display: 'flex', alignItems: 'flex-start', gap: '2rem', borderBottom: '1px solid rgba(255,255,255,0.1)', paddingBottom: '2rem', marginBottom: '2rem' }}>
          <div style={{ 
            width: '100px', 
            height: '100px', 
            borderRadius: '50%', 
            background: 'linear-gradient(135deg, #6366f1, #8b5cf6)', 
            display: 'flex', 
            alignItems: 'center', 
            justifyContent: 'center', 
            color: 'white', 
            fontSize: '2.5rem',
            fontWeight: 'bold',
            boxShadow: '0 10px 25px -5px rgba(99, 102, 241, 0.5)'
          }}>
            {user.name ? user.name.split(' ').map(n => n[0]).join('').toUpperCase().substring(0, 2) : 'U'}
          </div>
          <div style={{ flex: 1 }}>
            {isEditing ? (
              <div style={{ display: 'flex', flexDirection: 'column', gap: '0.5rem', marginBottom: '1rem' }}>
                <input 
                  type="text" 
                  name="name" 
                  value={editForm.name} 
                  onChange={handleInputChange} 
                  className="form-input" 
                  style={{ fontSize: '1.5rem', fontWeight: 'bold', background: 'rgba(15, 23, 42, 0.5)', color: '#f8fafc', border: '1px solid rgba(255,255,255,0.1)', padding: '0.5rem', borderRadius: '8px' }} 
                />
                <input 
                  type="text" 
                  name="role" 
                  value={editForm.role} 
                  onChange={handleInputChange} 
                  className="form-input" 
                  style={{ fontSize: '1rem', background: 'rgba(15, 23, 42, 0.5)', color: '#94a3b8', border: '1px solid rgba(255,255,255,0.1)', padding: '0.5rem', borderRadius: '8px' }} 
                />
              </div>
            ) : (
              <>
                <h1 style={{ fontSize: '2rem', margin: '0 0 0.5rem 0', color: '#f8fafc' }}>{user.name}</h1>
                <p style={{ color: '#94a3b8', fontSize: '1.1rem', margin: '0 0 1rem 0' }}>{user.role || 'Customer'}</p>
              </>
            )}
            <div style={{ display: 'inline-block', background: 'rgba(16, 185, 129, 0.2)', color: '#34d399', padding: '0.25rem 0.75rem', borderRadius: '12px', fontSize: '0.85rem', fontWeight: 'bold' }}>
              Active Account
            </div>
          </div>
        </div>

        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1.5rem' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '1rem', background: 'rgba(15, 23, 42, 0.5)', padding: '1rem', borderRadius: '12px' }}>
            <Mail size={24} color="#6366f1" />
            <div style={{ flex: 1 }}>
              <div style={{ fontSize: '0.8rem', color: '#94a3b8', marginBottom: '0.25rem' }}>Email Address</div>
              {isEditing ? (
                <input 
                  type="email" 
                  name="email" 
                  value={editForm.email} 
                  onChange={handleInputChange} 
                  className="form-input" 
                  style={{ width: '100%', background: 'rgba(30, 41, 59, 0.8)', color: '#f8fafc', border: '1px solid rgba(255,255,255,0.2)', padding: '0.25rem 0.5rem', borderRadius: '4px' }} 
                />
              ) : (
                <div style={{ color: '#e2e8f0', fontWeight: '500' }}>{user.email}</div>
              )}
            </div>
          </div>
          
          <div style={{ display: 'flex', alignItems: 'center', gap: '1rem', background: 'rgba(15, 23, 42, 0.5)', padding: '1rem', borderRadius: '12px' }}>
            <Shield size={24} color="#8b5cf6" />
            <div>
              <div style={{ fontSize: '0.8rem', color: '#94a3b8', marginBottom: '0.25rem' }}>Access Role</div>
              <div style={{ color: '#e2e8f0', fontWeight: '500' }}>{user.role || 'Customer'}</div>
            </div>
          </div>

          <div style={{ display: 'flex', alignItems: 'center', gap: '1rem', background: 'rgba(15, 23, 42, 0.5)', padding: '1rem', borderRadius: '12px' }}>
            <Clock size={24} color="#10b981" />
            <div>
              <div style={{ fontSize: '0.8rem', color: '#94a3b8', marginBottom: '0.25rem' }}>Member Since</div>
              <div style={{ color: '#e2e8f0', fontWeight: '500' }}>Today</div>
            </div>
          </div>

          <div style={{ display: 'flex', alignItems: 'center', gap: '1rem', background: 'rgba(15, 23, 42, 0.5)', padding: '1rem', borderRadius: '12px' }}>
            <Package size={24} color="#f59e0b" />
            <div>
              <div style={{ fontSize: '0.8rem', color: '#94a3b8', marginBottom: '0.25rem' }}>Total Orders</div>
              <div style={{ color: '#e2e8f0', fontWeight: '500' }}>0</div>
            </div>
          </div>
        </div>

        <div style={{ marginTop: '2.5rem', display: 'flex', justifyContent: 'flex-end', gap: '1rem' }}>
          {isEditing ? (
            <>
              <button className="btn btn-secondary" onClick={handleEditToggle}>Cancel</button>
              <button className="btn btn-primary" onClick={handleSave} style={{ background: '#10b981' }}>Save Changes</button>
            </>
          ) : (
            <>
              <button className="btn btn-secondary" onClick={handleEditToggle}>Edit Profile</button>
              <button className="btn btn-primary" onClick={handleResetPassword}>Reset Password</button>
            </>
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
    </div>
  );
};

export default Profile;

