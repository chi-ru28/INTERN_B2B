import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Mail, ArrowLeft, KeyRound, Lock, CheckCircle2 } from 'lucide-react';
import api from '../api/axiosConfig';

const ForgotPassword = () => {
  const [step, setStep] = useState(1); // 1: Email, 2: OTP, 3: New Password
  const [email, setEmail] = useState('');
  const [otp, setOtp] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [devOtp, setDevOtp] = useState('');
  
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  
  const [timeLeft, setTimeLeft] = useState(300); // 5 minutes
  const [resendCooldown, setResendCooldown] = useState(60);

  const navigate = useNavigate();

  useEffect(() => {
    let timer;
    if (step === 2 && timeLeft > 0) {
      timer = setInterval(() => setTimeLeft(prev => prev - 1), 1000);
    }
    return () => clearInterval(timer);
  }, [step, timeLeft]);

  useEffect(() => {
    let timer;
    if (step === 2 && resendCooldown > 0) {
      timer = setInterval(() => setResendCooldown(prev => prev - 1), 1000);
    }
    return () => clearInterval(timer);
  }, [step, resendCooldown]);

  const formatTime = (seconds) => {
    const m = Math.floor(seconds / 60).toString().padStart(2, '0');
    const s = (seconds % 60).toString().padStart(2, '0');
    return `${m}:${s}`;
  };

  const getErrorMessage = (err) => {
    if (!err.response) return 'Failed to connect to the server.';
    
    // Handle API Gateway 503 (no errorCode present in standard Spring Gateway errors)
    if (err.response.status === 503) {
      return 'Service is temporarily unavailable. Please try again later.';
    }

    if (!err.response.data) return 'An unexpected error occurred.';
    
    const errorCode = err.response.data.errorCode;
    switch (errorCode) {
      case 'USER_NOT_FOUND': return 'No account found with this email.';
      case 'SMTP_AUTH_FAILED': return 'Email service is temporarily unavailable.';
      case 'OTP_RATE_LIMIT': return 'Too many requests. Please try again later.';
      case 'OTP_COOLDOWN': return 'Please wait 60 seconds before requesting another code.';
      case 'OTP_EXPIRED': return 'Your verification code has expired.';
      case 'OTP_INVALID': return 'Incorrect verification code.';
      case 'REDIS_UNAVAILABLE': return 'Verification service is temporarily unavailable.';
      case 'SMTP_CONFIGURATION_MISSING': return 'Email service configuration is missing.';
      default: return err.response.data.message || 'An unexpected error occurred.';
    }
  };

  const handleRequestOtp = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    
    try {
      const res = await api.post('/api/v1/auth/request-otp', { email });
      setStep(2);
      setTimeLeft(300);
      setResendCooldown(60);
      setSuccess('Verification code sent successfully.');
      if (res.data.devOtp && import.meta.env.VITE_ENV === 'local') setDevOtp(res.data.devOtp);
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setLoading(false);
    }
  };

  const handleVerifyOtp = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setSuccess('');
    
    try {
      await api.post('/api/v1/auth/verify-otp', { email, otp });
      setStep(3);
      setSuccess('OTP verified. You can now reset your password.');
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setLoading(false);
    }
  };

  const handleResendOtp = async () => {
    if (resendCooldown > 0) return;
    setLoading(true);
    setError('');
    setSuccess('');

    try {
      const res = await api.post('/api/v1/auth/resend-otp', { email });
      setTimeLeft(300);
      setResendCooldown(60);
      setSuccess('Verification code sent successfully.');
      if (res.data.devOtp && import.meta.env.VITE_ENV === 'local') setDevOtp(res.data.devOtp);
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setLoading(false);
    }
  };

  const handleResetPassword = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setSuccess('');

    try {
      await api.post('/api/v1/auth/reset-password', { email, newPassword });
      setSuccess('Password reset successfully!');
      setTimeout(() => navigate('/login'), 3000);
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="card animate-fade-in" style={{ padding: '2.5rem' }}>
      <div style={{ textAlign: 'center', marginBottom: '2rem' }}>
        <h2 style={{ marginBottom: '0.5rem' }}>Reset Password</h2>
        <p style={{ color: 'var(--color-text-muted)' }}>
          {step === 1 && "We'll send you a verification code"}
          {step === 2 && "Enter the 6-digit code sent to your email"}
          {step === 3 && "Create a new secure password"}
        </p>
      </div>

      {error && <div style={{ marginBottom: '1.5rem', color: '#ef4444', background: 'rgba(239, 68, 68, 0.1)', padding: '0.75rem', borderRadius: '8px', fontSize: '0.9rem' }}>{error}</div>}
      {success && <div style={{ marginBottom: '1.5rem', color: '#10b981', background: 'rgba(16, 185, 129, 0.1)', padding: '0.75rem', borderRadius: '8px', fontSize: '0.9rem' }}>{success}</div>}

      {step === 1 && (
        <form onSubmit={handleRequestOtp} style={{ display: 'flex', flexDirection: 'column', gap: '1.5rem' }}>
          <div>
            <label style={{ display: 'block', marginBottom: '0.5rem', color: 'var(--color-text-muted)', fontSize: '0.9rem' }}>Email Address</label>
            <input 
              type="email" 
              className="form-input" 
              style={{ width: '100%', padding: '0.75rem', borderRadius: '8px', background: 'rgba(15, 23, 42, 0.5)', border: '1px solid rgba(255,255,255,0.1)', color: 'white' }}
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="jane@acme.com"
              required
            />
          </div>
          <button type="submit" disabled={loading} className="btn btn-primary" style={{ width: '100%', display: 'flex', justifyContent: 'center', alignItems: 'center', gap: '0.5rem', padding: '0.75rem' }}>
            <Mail size={18} />
            {loading ? 'Sending...' : 'Send Verification Code'}
          </button>
        </form>
      )}

      {step === 2 && (
        <form onSubmit={handleVerifyOtp} style={{ display: 'flex', flexDirection: 'column', gap: '1.5rem' }}>
          {devOtp && (
            <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem', backgroundColor: 'rgba(59, 130, 246, 0.1)', color: '#3b82f6', padding: '1rem', borderRadius: '12px', border: '1px solid rgba(59, 130, 246, 0.2)' }}>
              <div style={{ flexShrink: 0 }}>
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                  <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"></path>
                  <line x1="12" y1="8" x2="12" y2="12"></line>
                  <line x1="12" y1="16" x2="12.01" y2="16"></line>
                </svg>
              </div>
              <span style={{ fontSize: '0.95rem' }}>
                Your OTP is: <strong>{devOtp}</strong>. (Note: Email sending might be disabled).
              </span>
            </div>
          )}
          <div>
            <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '0.5rem' }}>
              <label style={{ color: 'var(--color-text-muted)', fontSize: '0.9rem' }}>Verification Code</label>
              <span style={{ color: timeLeft > 60 ? '#10b981' : '#ef4444', fontSize: '0.9rem', fontWeight: 'bold' }}>
                {formatTime(timeLeft)}
              </span>
            </div>
            <input 
              type="text" 
              maxLength={6}
              className="form-input" 
              style={{ width: '100%', padding: '0.75rem', borderRadius: '8px', background: 'rgba(15, 23, 42, 0.5)', border: '1px solid rgba(255,255,255,0.1)', color: 'white', letterSpacing: '4px', fontSize: '1.2rem', textAlign: 'center' }}
              value={otp}
              onChange={(e) => setOtp(e.target.value.replace(/\D/g, ''))}
              placeholder="000000"
              required
            />
          </div>
          <button type="submit" disabled={loading || timeLeft === 0 || otp.length < 6} className="btn btn-primary" style={{ width: '100%', display: 'flex', justifyContent: 'center', alignItems: 'center', gap: '0.5rem', padding: '0.75rem' }}>
            <KeyRound size={18} />
            {loading ? 'Verifying...' : 'Verify Code'}
          </button>

          <div style={{ textAlign: 'center', fontSize: '0.85rem' }}>
            <span style={{ color: 'var(--color-text-muted)' }}>Didn't receive the code? </span>
            <button 
              type="button" 
              onClick={handleResendOtp}
              disabled={resendCooldown > 0 || loading}
              style={{ 
                background: 'none', 
                border: 'none', 
                color: resendCooldown > 0 ? 'var(--color-text-muted)' : '#1a73e8', 
                cursor: resendCooldown > 0 ? 'not-allowed' : 'pointer',
                fontWeight: 'bold',
                padding: 0
              }}
            >
              {resendCooldown > 0 ? `Resend in ${resendCooldown}s` : 'Resend OTP'}
            </button>
          </div>
        </form>
      )}

      {step === 3 && (
        <form onSubmit={handleResetPassword} style={{ display: 'flex', flexDirection: 'column', gap: '1.5rem' }}>
          <div>
            <label style={{ display: 'block', marginBottom: '0.5rem', color: 'var(--color-text-muted)', fontSize: '0.9rem' }}>New Password</label>
            <input 
              type="password" 
              className="form-input" 
              style={{ width: '100%', padding: '0.75rem', borderRadius: '8px', background: 'rgba(15, 23, 42, 0.5)', border: '1px solid rgba(255,255,255,0.1)', color: 'white' }}
              value={newPassword}
              onChange={(e) => setNewPassword(e.target.value)}
              placeholder="Min 12 characters"
              minLength={12}
              required
            />
          </div>
          <button type="submit" disabled={loading || newPassword.length < 12} className="btn btn-primary" style={{ width: '100%', display: 'flex', justifyContent: 'center', alignItems: 'center', gap: '0.5rem', padding: '0.75rem' }}>
            <Lock size={18} />
            {loading ? 'Updating...' : 'Update Password'}
          </button>
        </form>
      )}

      {step === 1 && (
        <div style={{ textAlign: 'center', marginTop: '2rem' }}>
          <Link to="/login" style={{ display: 'inline-flex', alignItems: 'center', gap: '0.5rem', color: 'var(--color-text-muted)', textDecoration: 'none', fontSize: '0.9rem', transition: 'color 0.2s' }}>
            <ArrowLeft size={16} />
            Back to login
          </Link>
        </div>
      )}
    </div>
  );
};

export default ForgotPassword;
