import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const ChangePassword = () => {
  const [memberInfo, setMemberInfo] = useState({
    username: '',
    nickname: '',
  });

  const [passwords, setPasswords] = useState({
    newPassword: '',
    confirmPassword: '',
  });

  const [passwordError, setPasswordError] = useState(''); // 비밀번호 오류 메시지 상태
  const [confirmPasswordError, setConfirmPasswordError] = useState(''); // 비밀번호 확인 오류 메시지 상태

  const navigate = useNavigate();

  useEffect(() => {
   
    axios
      .get('/api/member/mypage')
      .then((response) => {
        const { username, nickname } = response.data;
        setMemberInfo({ username, nickname });
      })
      .catch((error) => {
        console.error("Error fetching member data:", error);
      });
  }, []);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setPasswords((prevPasswords) => ({
      ...prevPasswords,
      [name]: value,
    }));
    
        // 비밀번호 유효성 검사
        if (name === 'newPassword') {
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
        if (confirmPassword !== passwords.newPassword) {
          setConfirmPasswordError('비밀번호가 일치하지 않습니다.');
        } else {
          setConfirmPasswordError('');
        }
  };

  const handleChangePassword = (e) => {
    e.preventDefault();

    if (passwordError || confirmPasswordError) {
      alert('비밀번호를 확인해 주세요.');
      return;
    }

    axios
      .post('/api/member/change-password', {
        username: memberInfo.username, // 사용자 아이디로 비밀번호 변경 요청
        newPassword: passwords.newPassword,
        confirmPassword: passwords.confirmPassword,
      })
      .then((response) => {
        alert('비밀번호가 변경되었습니다!');
        navigate(-1); // 이전 페이지로 이동
      })
      .catch((error) => {
        console.error("Error changing password:", error);
        alert('Failed to change password.');
      });
  };

  const handleCancel = () => {
    navigate(-1); // 이전 페이지로 이동
  };

  return (
    <div className="container">
      <h1>비밀번호 변경</h1>
      <p><strong>아이디:</strong> {memberInfo.username}</p>
      <p><strong>닉네임:</strong> {memberInfo.nickname}</p>
      <form onSubmit={handleChangePassword}>
        <div className="form-group">
          <label>새 비밀번호:</label>
          <input
            type="password"
            name="newPassword"
            value={passwords.newPassword}
            onChange={handleInputChange}
            className="form-control"
            placeholder="영문자, 숫자, 특수문자 포함 8~20자"
            required
          />
              {passwordError && <p style={{ color: 'blue' }}>{passwordError}</p>} {/* 비밀번호 오류 메시지 */}
        </div>
        <div className="form-group">
          <label>비밀번호 확인:</label>
          <input
            type="password"
            name="confirmPassword"
            value={passwords.confirmPassword}
            onChange={handleInputChange}
            className="form-control"
            placeholder="비밀번호를 확인해 주세요"
            required
          />
              {confirmPasswordError && <p style={{ color: 'blue' }}>{confirmPasswordError}</p>} {/* 비밀번호 확인 오류 메시지 */}
        </div>
        <div className="form-group" style={{ marginTop: '10px' }}>
          <button type="submit" className="btn btn-primary">확인</button>
          <button type="button" onClick={handleCancel} className="btn btn-secondary" style={{ marginLeft: '10px' }}>취소</button>
        </div>
      </form>
    </div>
  );
};

export default ChangePassword;
