import React, { useState } from 'react';
import { Bell, Shield, Palette, Globe, Key, Save } from 'lucide-react';

const Settings = () => {
  const [notifications, setNotifications] = useState(true);
  const [marketingEmails, setMarketingEmails] = useState(false);
  const [twoFactorAuth, setTwoFactorAuth] = useState(false);

  const handleSave = () => {
    alert('Settings saved successfully!');
  };

  return (
    <div className="container" style={{ paddingTop: 'var(--spacing-xl)', paddingBottom: 'var(--spacing-xl)' }}>
      <h2 style={{ marginBottom: 'var(--spacing-lg)' }}>Account Settings</h2>
      
      <div style={{ display: 'grid', gridTemplateColumns: '1fr', gap: '2rem', maxWidth: '800px', margin: '0 auto' }}>
        
        {/* Notifications Section */}
        <div className="card animate-fade-in">
          <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem', marginBottom: '1.5rem', borderBottom: '1px solid rgba(255,255,255,0.1)', paddingBottom: '1rem' }}>
            <Bell size={24} color="#6366f1" />
            <h3 style={{ margin: 0, color: '#f8fafc' }}>Notifications</h3>
          </div>
          
          <div className="flex-between" style={{ marginBottom: '1rem' }}>
            <div>
              <div style={{ color: '#e2e8f0', fontWeight: '500', marginBottom: '0.25rem' }}>Order Updates</div>
              <div style={{ color: '#94a3b8', fontSize: '0.85rem' }}>Receive email notifications when your order status changes.</div>
            </div>
            <label className="toggle-switch">
              <input type="checkbox" checked={notifications} onChange={() => setNotifications(!notifications)} />
              <span className="slider"></span>
            </label>
          </div>
          
          <div className="flex-between">
            <div>
              <div style={{ color: '#e2e8f0', fontWeight: '500', marginBottom: '0.25rem' }}>Marketing & Promotions</div>
              <div style={{ color: '#94a3b8', fontSize: '0.85rem' }}>Receive exclusive B2B offers and platform updates.</div>
            </div>
            <label className="toggle-switch">
              <input type="checkbox" checked={marketingEmails} onChange={() => setMarketingEmails(!marketingEmails)} />
              <span className="slider"></span>
            </label>
          </div>
        </div>

        {/* Security Section */}
        <div className="card animate-fade-in" style={{ animationDelay: '0.1s' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem', marginBottom: '1.5rem', borderBottom: '1px solid rgba(255,255,255,0.1)', paddingBottom: '1rem' }}>
            <Shield size={24} color="#10b981" />
            <h3 style={{ margin: 0, color: '#f8fafc' }}>Security</h3>
          </div>
          
          <div className="flex-between" style={{ marginBottom: '1.5rem' }}>
            <div>
              <div style={{ color: '#e2e8f0', fontWeight: '500', marginBottom: '0.25rem' }}>Two-Factor Authentication (2FA)</div>
              <div style={{ color: '#94a3b8', fontSize: '0.85rem' }}>Add an extra layer of security to your account.</div>
            </div>
            <label className="toggle-switch">
              <input type="checkbox" checked={twoFactorAuth} onChange={() => setTwoFactorAuth(!twoFactorAuth)} />
              <span className="slider"></span>
            </label>
          </div>
          
          <div className="flex-between" style={{ alignItems: 'center' }}>
            <div>
              <div style={{ color: '#e2e8f0', fontWeight: '500', marginBottom: '0.25rem' }}>Password</div>
              <div style={{ color: '#94a3b8', fontSize: '0.85rem' }}>Last changed 3 months ago.</div>
            </div>
            <button className="btn btn-secondary" style={{ padding: '0.5rem 1rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
              <Key size={16} />
              Change
            </button>
          </div>
        </div>

        {/* Preferences Section */}
        <div className="card animate-fade-in" style={{ animationDelay: '0.2s' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem', marginBottom: '1.5rem', borderBottom: '1px solid rgba(255,255,255,0.1)', paddingBottom: '1rem' }}>
            <Palette size={24} color="#f59e0b" />
            <h3 style={{ margin: 0, color: '#f8fafc' }}>Preferences</h3>
          </div>
          
          <div className="flex-between" style={{ marginBottom: '1.5rem' }}>
            <div>
              <div style={{ color: '#e2e8f0', fontWeight: '500', marginBottom: '0.25rem' }}>Language</div>
              <div style={{ color: '#94a3b8', fontSize: '0.85rem' }}>Your preferred language for the interface.</div>
            </div>
            <select className="form-input" style={{ width: '150px', background: 'rgba(15, 23, 42, 0.5)', color: '#f8fafc', border: '1px solid rgba(255,255,255,0.1)', padding: '0.5rem', borderRadius: '8px' }}>
              <option value="en">English (US)</option>
              <option value="es">Español</option>
              <option value="fr">Français</option>
              <option value="de">Deutsch</option>
            </select>
          </div>
          
          <div className="flex-between">
            <div>
              <div style={{ color: '#e2e8f0', fontWeight: '500', marginBottom: '0.25rem' }}>Currency</div>
              <div style={{ color: '#94a3b8', fontSize: '0.85rem' }}>Display prices in your local currency.</div>
            </div>
            <select className="form-input" style={{ width: '150px', background: 'rgba(15, 23, 42, 0.5)', color: '#f8fafc', border: '1px solid rgba(255,255,255,0.1)', padding: '0.5rem', borderRadius: '8px' }}>
              <option value="usd">USD ($)</option>
              <option value="eur">EUR (€)</option>
              <option value="gbp">GBP (£)</option>
            </select>
          </div>
        </div>
        
        {/* Save Actions */}
        <div className="flex-between animate-fade-in" style={{ animationDelay: '0.3s', marginTop: '1rem' }}>
          <button className="btn btn-secondary" style={{ padding: '0.75rem 1.5rem' }}>Discard Changes</button>
          <button className="btn btn-primary" onClick={handleSave} style={{ padding: '0.75rem 1.5rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
            <Save size={18} />
            Save Preferences
          </button>
        </div>

      </div>
    </div>
  );
};

export default Settings;
