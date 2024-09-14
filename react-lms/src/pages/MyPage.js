import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { Modal, Button, Form, Table } from 'react-bootstrap';
import { useAuth } from '../AuthContext';
import { format } from 'date-fns';

const MyPage = () => {
  const [memberInfo, setMemberInfo] = useState({
    username: '',
    nickname: '',
    email: '',
    rank: '',
    expPoints: '',
  });
  const [quizAnswers, setQuizAnswers] = useState([]); // 퀴즈 데이터를 저장할 상태
  const [currentPage, setCurrentPage] = useState(0);  // 현재 페이지 상태
  const [totalPages, setTotalPages] = useState(0);    // 총 페이지 수
  const [showModal, setShowModal] = useState(false);
  const [password, setPassword] = useState('');
  const [confirmText, setConfirmText] = useState('');
  const [error, setError] = useState('');
  const [selectedTab, setSelectedTab] = useState('correct');

  const navigate = useNavigate();
  const { logout } = useAuth();

  useEffect(() => {
    // API 호출하여 사용자 정보 가져오기
    axios
      .get('/api/member/mypage')
      .then((response) => {
        setMemberInfo(response.data);
      })
      .catch((error) => {
        console.error("Error fetching member data:", error);
      });
  }, []);

  // 맞춘 문제 또는 틀린 문제 목록 가져오기
  useEffect(() => {
    fetchQuizzes(currentPage);
  }, [currentPage, selectedTab]);

  // 맞춘 문제 또는 틀린 문제를 가져오는 API 호출
  const fetchQuizzes = (page) => {
    const url =
      selectedTab === 'correct'
        ? '/api/member/my-correct-quizs'
        : '/api/member/my-incorrect-quizs';
    axios
      .get(url, {
          params: { page: page }
        })
        .then((response) => {
          setQuizAnswers(response.data.content); // 현재 페이지의 퀴즈 데이터 설정
          setTotalPages(response.data.totalPages); // 전체 페이지 수 설정
        })
        .catch((error) => {
          console.error("Error fetching quiz answers:", error);
          setError("퀴즈 데이터를 불러오는 중 오류가 발생했습니다.");
        });
    };
  
    // 페이지 변경 핸들러
    const handleNextPage = () => {
      if (currentPage < totalPages - 1) {
        setCurrentPage(currentPage + 1);
      }
    };
  
    const handlePrevPage = () => {
      if (currentPage > 0) {
        setCurrentPage(currentPage - 1);
      }
    };

    const handleQuizClick = (quizId) => {
      navigate(`/coding-page/${quizId}`);
    };
  
  const handleChangePasswordClick = () => {
    navigate('/change-password'); // 비밀번호 변경 페이지로 이동
  };
  
  const handleWithdrawalClick = () => {
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setShowModal(false);
    setPassword('');
    setConfirmText('');
    setError('');
  };

  const handleWithdrawal = async () => {
    if (confirmText !== '회원탈퇴') {
      setError('올바른 확인 텍스트를 입력해주세요.');
      return;
    }

    try {
      await axios.post('/api/member/withdraw', { password });
      alert('회원 탈퇴가 완료되었습니다.');
      await logout(); // 회원탈퇴 성공 후 로그아웃 처리
      navigate('/'); // 홈페이지로 이동
    } catch (error) {
      setError('비밀번호가 올바르지 않거나 탈퇴 처리 중 오류가 발생했습니다.');
    }
  };

  const handleTabChange = (tab) => {
    setSelectedTab(tab);
    setCurrentPage(0); // 탭 변경 시 페이지 초기화
  };

  return (
<div className='container-fluid'>
  <div className='container'>
    <div className="row">
      {/* 회원 정보 */}
      <div className="col-md-6">
        <h1>My Page</h1>
        <p><strong>아이디:</strong> {memberInfo.username}</p>
        <p><strong>닉네임:</strong> {memberInfo.nickname}</p>
        <p><strong>Email:</strong> {memberInfo.email}</p>
        <p><strong>등급:</strong> {memberInfo.rank}</p>
        <p><strong>경험치:</strong> {memberInfo.expPoints}</p>

        {/* 비밀번호 변경 및 회원 탈퇴 버튼 */}
        <div className="d-flex justify-content-start">
          <button onClick={handleChangePasswordClick} className="btn btn-primary me-2">비밀번호 변경</button>
          <button onClick={handleWithdrawalClick} className="btn btn-danger">회원 탈퇴</button>
        </div>
      </div>

          {/* 맞춘 문제와 틀린 문제 탭 */}
          <div className="container mt-4">
            <h2>내가 푼 문제</h2>

            {/* 탭 버튼 */}
            <div className="d-flex mb-3">
              <button
                className={`btn ${selectedTab === 'correct' ? 'btn-primary' : 'btn-secondary'} me-2`}
                onClick={() => handleTabChange('correct')}
              >
                맞춘 문제
              </button>
              <button
                className={`btn ${selectedTab === 'incorrect' ? 'btn-primary' : 'btn-secondary'}`}
                onClick={() => handleTabChange('incorrect')}
              >
                틀린 문제
              </button>
            </div>

            {/* 퀴즈 목록 */}
        {quizAnswers.length > 0 ? (
          <>
            <Table striped bordered hover>
              <thead>
                <tr>
                  <th>제목</th>
                  <th>난이도</th>
                  <th>날짜</th>
                </tr>
              </thead>
              <tbody>
                {quizAnswers.map((quiz, index) => (
                  <tr key={index} onClick={() => handleQuizClick(quiz.quizId)} style={{ cursor: 'pointer' }}>
                    <td>{quiz.title}</td>
                    <td>{quiz.quizRank}</td>
                    <td>{format(new Date(quiz.solvedQuizTime), 'yyyy-MM-dd HH:mm:ss')}</td>
                  </tr>
                ))}
              </tbody>
            </Table>

            {/* 페이징 버튼 */}
            <div className="pagination">
              <button onClick={handlePrevPage} disabled={currentPage === 0} className="btn btn-secondary me-2">
                이전
              </button>
              <span>페이지 {currentPage + 1} / {totalPages}</span>
              <button onClick={handleNextPage} disabled={currentPage === totalPages - 1} className="btn btn-secondary ms-2">
                다음
              </button>
            </div>
          </>
        ) : (
          <p>{selectedTab === 'correct' ? '맞춘 문제가 없습니다.' : '틀린 문제가 없습니다.'}</p>
        )}
      </div>
    </div>

        <Modal show={showModal} onHide={handleCloseModal}>
          <Modal.Header closeButton>
            <Modal.Title>회원 탈퇴</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form>
              <Form.Group className="mb-3">
                <Form.Label>비밀번호</Form.Label>
                <Form.Control
                  type="password"
                  placeholder="비밀번호를 입력하세요"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                />
              </Form.Group>
              <Form.Group className="mb-3">
                <Form.Label>확인을 위해 "회원탈퇴"를 입력하세요</Form.Label>
                <Form.Control
                  type="text"
                  placeholder="회원탈퇴"
                  value={confirmText}
                  onChange={(e) => setConfirmText(e.target.value)}
                />
              </Form.Group>
              {error && <p className="text-danger">{error}</p>}
            </Form>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={handleCloseModal}>
              취소
            </Button>
            <Button variant="danger" onClick={handleWithdrawal}>
              탈퇴 확인
            </Button>
          </Modal.Footer>
        </Modal>
      </div>
    </div>
  );
};

export default MyPage;
