import React, { useState, useEffect, useCallback } from 'react';
import { Form, Table, Button } from 'react-bootstrap';
import { useNavigate, useLocation } from 'react-router-dom';
import CustomPagination from '../CustomPagination';
import api from '../Api';

const AdminNotice = () => {
    const navigate = useNavigate();
    const location = useLocation();

    const getInitialPage = () => {
        const searchParams = new URLSearchParams(location.search);
        return parseInt(searchParams.get('page') || '1', 10);
    };

    const [notices, setNotices] = useState([]);
    const [totalPages, setTotalPages] = useState(0);
    const [searchTerm, setSearchTerm] = useState('');
    const [currentPage, setCurrentPage] = useState(getInitialPage);

    const formatDate = (dateString) => {
        const date = new Date(dateString);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        
        return `${year}-${month}-${day} ${hours}:${minutes}`;
    };

    const fetchNotices = useCallback(async () => {
        try {
            const response = await api.get('/api/notice/list', {
                params: {
                    page: currentPage - 1
                }
            });
            setNotices(response.data.content);
            setTotalPages(response.data.totalPages);
        } catch (error) {
            console.error('Error fetching notices:', error);
        }
    }, [currentPage, searchTerm]);

    useEffect(() => {
        fetchNotices();
    }, [fetchNotices]);

    useEffect(() => {
        navigate(`?page=${currentPage}`, { replace: true });
    }, [currentPage, navigate]);

    const handleNoticeClick = (noticeId) => {
        navigate(`/admin/notice/edit/${noticeId}`);
    };

    const handlePageChange = (pageNumber) => {
        setCurrentPage(pageNumber);
    };

    const handleAddNotice = () => {
        navigate('/admin/notice/edit');
    };

    const handleDeleteNotice = async (noticeId) => {
        if (window.confirm('Are you sure you want to delete this notice?')) {
            try {
                await api.delete(`/api/notice/delete/${noticeId}`);
                fetchNotices();
            } catch (error) {
                console.error('Error deleting notice:', error);
            }
        }
    };

    return (
        <div className='container mt-4'>
            <h1 className="mb-4 text-color">공지사항 관리</h1>

            <Button variant="primary" className="mb-3" onClick={handleAddNotice}>
                공지사항 등록
            </Button>

            <Form className="mb-4">
                <Form.Group className="mb-3">
                    <Form.Control
                        type="text"
                        placeholder="공지사항 제목 검색"
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                    />
                </Form.Group>
            </Form>

            <div className="mb-3">
                <span>{notices.length} 공지사항</span>
            </div>

            <Table striped bordered hover>
                <thead>
                    <tr>
                        <th>제목</th>
                        <th>작성일</th>
                        <th>액션</th>
                    </tr>
                </thead>
                <tbody>
                    {notices.map((notice) => (
                        <tr key={notice.noticeId}>
                            <td onClick={() => handleNoticeClick(notice.noticeId)} style={{cursor: 'pointer'}}>{notice.title}</td>
                            <td>{formatDate(notice.createDate)}</td>
                            <td>
                                <Button variant="danger" size="sm" onClick={() => handleDeleteNotice(notice.noticeId)}>삭제</Button>
                            </td>
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

export default AdminNotice;