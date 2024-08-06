import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
  vus: 20, // 동시 사용자 수
  duration: '30s', // 테스트 실행 시간
};

const payload = JSON.stringify({
  isbn: '12345',
  content: 'This is a test review. Great book!',
  rating: 5,
  title: 'Some Book Title',
  image: 'http://example.com/image.jpg',
  user: {
    id: '67890'
  }
});

const params = {
  headers: {
    'Content-Type': 'application/json',
  },
};

export default function () {
  let res = http.post('http://localhost:8080/reviews', payload, params);
  check(res, { 'status was 201': (r) => r.status == 201 });
  sleep(1);
}
