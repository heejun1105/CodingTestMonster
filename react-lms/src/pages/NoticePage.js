import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import ReactMarkdown from 'react-markdown';
import { Card, Spinner, Alert } from 'react-bootstrap';

const NoticeDetail = () => {
    const { id } = useParams();
    const [notice, setNotice] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const formatDate = (dateString) => {
        const date = new Date(dateString);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        
        return `${year}-${month}-${day} ${hours}:${minutes}`;
    };
    
    useEffect(() => {
        const fetchNotice = async () => {
            try {
                const response = await axios.get(`/api/notice/${id}`);
                setNotice(response.data);
                setLoading(false);
            } catch (err) {
                setError('공지사항을 불러오는 데 실패했습니다.');
                setLoading(false);
            }
        };

        fetchNotice();
    }, [id]);

    if (loading) return (
        <div className="d-flex justify-content-center align-items-center" style={{height: '100vh'}}>
            <Spinner animation="border" role="status">
                <span className="visually-hidden">로딩 중...</span>
            </Spinner>
        </div>
    );

    if (error) return <Alert variant="danger">{error}</Alert>;
    if (!notice) return <Alert variant="warning">공지사항을 찾을 수 없습니다.</Alert>;

    return (
        <div className='container mt-5'>
            <Card className="shadow-sm">
                <Card.Body>
                    <Card.Title as="h1" className="mb-4">{notice.title}</Card.Title>
                    <Card.Subtitle className="mb-3 text-muted">
                        작성일: {formatDate(notice.createDate)}
                    </Card.Subtitle>
                    <Card.Text as="div">
                        <ReactMarkdown className='markdown-body'>{notice.content}</ReactMarkdown>
                    </Card.Text>
                </Card.Body>
            </Card>
        </div>
    );
};

export default NoticeDetail;