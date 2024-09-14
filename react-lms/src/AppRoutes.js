import React, { useMemo } from 'react';
import { Routes, Route } from 'react-router-dom';
import Home from './pages/Home';
import Login from './pages/Login';
import SignUp from './pages/SignUp';
import CodingTest from './pages/CodingTest';
import CodingPage from './pages/CodingTestPage';
import MyPage from './pages/MyPage';
import Admin from './components/Admin/Admin';
import AdminQuiz from './components/Admin/AdminQuiz';
import QuizForm from './components/Admin/QuizForm';
import SolvedQuiz from './pages/SolvedQuiz';
import ChallengeAdmin from './components/Admin/AdminChallenge';
import ProtectedRoute from './components/ProtectedRoute';
import ChallengePage from './pages/ChallengeDetail';
import NoticeAdmin from './components/Admin/AdminNotice';
import NoticeForm from './components/Admin/NoticeForm';
import NoticePage from './pages/NoticePage';
import { useAuth } from './AuthContext';
import ChangePassword from './pages/ChangePassword';
import ChallengeForm from './components/Admin/ChallengeForm';

const AppRoutes = () => {
  const { username } = useAuth();
  
  const routes = useMemo(() => (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/login" element={<Login />} />
      <Route path="/signup" element={<SignUp />} />
      <Route path="/coding-test" element={<CodingTest />} />
      <Route path="/solved" element={<SolvedQuiz />} />
      <Route path='/coding-page/:quizId' element={<CodingPage username={username} />} />
      <Route path="/my-page" element={<MyPage />} />
      <Route path='/admin' element={<ProtectedRoute><Admin /></ProtectedRoute>} />
      <Route path='/admin/quiz' element={<ProtectedRoute><AdminQuiz /></ProtectedRoute>} />
      <Route path='/admin/quiz/edit' element={<QuizForm />}></Route>
      <Route path='/admin/challenges' element={<ProtectedRoute><ChallengeAdmin /></ProtectedRoute>} />
      <Route path='/admin/challenges/edit' element={<ProtectedRoute><ChallengeForm /></ProtectedRoute>} />
      <Route path='/admin/challenges/edit/:id' element={<ProtectedRoute><ChallengeForm /></ProtectedRoute>} />
      <Route path='/admin/notice' element={<ProtectedRoute><NoticeAdmin /></ProtectedRoute>} />
      <Route path='/admin/notice/edit' element={<ProtectedRoute><NoticeForm /></ProtectedRoute>} />
      <Route path='/admin/notice/edit/:noticeId' element={<ProtectedRoute><NoticeForm /></ProtectedRoute>} />
      <Route path='/admin/quiz/edit/:quizId' element={<QuizForm />}></Route>
      <Route path="/change-password" element={<ChangePassword />} />
      <Route path='/challenges/:id' element={<ChallengePage />}/>
      <Route path='/notice/:id' element={<NoticePage />}/>
    </Routes>
  ), [username]);  // username이 변경될 때만 재생성

  return routes;
};

export default AppRoutes;