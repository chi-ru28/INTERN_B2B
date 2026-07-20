import React from 'react';

const ProductCard = ({ product }) => {
  return (
    <div className="card animate-fade-in" style={{ display: 'flex', flexDirection: 'column', height: '100%' }}>
      <div style={{ backgroundColor: 'var(--color-bg)', height: '200px', borderRadius: 'var(--radius-md)', marginBottom: 'var(--spacing-md)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
        {/* Placeholder for Product Image */}
        <span style={{ color: 'var(--color-text-muted)', fontSize: '3rem' }}>🛍️</span>
      </div>
      <h3 style={{ marginBottom: '0.25rem' }}>{product.name}</h3>
      <p style={{ fontSize: '0.875rem', flexGrow: 1 }}>{product.description}</p>
      
      <div className="flex-between" style={{ marginTop: 'var(--spacing-md)' }}>
        <span style={{ fontSize: '1.25rem', fontWeight: '700', color: 'var(--color-text-main)' }}>
          ${product.price}
        </span>
        <button className="btn btn-primary" style={{ padding: '0.5rem 1rem', fontSize: '0.875rem' }}>
          Add to Cart
        </button>
      </div>
    </div>
  );
};

export default ProductCard;
