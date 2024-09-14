import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8282', // 스프링 부트 서버 주소
  withCredentials: true, // 쿠키를 포함한 요청 활성화
});

export default api;