import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../AuthContext';
import api from "../components/Api";

const Login = () => {
  const navigate = useNavigate();
  const { login } = useAuth();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [rememberMe, setRememberMe] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');

  const handleSignup = () => {
    navigate('/signup');
  };

  const handleLogin = async (e) => {
    e.preventDefault();

    const formData = new URLSearchParams();
    formData.append('username', username);
    formData.append('password', password);
    formData.append('remember-me', rememberMe);

    try {
      const response = await fetch('http://localhost:8282/api/member/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: formData,
        credentials: 'include',
      });
      
      if (response.ok) {
        // 로그인 성공 후 즉시 사용자 정보를 가져옵니다.
        const userInfoResponse = await api.get('/api/member/check');
        if (userInfoResponse.status === 200 && userInfoResponse.data.loggedIn) {
          login(userInfoResponse.data);
          alert('Login 성공!');
          navigate('/');
        } else {
          throw new Error('사용자 정보를 가져오는데 실패했습니다.');
        }
      } else {
        setErrorMessage('아이디, 비밀번호를 확인해주세요.');
      }
    } catch (error) {
      console.error('Login error:', error);
      setErrorMessage('로그인 중 오류가 발생했습니다. 나중에 다시 시도해주세요.');
    }
  };

  return (
    <div className="container mt-5">
      <div className="row justify-content-center">
        <div className="col-md-6">
          <div className="card">
            <div className="card-header text-center">
              <h2 className="mb-0">로그인</h2>
            </div>
            <div className="card-body">
              <form onSubmit={handleLogin}>
                <div className="mb-3">
                  <input
                    type="text"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    className="form-control"
                    placeholder="아이디를 입력해 주세요"
                    required
                  />
                </div>
                <div className="mb-3">
                  <div className="input-group">
                    <input
                      type="password"
                      value={password}
                      onChange={(e) => setPassword(e.target.value)}
                      className="form-control"
                      placeholder="비밀번호를 입력해 주세요"
                      required
                    />
                  </div>
                </div>
                <div className="mb-3 form-check">
                  <input
                    type="checkbox"
                    className="form-check-input"
                    id="rememberMe"
                    checked={rememberMe}
                    onChange={(e) => setRememberMe(e.target.checked)}
                  />
                  <label className="form-check-label text-color" htmlFor="rememberMe">자동 로그인</label>
                </div>
                <div className="d-grid">
                  <button type="submit" className="btn btn-primary">로그인하기</button>
                </div>
              </form>
              {errorMessage && <p style={{ color: 'red' }}>{errorMessage}</p>}
              <div className="mt-3 d-grid">
                <button onClick={handleSignup} className="btn btn-secondary text-decoration-none text-color">이메일 회원가입</button>
              </div>
              <div className="mt-2 text-end">
                <button className="btn btn-link small p-0 text-color">비밀번호 재설정</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Login;