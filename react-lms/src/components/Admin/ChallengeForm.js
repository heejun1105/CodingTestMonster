import React, { useState, useEffect, useCallback } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Form, Button } from 'react-bootstrap';
import MdEditor from 'react-markdown-editor-lite';
import MarkdownIt from 'markdown-it';
import 'react-markdown-editor-lite/lib/index.css';
import api from '../Api';

const mdParser = new MarkdownIt();

const ChallengeForm = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [challenge, setChallenge] = useState({
    title: '',
    content: '',
    expPoints: 0,
    close: false,
  });

  const fetchChallenge = useCallback(async () => {
    if (!id) return;
    try {
      const response = await api.get(`/api/challenges/${id}`);
      setChallenge(response.data);
    } catch (error) {
      console.error('챌린지 불러오기 중 오류 발생:', error);
      // 에러 처리 로직 (예: 사용자에게 알림)
    }
  }, [id]);

  useEffect(() => {
    fetchChallenge();
  }, [fetchChallenge]);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setChallenge(prevChallenge => ({
      ...prevChallenge,
      [name]: type === 'checkbox' ? checked : value
    }));
  };

  const handleEditorChange = ({ text }) => {
    setChallenge(prevChallenge => ({
      ...prevChallenge,
      content: text
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      let response;
      if (id) {
        // 챌린지 수정 로직
        response = await api.put(`/api/challenges/${id}`, challenge);
      } else {
        // 챌린지 등록 로직
        response = await api.post('/api/challenges', challenge);
      }
      console.log('서버 응답:', response.data);
      navigate('/admin/challenges'); // 챌린지 등록/수정 후 이동할 페이지
    } catch (error) {
      console.error('챌린지 저장 중 오류 발생:', error);
      // 에러 처리 로직 (예: 사용자에게 알림)
    }
  };

    return (
        <div className="container mt-4">
          <h1>{id ? '챌린지 수정' : '챌린지 등록'}</h1>
          <Form onSubmit={handleSubmit}>
            
            <Form.Group className="mb-3">
              <Form.Label>제목</Form.Label>
              <Form.Control
                type="text"
                name="title"
                value={challenge.title}
                onChange={handleChange}
                required
              />
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>내용</Form.Label>
              <MdEditor
                style={{ height: '400px' }}
                renderHTML={(text) => mdParser.render(text)}
                onChange={handleEditorChange}
                value={challenge.content}
              />
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>경험치</Form.Label>
              <Form.Control
                type="number"
                name="expPoints"
                value={challenge.expPoints}
                onChange={handleChange}
                required
              />
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Check
                type="checkbox"
                label="마감"
                name="close"
                checked={challenge.close}
                onChange={handleChange}
              />
            </Form.Group>

            <Button variant="primary" type="submit">
              {id ? '수정' : '등록'}
            </Button>
        </Form>
        </div>
    );
};

export default ChallengeForm;