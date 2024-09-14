import React, { useState, useEffect, useCallback } from 'react';
import { Table } from 'react-bootstrap';
import { useNavigate, useLocation } from 'react-router-dom';
import CustomPagination from '../components/CustomPagination';
import api from '../components/Api';

const SolvedQuizzes = () => {
    const navigate = useNavigate();
    const location = useLocation();

    const getInitialPage = () => {
        const searchParams = new URLSearchParams(location.search);
        return parseInt(searchParams.get('page') || '1', 10);
    };

    const [quizzes, setQuizzes] = useState([]);
    const [totalPages, setTotalPages] = useState(0);
    const [currentPage, setCurrentPage] = useState(getInitialPage);

    const fetchQuizzes = useCallback(async () => {
        try {
            const response = await api.get('/api/quizAnswer/solved', {
                params: {
                    page: currentPage - 1
                }
            });
            setQuizzes(response.data.content);
            setTotalPages(response.data.totalPages);
        } catch (error) {
            console.error('Error fetching solved quizzes:', error);
        }
    }, [currentPage]);

    useEffect(() => {
        fetchQuizzes();
    }, [fetchQuizzes]);

    useEffect(() => {
        navigate(`?page=${currentPage}`, { replace: true });
    }, [currentPage, navigate]);

    const handleQuizClick = (quizId) => {
        navigate(`/coding-page/${quizId}`);
    };

    const handlePageChange = (pageNumber) => {
        setCurrentPage(pageNumber);
    };

    return (
        <div className='container mt-4'>
            <h1 className="mb-4">Solved Quizzes</h1>

            <div className="mb-3">
                <span>{quizzes.length} 문제</span>
            </div>

            <Table striped bordered hover>
                <thead>
                    <tr>
                        <th>제목</th>
                        <th>난이도</th>
                        <th>시도 횟수</th>
                        <th>정답률</th>
                    </tr>
                </thead>
                <tbody>
                    {quizzes.map((quiz) => (
                        <tr key={quiz.quizId} onClick={() => handleQuizClick(quiz.quizId)} style={{cursor: 'pointer'}}>
                            <td>{quiz.title}</td>
                            <td>{quiz.quizRank}</td>
                            <td>{quiz.solvedBy}회</td>
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

export default SolvedQuizzes;