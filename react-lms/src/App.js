import React from 'react';
import { BrowserRouter as Router } from 'react-router-dom';
import { Header, Footer } from './components/layout';
import AppRoutes from './AppRoutes';
import { AuthProvider } from './AuthContext';

import 'bootstrap/dist/css/bootstrap.min.css'

function App() {
  return (
    <Router>
      <AuthProvider>
        <AppContent />
      </AuthProvider>
    </Router>
  );
}

function AppContent() {
  return (
    <>
      <Header />
      <div className="App min-vh-100">
        <main>
          <AppRoutes />
        </main>
      </div>
      <Footer />
    </>
  );
}

export default App;