import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import ReactMarkdown from 'react-markdown';
import { Card, Badge, Spinner, Alert } from 'react-bootstrap';

const ChallengeDetail = () => {
    const { id } = useParams();
    const [challenge, setChallenge] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchChallenge = async () => {
            try {
                const response = await axios.get(`/api/challenges/${id}`);
                setChallenge(response.data);
                setLoading(false);
            } catch (err) {
                setError('챌린지를 불러오는 데 실패했습니다.');
                setLoading(false);
            }
        };

        fetchChallenge();
    }, [id]);

    if (loading) return (
        <div className="d-flex justify-content-center align-items-center" style={{height: '100vh'}}>
            <Spinner animation="border" role="status">
                <span className="visually-hidden">로딩 중...</span>
            </Spinner>
        </div>
    );

    if (error) return <Alert variant="danger">{error}</Alert>;
    if (!challenge) return <Alert variant="warning">챌린지를 찾을 수 없습니다.</Alert>;

    return (
        <div className='container mt-5'>
            <Card className="shadow-sm">
                <Card.Body>
                    <Card.Title as="h1" className="mb-4 text-color">{challenge.title}</Card.Title>
                    <Card.Subtitle className="mb-3 text-white">
                        경험치: <Badge bg="primary">{challenge.expPoints} XP</Badge>
                    </Card.Subtitle>
                    <Card.Text as="div">
                        <ReactMarkdown className='markdown-body text-color'>{challenge.content}</ReactMarkdown>
                    </Card.Text>
                    {challenge.close && (
                        <Alert variant="info" className="mt-3">
                            이 챌린지는 마감되었습니다.
                        </Alert>
                    )}
                </Card.Body>
            </Card>
        </div>
    );
};

export default ChallengeDetail;