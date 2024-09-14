import React, { useState, useRef, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Controlled as CodeMirror } from 'react-codemirror2';
import axios from 'axios';
import { useAuth } from '../AuthContext';
import { MessageCircle, Send } from 'lucide-react';
import ReactMarkdown from 'react-markdown';
import api from '../components/Api';

import 'codemirror/lib/codemirror.css';
import 'codemirror/theme/material.css';
import 'codemirror/mode/javascript/javascript';

// 새로운 스타일 추가
const customMarkdownStyle = `
  .markdown-body {
    font-size: 16px;
    color: #E0E0E0;
  }
  .markdown-body h1,
  .markdown-body h2,
  .markdown-body h3,
  .markdown-body h4,
  .markdown-body h5,
  .markdown-body h6 {
    color: #FFFFFF;
  }
  .markdown-body p,
  .markdown-body li {
    color: #fff;
  }
  .markdown-body code {
    background-color: #37474F;
    color: #FF8A65;
  }
  .markdown-body pre {
    background-color: #263238;
  }
`;

const CodingTestPage = () => {
  const navigate = useNavigate();
  const [code, setCode] = useState("");
  const [output, setOutput] = useState("");
  const [problem, setProblem] = useState(null);
  const [isPublic, setIsPublic] = useState(false);
  const [isChatOpen, setIsChatOpen] = useState(false);
  const [messages, setMessages] = useState([]);
  const [inputMessage, setInputMessage] = useState('');
  const iframeRef = useRef(null);
  const { quizId } = useParams();
  const { username } = useAuth();

  const submitQuiz = async () => {
    try {
      const response = await axios.post('/api/quiz/submit', null, {
        params: {
          quizId,
          answer: encodeURIComponent(code),
          output: encodeURIComponent(output),
          isPublic,
          username
        }
      });
      
      alert(response.data ? "정답입니다!" : "틀렸습니다. 다시 시도해보세요.");
      if (response.data) {
        navigate('/coding-test');
      }
    } catch (error) {
      console.error('퀴즈 제출 중 오류 발생:', error);
      alert('퀴즈 제출에 실패했습니다. 다시 시도해주세요.');
    }
  };

  const fetchChatHistory = async () => {
    try {
      const response = await axios.get(`/api/chat-history/${quizId}`);
      const chatHistory = response.data.map(chat => ([
        { text: chat.memberContent, sender: 'user' },
        { text: chat.botContent, sender: 'bot' }
      ])).flat();
      setMessages(chatHistory);
    } catch (error) {
      console.error('Error fetching chat history:', error);
      // 오류 발생 시 사용자에게 알림
    }
  };
  
  useEffect(() => {
    const fetchProblem = async () => {
      try {
        const response = await axios.get(`/api/quiz/detail/${quizId}`);
        setProblem(response.data);
        
        const answerResponse = await api.get(`/api/quizAnswer/${quizId}`);
        
        // quizAnswer가 있으면 code를 설정, 없으면 기본 코드 설정
        if (answerResponse.data && answerResponse.data.answer) {
          setCode(decodeURIComponent(answerResponse.data.answer));
        } else {
          setCode("// Write your code here\nconsole.log('Hello, World!');");
        }
      } catch (error) {
        console.error('Error fetching problem:', error);
      }
    };
  
    fetchChatHistory();
    fetchProblem();
  }, [quizId]);

  useEffect(() => {
    const iframe = iframeRef.current;
    if (iframe) {
      iframe.srcdoc = `
        <html>
          <head>
            <script>
              function runCode(code) {
                try {
                  console.log = function(msg) { 
                    window.parent.postMessage(msg, '*'); 
                  };
                  eval(code);
                } catch (error) {
                  window.parent.postMessage('Error: ' + error.message, '*');
                }
              }
            </script>
          </head>
          <body></body>
        </html>
      `;
    }
  }, []);

  useEffect(() => {
    const handleMessage = (event) => {
      setOutput(prevOutput => prevOutput + event.data + '\n');
    };
    window.addEventListener('message', handleMessage);
    return () => window.removeEventListener('message', handleMessage);
  }, []);

  const runCode = () => {
    setOutput("");
    const iframe = iframeRef.current;
    if (iframe && iframe.contentWindow) {
      iframe.contentWindow.runCode(code);
    }
  };

  //비동기방식
  // const sendMessage = async () => {
  //   if (inputMessage.trim() !== '') {
  //     const newUserMessage = { text: inputMessage, sender: 'user' };
  //     setMessages(prevMessages => [...prevMessages, newUserMessage]);
  //     setInputMessage('');
  
  //     // 로딩 메시지 추가
  //     const loadingMessage = { text: '답변을 생성 중입니다...', sender: 'bot', isLoading: true };
  //     setMessages(prevMessages => [...prevMessages, loadingMessage]);
  
  //     try {
  //       const response = await fetch(`/ai/generateStream?quizId=${quizId}&message=${encodeURIComponent(inputMessage)}`, {
  //         method: 'GET',
  //         headers: {
  //           'Accept': 'text/event-stream',
  //         },
  //       });
  
  //       // 로딩 메시지 제거
  //       setMessages(prevMessages => prevMessages.filter(msg => !msg.isLoading));
  
  //       const reader = response.body.getReader();
  //       const decoder = new TextDecoder('utf-8');
  
  //       let botMessage = { text: '', sender: 'bot' };
  
  //       while (true) {
  //         const { done, value } = await reader.read();
  //         if (done) break;
  
  //         const chunk = decoder.decode(value);
  //         const lines = chunk.split('\n');
          
  //         for (const line of lines) {
  //           if (line.startsWith('data:')) {
  //             const data = line.slice(5).trim();
  //             if (data) {
  //               botMessage.text += data;
  
  //               setMessages(prevMessages => {
  //                 const updatedMessages = [...prevMessages];
  //                 if (updatedMessages[updatedMessages.length - 1].sender === 'bot') {
  //                   updatedMessages[updatedMessages.length - 1] = { ...botMessage };
  //                 } else {
  //                   updatedMessages.push({ ...botMessage });
  //                 }
  //                 return updatedMessages;
  //               });
  //             }
  //           }
  //         }
  //       }
  
  //     } catch (error) {
  //       console.error('Error details:', error);
  //       setMessages(prevMessages => prevMessages.filter(msg => !msg.isLoading));
  //       const errorMessage = { text: 'Sorry, I encountered an error.', sender: 'bot' };
  //       setMessages(prevMessages => [...prevMessages, errorMessage]);
  //     }
  //   }
  // };

  //동기방식
  const sendMessage = async () => {
    if (inputMessage.trim() !== '') {
      const newUserMessage = { text: inputMessage, sender: 'user' };
      setMessages(prevMessages => [...prevMessages, newUserMessage]);
      setInputMessage('');
  
      // 로딩 메시지 추가
      const loadingMessage = { text: '답변을 생성 중입니다...', sender: 'bot', isLoading: true };
      setMessages(prevMessages => [...prevMessages, loadingMessage]);
  
      try {
        const response = await axios.get('/ai/generate', {
          params: {
            quizId,
            message: inputMessage
          }
        });
        console.log('Server response:', response.data);
  
        // 로딩 메시지 제거
        setMessages(prevMessages => prevMessages.filter(msg => !msg.isLoading));
  
        // 응답을 여러 개의 메시지로 나누어 표시
        const botResponses = response.data.generation.split('\n').filter(text => text.trim() !== '');
        botResponses.forEach((text, index) => {
          setTimeout(() => {
            const botMessage = { text, sender: 'bot' };
            setMessages(prevMessages => [...prevMessages, botMessage]);
          }, index * 100); // 각 메시지를 100ms 간격으로 표시
        });
      } catch (error) {
        console.error('Error details:', error.response ? error.response.data : error.message);
        setMessages(prevMessages => prevMessages.filter(msg => !msg.isLoading));
        const errorMessage = { text: 'Sorry, I encountered an error.', sender: 'bot' };
        setMessages(prevMessages => [...prevMessages, errorMessage]);
      }
    }
  };


  // const sendMessage = async () => {
  //   if (inputMessage.trim() !== '') {
  //     const newUserMessage = { text: inputMessage, sender: 'user' };
  //     setMessages(prevMessages => [...prevMessages, newUserMessage]);
  //     setInputMessage('');

  //     try {
  //       const response = await axios.get('/ai/generate', {
  //         params: {
  //           quizId,
  //           message: inputMessage
  //         }
  //       });
  //       console.log('Server response:', response.data);
  //       const botMessage = { text: response.data.generation, sender: 'bot' };
  //       setMessages(prevMessages => [...prevMessages, botMessage]);
  //     } catch (error) {
  //       console.error('Error details:', error.response ? error.response.data : error.message);
  //       const errorMessage = { text: 'Sorry, I encountered an error.', sender: 'bot' };
  //       setMessages(prevMessages => [...prevMessages, errorMessage]);
  //     }
  //   }
  // };

  return (
    <div className="container-fluid vh-100 d-flex flex-column" style={{ backgroundColor: '#263238', color: '#B0BEC5' }}>
      <div className='container'>
        <div className="row flex-grow-1">
          {/* Problem Display */}
          <div className="col-md-6 p-4" style={{ borderRight: '1px solid #37474F', position: 'relative' }}>
            <h2 className="mb-4">Problem</h2>
            {problem ? (
              <>
                <style>{customMarkdownStyle}</style>
                <div style={{ maxHeight: 'calc(100vh - 200px)', overflowY: 'auto' }}>
                  <ReactMarkdown className="markdown-body">{problem.content}</ReactMarkdown>
                </div>
              </>
            ) : (
              <p>Loading problem...</p>
            )}
            
          </div>

          {/* Code Editor and Output */}
          <div className="col-md-6 p-4 d-flex flex-column">
            <h2 className="mb-4">Code Editor</h2>
            <div>
              <CodeMirror
                value={code}
                options={{
                  mode: 'javascript',
                  theme: 'material',
                  lineNumbers: true
                }}
                onBeforeChange={(editor, data, value) => setCode(value)}
              />
            </div>
            <div className="form-check mb-3">
              <input
                type="checkbox"
                checked={isPublic}
                onChange={(e) => setIsPublic(e.target.checked)}
                className="form-check-input"
                id="publicCheck"
              />
              <label className="form-check-label" htmlFor="publicCheck">
                정답 공개
              </label>
            </div>
            <div>
              <button onClick={runCode} className="btn btn-primary mx-3 mb-3">
                Run Code
              </button>
              <button onClick={submitQuiz} className='btn btn-secondary mb-3'>
                제출
              </button>
            </div>
            <div>
              <h3>Output:</h3>
              <pre className="p-2 rounded" style={{ backgroundColor: '#1E272C', color: '#B0BEC5' }}>{output}</pre>
            </div>
          </div>
        </div>
        <iframe 
          ref={iframeRef} 
          style={{display: 'none'}} 
          title="Code Execution Environment"
        ></iframe>
      </div>
      <div 
              onClick={() => setIsChatOpen(!isChatOpen)}
              style={{
                position: 'absolute',
                bottom: '20px',
                right: '20px',
                cursor: 'pointer',
                backgroundColor: '#4CAF50',
                borderRadius: '50%',
                width: '50px',
                height: '50px',
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
              }}
            >
              <MessageCircle size={24} color="white" />
            </div>
            {isChatOpen && (
              <div
                style={{
                  position: 'absolute',
                  bottom: '80px',
                  right: '20px',
                  width: '300px',
                  height: '400px',
                  backgroundColor: '#37474F',
                  borderRadius: '10px',
                  padding: '10px',
                  display: 'flex',
                  flexDirection: 'column',
                  
                }}
              >
                <div style={{ flexGrow: 1, overflowY: 'auto', marginBottom: '10px' }}>
                  {messages.map((msg, index) => (
                    <div key={index} style={{ marginBottom: '10px', textAlign: msg.sender === 'user' ? 'right' : 'left' }}>
                      <div className='text-white' style={{ 
                        backgroundColor: msg.sender === 'user' ? '#4CAF50' : '#2196F3', 
                        padding: '5px 10px', 
                        borderRadius: '20px',
                        display: 'inline-block',
                        maxWidth: '80%',
                        wordWrap: 'break-word'
                      }}>
                        <ReactMarkdown className="markdown-body">{msg.text}</ReactMarkdown>
                      </div>
                    </div>
                  ))}
                </div>
                <div style={{ display: 'flex' }}>
                  <input
                    type="text"
                    value={inputMessage}
                    onChange={(e) => setInputMessage(e.target.value)}
                    onKeyPress={(e) => e.key === 'Enter' && sendMessage()}
                    style={{ flexGrow: 1, marginRight: '10px', padding: '5px', borderRadius: '5px', border: 'none' }}
                    placeholder="Type a message..."
                  />
                  <button 
                    onClick={sendMessage} 
                    style={{ 
                      padding: '5px 10px', 
                      backgroundColor: '#4CAF50', 
                      color: 'white', 
                      border: 'none', 
                      borderRadius: '5px',
                      cursor: 'pointer'
                    }}
                  >
                    <Send size={20} />
                  </button>
                </div>
              </div>
            )}
    </div>
  );
};

export default CodingTestPage;