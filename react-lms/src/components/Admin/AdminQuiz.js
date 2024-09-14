import React, { useState, useEffect, useCallback } from 'react';
import { Form, Table, Button } from 'react-bootstrap';
import { useNavigate, useLocation } from 'react-router-dom';
import CustomPagination from '../CustomPagination';
import api from '../Api';

const AdminQuiz = () => {
    const navigate = useNavigate();
    const location = useLocation();

    const getInitialPage = () => {
      const searchParams = new URLSearchParams(location.search);
      return parseInt(searchParams.get('page') || '1', 10);
    };

    const [quizzes, setQuizzes] = useState([]);
    const [totalPages, setTotalPages] = useState(0);
    const [searchTerm, setSearchTerm] = useState('');
    const [difficulty, setDifficulty] = useState('');
    const [currentPage, setCurrentPage] = useState(getInitialPage);
  
    const fetchQuizzes = useCallback(async () => {
      try {
        const response = await api.get('/api/quiz/list', {
          params: {
            page: currentPage - 1,
            search: searchTerm,
            difficulty: difficulty
          }
        });
        setQuizzes(response.data.content);
        setTotalPages(response.data.totalPages);
      } catch (error) {
        console.error('Error fetching quizzes:', error);
      }
    }, [currentPage, searchTerm, difficulty]);
    
    useEffect(() => {
      fetchQuizzes();
    }, [fetchQuizzes]);
  
    useEffect(() => {
      navigate(`?page=${currentPage}`, {replace: true});
    }, [currentPage, navigate]);
    
    const handleQuizClick = (quizId) => {
      // 여기서 퀴즈 수정 페이지로 이동하도록 변경할 수 있습니다.
      navigate(`/admin/quiz/edit/${quizId}`);
    };
  
    const handlePageChange = (pageNumber) => {
      setCurrentPage(pageNumber);
    };

    const handleAddQuiz = () => {
      // 퀴즈 등록 페이지로 이동
      navigate('/admin/quiz/edit');
    };

  return (
    <div className='container mt-4'>
      <h1 className="mb-4 text-color">퀴즈 관리</h1>
      
      <Button variant="primary" className="mb-3" onClick={handleAddQuiz}>
        퀴즈 등록
      </Button>

      <Form className="mb-4">
        <Form.Group className="mb-3">
          <Form.Control 
            type="text" 
            placeholder="퀴즈 제목 검색" 
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </Form.Group>
        
        <div className="d-flex">
          <Form.Select 
            className="me-2" 
            value={difficulty}
            onChange={(e) => setDifficulty(e.target.value)}
          >
            <option value="">난이도</option>
            <option value="Lv. 1">Lv. 1</option>
            <option value="Lv. 2">Lv. 2</option>
            <option value="Lv. 3">Lv. 3</option>
            <option value="Lv. 4">Lv. 4</option>
            <option value="Lv. 5">Lv. 5</option>
          </Form.Select>
        </div>
      </Form>

      <div className="mb-3">
        <span>{quizzes.length} 문제</span>
      </div>

      <Table striped bordered hover>
        <thead>
          <tr>
            <th>제목</th>
            <th>난이도</th>
            <th>완료한 사람</th>
            <th>정답률</th>
          </tr>
        </thead>
        <tbody>
          {quizzes.map((quiz) => (
            <tr key={quiz.quizId} onClick={() => handleQuizClick(quiz.quizId)} style={{cursor: 'pointer'}}>
              <td>{quiz.title}</td>
              <td>{quiz.quizRank}</td>
              <td>{quiz.solvedBy}명</td>
              <td>{quiz.correctRate}</td>
            </tr>
          ))}
        </tbody>
      </Table>

      <CustomPagination
        currentPage={currentPage}
        totalPages={totalPages}
        onPageChange={handlePageChange}
      />
    </div>
  );
};

export default AdminQuiz;