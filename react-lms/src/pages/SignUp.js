import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const SignUp = () => {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    username: '',
    password: '',
    confirmPassword: '',
    nickname: '',
    email: ''
  });

  const [passwordError, setPasswordError] = useState(''); // 비밀번호 오류 메시지 상태
  const [confirmPasswordError, setConfirmPasswordError] = useState(''); // 비밀번호 확인 오류 메시지 상태

  const [isUsernameValid, setIsUsernameValid] = useState(null); // 아이디 중복 메시지 상태
  const [isNicknameValid, setIsNicknameValid] = useState(null); // 닉네임 중복 메시지 상태

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prevState => ({
      ...prevState,
      [name]: value
    }));
    
    // 비밀번호 유효성 검사
    if (name === 'password') {
      validatePassword(value);
    }

    // 비밀번호 확인 유효성 검사
    if (name === 'confirmPassword') {
      validateConfirmPassword(value);
    }
  };

  const validatePassword = (password) => {
    const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,20}$/;
    if (!passwordRegex.test(password)) {
      setPasswordError('비밀번호의 길이가 8~20자이며 영문자, 숫자, 특수문자를 포함해야 합니다.');
    } else {
      setPasswordError('');
    }
  };

  const validateConfirmPassword = (confirmPassword) => {
    if (confirmPassword !== formData.password) {
      setConfirmPasswordError('비밀번호가 일치하지 않습니다.');
    } else {
      setConfirmPasswordError('');
    }
  };

  const handleUsernameCheck = async () => {
    try {
      const response = await fetch(`http://localhost:8282/api/member/check-username?username=${formData.username}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
        credentials: 'include',
      });

      const data = await response.json();
      if (response.ok) {
        alert(data.message);
        setIsUsernameValid(true);
      } else {
        alert(data.error); 
        setIsUsernameValid(false);
      }
    } catch (error) {
      console.error('Error:', error);
      alert('아이디 중복 확인 중 오류가 발생했습니다.');
      setIsUsernameValid(false);
    }
  };

  const handleNicknameCheck = async () => { // 닉네임 중복 확인 함수 
    try {
      const response = await fetch(`http://localhost:8282/api/member/check-nickname?nickname=${formData.nickname}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
        credentials: 'include',
      });

      const data = await response.json();
      if (response.ok) {
        alert(data.message);
        setIsNicknameValid(true);
      } else {
        alert(data.error);
        setIsNicknameValid(false);
      }
    } catch (error) {
      console.error('Error:', error);
      alert('닉네임 중복 확인 중 오류가 발생했습니다.');
      setIsNicknameValid(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (passwordError || confirmPasswordError) {
      alert('비밀번호를 확인해 주세요.');
      return;
    }
    
    if (formData.password !== formData.confirmPassword) {
      alert('비밀번호가 일치하지 않습니다.');
      return;
    }
    
    if (isUsernameValid === false) {
      alert('아이디 중복 확인을 해주세요.');
      return;
    }

    if (isNicknameValid === false) {
      alert('닉네임 중복 확인을 해주세요.');
      return;
    }
  
    try {
      const response = await fetch('http://localhost:8282/api/member/signup', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        credentials: 'include',
        body: JSON.stringify({
          username: formData.username,
          password: formData.password,
          nickname: formData.nickname,
          email: formData.email
        }),
      });
  
      const contentType = response.headers.get("content-type");
      if (contentType && contentType.indexOf("application/json") !== -1) {
        const data = await response.json();
        if (response.ok) {
          alert(data.message || "회원가입이 완료되었습니다.");
          navigate('/login');
        } else {
          alert(`회원가입 실패: ${data.error || "알 수 없는 오류가 발생했습니다."}`);
        }
      } else {
        const text = await response.text();
        console.error('Unexpected response:', text);
        alert('서버에서 예상치 못한 응답을 받았습니다.');
      }
    } catch (error) {
      console.error('Error:', error);
      alert('회원가입 중 오류가 발생했습니다.');
    }
  };

  return (
    <div className="container mt-5">
      <div className="row justify-content-center">
        <div className="col-md-6">
          <div className="card">
            <div className="card-header text-center">
              <h4 className="my-1 text-color">회원가입</h4>
            </div>
            <div className="card-body">
              <form onSubmit={handleSubmit}>
                <div className="mb-3">
                  <label><p className='text-color'>아이디</p></label>
                  <div className="input-group">
                  <input 
                    type="text" 
                    name="username"
                    className="form-control" 
                    placeholder="사용할 아이디를 입력해 주세요"
                    value={formData.username}
                    onChange={handleChange}
                    required
                  />
                  <button type="button" className="btn btn-outline-secondary" onClick={handleUsernameCheck}>
                    중복 확인
                  </button>
                  </div>
                </div>
                <div className="mb-3">
                  <label><p className='text-color'>비밀번호</p></label>
                  <input 
                    type="password" 
                    name="password"
                    className="form-control mb-1" 
                    placeholder="영문자,숫자,특수문자 포함 8~20자"
                    value={formData.password}
                    onChange={handleChange}
                    required
                  />
                                    {passwordError && <p style={{ color: 'blue' }}>{passwordError}</p>} {/* 비밀번호 입력 메시지 */}
                  <input 
                    type="password" 
                    name="confirmPassword"
                    className="form-control" 
                    placeholder="비밀번호를 확인해 주세요"
                    value={formData.confirmPassword}
                    onChange={handleChange}
                    required
                  />
                  {confirmPasswordError && <p style={{ color: 'blue' }}>{confirmPasswordError}</p>} {/*비밀번호 확인 메시지 */}
                </div>
                <div className="mb-3">
                  <label><p className='text-color'>이메일</p></label>
                  <input 
                    type="email" 
                    name="email"
                    className="form-control" 
                    placeholder="이메일을 입력해주세요"
                    value={formData.email}
                    onChange={handleChange}
                    required
                  />
                </div>
                <div className="mb-3">
                  <label><p className='text-color'>닉네임</p></label>
                  <div className="input-group">
                  <input 
                    type="text" 
                    name="nickname"
                    className="form-control" 
                    placeholder="사용할 닉네임을 입력해주세요"
                    value={formData.nickname}
                    onChange={handleChange}
                    required
                  />
                  <button type="button" className="btn btn-outline-secondary" onClick={handleNicknameCheck}>
                      중복 확인
                  </button>
                  </div>
                </div>
                <div className="d-grid">
                  <button type="submit" className="btn btn-secondary" disabled={!isUsernameValid || !isNicknameValid}>
                      가입하기
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default SignUp;
