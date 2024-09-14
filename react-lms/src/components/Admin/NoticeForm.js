import React, { useState, useEffect, useCallback } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Form, Button } from 'react-bootstrap';
import MdEditor from 'react-markdown-editor-lite';
import MarkdownIt from 'markdown-it';
import 'react-markdown-editor-lite/lib/index.css';
import api from '../Api';

const mdParser = new MarkdownIt();

const NoticeForm = () => {
    const { noticeId } = useParams();
    const navigate = useNavigate();
    const [notice, setNotice] = useState({
        title: '',
        content: '',
    });

    const fetchNotice = useCallback(async () => {
        if (!noticeId) return;
        try {
            const response = await api.get(`/api/notice/${noticeId}`);
            setNotice(response.data);
        } catch (error) {
            console.error('공지사항 불러오기 중 오류 발생:', error);
            // Error handling logic (e.g., notify user)
        }
    }, [noticeId]);

    useEffect(() => {
        fetchNotice();
    }, [fetchNotice]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setNotice(prevNotice => ({
            ...prevNotice,
            [name]: value
        }));
    };

    const handleEditorChange = ({ text }) => {
        setNotice(prevNotice => ({
            ...prevNotice,
            content: text
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            let response;
            
            if (noticeId) {
                // Update existing notice
                response = await api.put(`/api/notice/update/${noticeId}`, notice);
            } else {
                // Create new notice
                response = await api.post(`/api/notice/create`, notice);
            }
            console.log('서버 응답:', response.data);
            navigate('/admin/notice'); // Redirect to notices list after creation/update
        } catch (error) {
            console.error('공지사항 저장 중 오류 발생:', error);
            alert('공지사항 저장에 실패했습니다. 다시 시도해주세요.');
        }
    };

    return (
        <div className="container mt-4">
            <h1>{noticeId ? '공지사항 수정' : '공지사항 등록'}</h1>
            <Form onSubmit={handleSubmit}>
                <Form.Group className="mb-3">
                    <Form.Label>제목</Form.Label>
                    <Form.Control
                        type="text"
                        name="title"
                        value={notice.title}
                        onChange={handleChange}
                        required
                    />
                </Form.Group>
                <Form.Group className="mb-3">
                    <Form.Label>내용</Form.Label>
                    <MdEditor
                        style={{ height: '300px' }}
                        renderHTML={(text) => mdParser.render(text)}
                        onChange={handleEditorChange}
                        value={notice.content}
                    />
                </Form.Group>
                <Button variant="primary" type="submit">
                    {noticeId ? '수정' : '등록'}
                </Button>
            </Form>
        </div>
    );
};

export default NoticeForm;