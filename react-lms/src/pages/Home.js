import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import '../css/Home.css'; // 별도의 CSS 파일을 만들어 스타일을 관리합니다.
import axios from 'axios';
import cmtImage from '../images/ctmW2.png';

const RankingSection = ({ rankings }) => (
  <div className="card ranking-card">
    <div className='d-flex align-content-center justify-content-between title'>
      <h2 className='mb-0'>랭킹</h2>
      <Link to="/rankings" className="text-decoration-none small text-color mb-0">전체보기</Link>
    </div>
    <table>
      <thead>
        <tr>
          <th>순위</th>
          <th>랭크</th>
          <th>사용자</th>
          <th>점수</th>
        </tr>
      </thead>
      <tbody>
        {rankings.map((rank, index) => (
          <tr key={index} className='text-white'>
            <td>{index+1}</td>
            <td>{rank.userRank}</td>
            <td>{rank.nickname}</td>
            <td>{rank.expPoints}</td>
          </tr>
        ))}
      </tbody>
    </table>
  </div>
);

const ChallengesSection = ({ challenges }) => (
  <div className="card challenge-card">
    <div className='d-flex align-content-center justify-content-between title'>
      <h2 className='mb-0'>도전과제 및 대회</h2>
      <Link to="/challenges" className="text-decoration-none small text-color mb-0">전체보기</Link>
    </div>
    <ul>
      {challenges.map((challenge, index) => (
        <li key={index}>
          <Link to={`/challenges/${challenge.id}`} className="challenge-title text-decoration-none text-white">{challenge.title}</Link>
        </li>
      ))}
    </ul>
  </div>
);

const NoticeSection = ({ notices }) => (
  <div className="card notice-card">
    <div className='d-flex align-content-center justify-content-between title'>
      <h2 className='mb-0'>공지사항</h2>
      <Link to="/notices" className="text-decoration-none small text-color mb-0">전체보기</Link>
    </div>
    <ul>
      {notices.map((notice, index) => (
        <li className='d-flex justify-content-between' key={index}>
          <Link className='text-decoration-none text-white' to={`/notice/${notice.noticeId}`}>{notice.title}</Link><span className='text-color'>{formatDate(notice.createDate)}</span>
        </li>
      ))}
    </ul>
  </div>
);

const PopularProblemsSection = ({ problems }) => (
  <div className="card problem-card">
    <div className='title'>
      <h2>인기문제(TOP5)</h2>

    </div>
    {Array.isArray(problems) && problems.length > 0 ? (
      <ul>
        {problems.map((problem, index) => (
          <li key={index} className='d-flex justify-content-between'>
            <Link to={`/coding-page/${problem.quizId}`} className='text-decoration-none text-white'>{problem.title}</Link>
            <span className="problem-difficulty">난이도: {problem.quizRank}</span>
          </li>
        ))}
      </ul>
    ) : (
      <p className='text-color'>인기 문제가 없습니다.</p>
    )}
  </div>
);

const PopularBlog = ({blogs}) => (
  <div className="card problem-card">
    <div className='d-flex align-content-center justify-content-between title'>
      <h2 className='mb-0'>HOT! IT 소식</h2>
      <Link to="/blogs" className="text-decoration-none small text-color mb-0">전체보기</Link>
    </div>
    {Array.isArray(blogs) && blogs.length > 0 ? (
      <ul>
        {blogs.map((blogs, index) => (
          <li key={index} className='d-flex justify-content-between'>
            <Link to={`/coding-page/${blogs.title}`} className='text-decoration-none text-white'>{blogs.title}</Link>
            <span className="problem-difficulty">추천수: {blogs.like}</span>
          </li>
        ))}
      </ul>
    ) : (
      <p className='text-color'>소식이 없습니다.</p>
    )}
  </div>
);

const RecommendedEmployment = ({recommends}) => (
  <div className="card problem-card">
    <div className='d-flex align-content-center justify-content-between title'>
      <h2 className='mb-0'>추천 채용</h2>
      <Link to="/recommends" className="text-decoration-none small text-color mb-0">전체보기</Link>
    </div>
    {Array.isArray(recommends) && recommends.length > 0 ? (
      <ul>
        {recommends.map((recommends, index) => (
          <li key={index} className='d-flex justify-content-between'>
            <Link to={`/coding-page/${recommends.title}`} className='text-decoration-none text-white'>{recommends.title}</Link>
            <span className="problem-difficulty">{recommends.company}</span>
          </li>
        ))}
      </ul>
    ) : (
      <p className='text-color'>추천 채용이 없습니다.</p>
    )}
  </div>
);

const formatDate = (dateString) => {
  const date = new Date(dateString);
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  
  return `${month}-${day} ${hours}:${minutes}`;
};

const Home = () => {
  const [rankings, setRankings] = useState([]);
  const [challenges, setChallenges] = useState([]);
  const [notices, setNotices] = useState([]);
  const [problems, setProblems] = useState([]);
  const [blogs, setBlogs] = useState([]);
  const [recommends, setRecommends] = useState([]);



  useEffect(() => {
    const fetchPopularProblems = async () => {
      try {
        const response = await axios.get('/api/quiz/counts');
        setProblems(response.data);
        const expResponse = await axios.get('/api/member/exp-top');
        setRankings(expResponse.data);
        const challengeResponse = await axios.get('/api/challenges/top5');
        setChallenges(challengeResponse.data);
        const noticeResponse = await axios.get('/api/notice');
        setNotices(noticeResponse.data);
        const blogResponse = await axios.get('/api/etc/blogs');
        setBlogs(blogResponse.data);
        const recommendsResponse = await axios.get('/api/etc/recommends');
        setRecommends(recommendsResponse.data);
      } catch (error) {
        console.error('데이터를 불러오는데 실패하였습니다. :', error);
      }
    };

    fetchPopularProblems();
    
  }, []);

  return (
    <div className='container-fluid'>
      <div className='container'>
        <div className="ctm-section">
          <img src={cmtImage} alt="ctm" className="ctm-image"/>
        </div>
        <div className="sub-title text-center my-4">
          <h1>코딩테스트몬스터</h1>
          <p>당신의 코딩 실력을 극대화하세요!</p>
        </div>
        <main>
          <div className="grid-container">
            <RankingSection rankings={rankings} />
            <ChallengesSection challenges={challenges} />
            <NoticeSection notices={notices} />
            <PopularProblemsSection problems={problems} />
            <PopularBlog blogs={blogs}/>
            <RecommendedEmployment recommends={recommends}/>
          </div>
        </main>
      </div>
    </div>
  );
};

export default Home;