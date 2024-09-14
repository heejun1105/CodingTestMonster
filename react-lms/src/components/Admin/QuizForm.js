import React, { useState, useEffect, useCallback } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Form, Button } from 'react-bootstrap';
import MdEditor from 'react-markdown-editor-lite';
import MarkdownIt from 'markdown-it';
import 'react-markdown-editor-lite/lib/index.css';
import CodeMirror from '@uiw/react-codemirror';
import { javascript } from '@codemirror/lang-javascript';
import { oneDark } from '@codemirror/theme-one-dark';
import api from '../Api';

const mdParser = new MarkdownIt();

const QuizForm = () => {
    const { quizId } = useParams();
    const navigate = useNavigate();
    const [quiz, setQuiz] = useState({
        title: '',
        content: '',
        quizRank: '',
        correct: '',
        output: '',
    });

    const fetchQuiz = useCallback(async () => {
        if (!quizId) return;
        try {
            const response = await api.get(`/api/quiz/detail/${quizId}`);
            setQuiz(response.data);
        } catch (error) {
            console.error('퀴즈 불러오기 중 오류 발생:', error);
            // 에러 처리 로직 (예: 사용자에게 알림)
        }
    }, [quizId]);

    useEffect(() => {
        fetchQuiz();
    }, [fetchQuiz]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setQuiz(prevQuiz => ({
            ...prevQuiz,
            [name]: value
        }));
    };

    const handleEditorChange = ({ text }) => {
        setQuiz(prevQuiz => ({
            ...prevQuiz,
            content: text
        }));
    };

    const handleCorrectChange = (value) => {
        setQuiz(prevQuiz => ({
            ...prevQuiz,
            correct: value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            let response;
            if (quizId) {
                // 퀴즈 수정 로직
                response = await api.put(`/api/quiz/edit/${quizId}`, quiz, {
                    headers: {
                        'Content-Type': 'application/json',
                    },
                });
            } else {
                // 퀴즈 등록 로직
                response = await api.post('/api/quiz/edit', quiz, {
                    headers: {
                        'Content-Type': 'application/json',
                    },
                });
            }
            console.log('서버 응답:', response.data);
            navigate('/admin/quiz'); // 퀴즈 등록/수정 후 이동할 페이지
        } catch (error) {
            console.error('퀴즈 저장 중 오류 발생:', error);
            // 에러 처리 로직 (예: 사용자에게 알림)
        }
    };

    return (
        <div className="container mt-4">
            <h1>{quizId ? '퀴즈 수정' : '퀴즈 등록'}</h1>
            <Form onSubmit={handleSubmit}>
                <Form.Group className="mb-3">
                    <Form.Label>제목</Form.Label>
                    <Form.Control
                        type="text"
                        name="title"
                        value={quiz.title}
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
                        value={quiz.content}
                    />
                </Form.Group>
                <Form.Group className="mb-3">
                    <Form.Label>난이도</Form.Label>
                    <Form.Select
                        name="quizRank"
                        value={quiz.quizRank}
                        onChange={handleChange}
                        required
                    >
                        <option value="">선택하세요</option>
                        <option value="A">A</option>
                        <option value="B">B</option>
                        <option value="C">C</option>
                        <option value="D">D</option>
                        <option value="E">E</option>
                    </Form.Select>
                </Form.Group>
                <Form.Group className="mb-3">
                    <Form.Label>정답</Form.Label>
                    <CodeMirror
                        value={quiz.correct}
                        height="200px"
                        extensions={[javascript({ jsx: true })]}
                        theme={oneDark}
                        onChange={handleCorrectChange}
                    />
                </Form.Group>
                <Form.Group className='mb-3'>
                    <Form.Label>Output</Form.Label>
                    <Form.Control
                        type='text'
                        name='output'
                        value={quiz.output}
                        onChange={handleChange}
                        required
                    />
                </Form.Group>
                <Button variant="primary" type="submit">
                    {quizId ? '수정' : '등록'}
                </Button>
            </Form>
        </div>
    );
};

export default QuizForm;