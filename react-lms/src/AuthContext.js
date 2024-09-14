import React, { createContext, useState, useContext, useEffect } from 'react';
import api from "./components/Api";

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [authState, setAuthState] = useState(() => {
    const storedState = localStorage.getItem('authState');
    return storedState ? JSON.parse(storedState) : {
      isLoggedIn: false,
      username: '',
      nickname: '',
      role: ''
    };
  });

  useEffect(() => {
    localStorage.setItem('authState', JSON.stringify(authState));
  }, [authState]);

  const login = (userData) => {
    setAuthState({
      isLoggedIn: true,
      username: userData.username,
      nickname: userData.nickname,
      role: userData.role
    });
  };

  const logout = async () => {
    try {
      await api.post('/api/member/logout');
      setAuthState({
        isLoggedIn: false,
        username: '',
        nickname: '',
        role: ''
      });
      localStorage.removeItem('authState');
    } catch (error) {
      console.error('로그아웃 에러', error);
    }
  };

  const isAdmin = () => authState.role === 'ADMIN';

  return (
    <AuthContext.Provider value={{ ...authState, login, logout, isAdmin }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);