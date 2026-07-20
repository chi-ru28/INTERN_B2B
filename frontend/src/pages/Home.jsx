import React from 'react';
import { ArrowRight, ShieldCheck, Zap, Globe } from 'lucide-react';
import { Link } from 'react-router-dom';

const Home = () => {
  return (
    <div className="container" style={{ paddingTop: 'var(--spacing-xl)', paddingBottom: 'var(--spacing-xl)' }}>
      
      {/* Hero Section */}
      <section className="animate-fade-in" style={{ textAlign: 'center', marginBottom: 'var(--spacing-xl)' }}>
        <h1 style={{ fontSize: '4rem', marginBottom: 'var(--spacing-md)', background: 'linear-gradient(90deg, var(--color-primary), #00d2ff)', WebkitBackgroundClip: 'text', WebkitTextFillColor: 'transparent' }}>
          Next-Gen B2B Procurement
        </h1>
        <p style={{ fontSize: '1.25rem', maxWidth: '800px', margin: '0 auto var(--spacing-lg) auto' }}>
          Experience the future of enterprise commerce. Our microservice-driven, highly available platform ensures your supply chain never sleeps.
        </p>
        <Link to="/catalog" className="btn btn-primary" style={{ fontSize: '1.125rem', padding: '1rem 2rem' }}>
          Explore Catalog <ArrowRight style={{ marginLeft: '0.5rem' }} size={20} />
        </Link>
      </section>

      {/* Features Section */}
      <section style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))', gap: 'var(--spacing-lg)' }}>
        <div className="card glass">
          <ShieldCheck size={40} color="var(--color-primary)" style={{ marginBottom: 'var(--spacing-md)' }} />
          <h2>Fault Tolerant</h2>
          <p>Built with Resilience4j circuit breakers to guarantee continuous availability during downstream outages.</p>
        </div>
        <div className="card glass">
          <Zap size={40} color="var(--color-primary)" style={{ marginBottom: 'var(--spacing-md)' }} />
          <h2>Event-Driven</h2>
          <p>Asynchronous order choreography powered by Apache Kafka for unparalleled throughput.</p>
        </div>
        <div className="card glass">
          <Globe size={40} color="var(--color-primary)" style={{ marginBottom: 'var(--spacing-md)' }} />
          <h2>Distributed</h2>
          <p>Microservices architecture dynamically orchestrated via Eureka Service Registry.</p>
        </div>
      </section>
    </div>
  );
};

export default Home;
