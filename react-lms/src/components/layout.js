import React from "react";
import { Menu } from 'lucide-react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../AuthContext';
import '../css/header.css';

export function Header() {
  const location = useLocation();
  const navigate = useNavigate();
  const { isLoggedIn, nickname, logout, isAdmin } = useAuth();

  const handleLogout = async (e) => {
    e.preventDefault();
    await logout();
    navigate('/login');
  };

  const isCodingPage = location.pathname.startsWith('/');

  const headerStyle = isCodingPage
    ? { backgroundColor: '#263238', color: '#B0BEC5' }
    : {};

  const linkStyle = isCodingPage
    ? { color: '#B0BEC5' }
    : { color: 'inherit' };

  return (
    <header className="navbar navbar-light topbar" style={headerStyle}>
      <div className="container">
        <h1>
          <Link to="/" style={{ ...linkStyle, textDecoration: 'none' }}>
            CoTeMon
          </Link>
        </h1>
        <div className="header-flex">
          <div>
            <Link to="/coding-test" style={{ ...linkStyle, textDecoration: 'none', marginRight: '1.5rem' }}>
              Coding Test
            </Link>
            <Link to="/rankings" style={{ ...linkStyle, textDecoration: 'none', marginRight: '1.5rem' }}>
              랭킹
            </Link>
            <Link to="/notices" style={{ ...linkStyle, textDecoration: 'none', marginRight: '1.5rem' }}>
              공지사항
            </Link>
            <Link to="/recommends" style={{ ...linkStyle, textDecoration: 'none' }}>
              추천 채용
            </Link>
          </div>
          <div>
            {location.pathname !== '/login' && (
              isLoggedIn ? (
                <div>
                  <Link to="/logout" style={{ ...linkStyle, textDecoration: 'none', marginRight: '1rem' }} onClick={handleLogout}>
                    로그아웃
                  </Link>
                  {isAdmin() && (
                    <Link to="/admin" style={{ ...linkStyle, textDecoration: 'none', marginRight: '1rem' }}>
                      관리자 페이지
                    </Link>
                  )}
                  <Link to="/my-page" style={{ ...linkStyle, textDecoration: 'none' }}>
                    {nickname} 님
                  </Link>
                </div>
              ) : (
                <div>
                  <Link to="/login" style={{ ...linkStyle, textDecoration: 'none', marginRight: '1rem' }}>
                    로그인
                  </Link>
                  <Link to="/signup" style={{ ...linkStyle, textDecoration: 'none' }}>
                    회원가입
                  </Link>
                </div>
              )
            )}
          </div>
        </div>
      </div>
    </header>
  );
}

export function Footer() {
  const location = useLocation();
  const isCodingPage = location.pathname.startsWith('/coding-page/');

  const footerStyle = isCodingPage
    ? { backgroundColor: '#263238', color: '#B0BEC5' }
    : {};

  return (
    <footer className="sticky-footer navbar-gray" style={footerStyle}>
      <div className="container mx-auto flex justify-between items-center text-center">
        <span className='text-color'>Copyright @ CodeTestMonster</span>
      </div>
    </footer>
  );
}

export function CodingHeader() {
  return (
    <header className="navbar navbar-light topbar">
      <div className="container mx-auto flex justify-between items-center">
        <h1>My Website</h1>
        <button className="md:hidden">
          <Menu size={24} />
        </button>
      </div>
    </header>
  );
}