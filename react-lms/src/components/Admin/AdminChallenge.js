import React, { useState, useEffect, useCallback } from 'react';
import { Form, Table, Button } from 'react-bootstrap';
import { useNavigate, useLocation } from 'react-router-dom';
import CustomPagination from '../CustomPagination';
import api from '../Api';

const ChallengeAdmin = () => {
    const navigate = useNavigate();
    const location = useLocation();

    const getInitialPage = () => {
        const searchParams = new URLSearchParams(location.search);
        return parseInt(searchParams.get('page') || '1', 10);
    };

    const [challenges, setChallenges] = useState([]);
    const [totalPages, setTotalPages] = useState(0);
    const [searchTerm, setSearchTerm] = useState('');
    const [currentPage, setCurrentPage] = useState(getInitialPage);

    const fetchChallenges = useCallback(async () => {
        try {
            const response = await api.get('/api/challenges', {
                params: {
                    page: currentPage - 1
                }
            });
            setChallenges(response.data.content);
            setTotalPages(response.data.totalPages);
        } catch (error) {
            console.error('Error fetching challenges:', error);
        }
    }, [currentPage, searchTerm]);

    useEffect(() => {
        fetchChallenges();
    }, [fetchChallenges]);

    useEffect(() => {
        navigate(`?page=${currentPage}`, { replace: true });
    }, [currentPage, navigate]);

    const handleChallengeClick = (challengeId) => {
        navigate(`/admin/challenges/edit/${challengeId}`);
    };

    const handlePageChange = (pageNumber) => {
        setCurrentPage(pageNumber);
    };

    const handleAddChallenge = () => {
        navigate('/admin/challenges/edit');
    };

    return (
        <div className='container mt-4'>
            <h1 className="mb-4 text-color">챌린지 관리</h1>

            <Button variant="primary" className="mb-3" onClick={handleAddChallenge}>
                챌린지 등록
            </Button>

            <Form className="mb-4">
                <Form.Group className="mb-3">
                    <Form.Control
                        type="text"
                        placeholder="챌린지 제목 검색"
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                    />
                </Form.Group>
            </Form>

            <div className="mb-3">
                <span>{challenges.length} 챌린지</span>
            </div>

            <Table striped bordered hover>
                <thead>
                    <tr>
                        <th>제목</th>
                        <th>내용</th>
                        <th>경험치</th>
                        <th>상태</th>
                    </tr>
                </thead>
                <tbody>
                    {challenges.map((challenge) => (
                        <tr key={challenge.id} onClick={() => handleChallengeClick(challenge.id)} style={{cursor: 'pointer'}}>
                            <td>{challenge.title}</td>
                            <td>{challenge.content}</td>
                            <td>{challenge.expPoints}</td>
                            <td>{challenge.close ? '종료' : '진행중'}</td>
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

export default ChallengeAdmin;