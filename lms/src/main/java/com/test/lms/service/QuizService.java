package com.test.lms.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.test.lms.entity.Member;
import com.test.lms.entity.Quiz;
import com.test.lms.entity.QuizAnswer;
import com.test.lms.repository.MemberRepository;
import com.test.lms.repository.QuizAnswerRepository;
import com.test.lms.repository.QuizRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuizService {

        private final QuizRepository quizRepository;
        private final MemberRepository memberRepository;
        private final QuizAnswerRepository quizAnswerRepository;
        private final MemberService memberService;
        
        
        
        //모든 퀴즈 리스트 가져오기
        public List<Quiz> getAllQuizzes(){
                return quizRepository.findAll();
        }

        //퀴즈 디테일 가져오기
        public Quiz getQuizById(Long quizId){
                return quizRepository.findById(quizId).orElseThrow(()->new EntityNotFoundException("퀴즈를 찾을 수 없습니다! ID : " + quizId));
        }     

        //퀴즈 생성
        public Quiz create(String title, String content, String correct, String quizRank, String output){

                Quiz quiz = new Quiz();

                quiz.setTitle(title);
                quiz.setContent(content);
                quiz.setCorrect(correct);
                quiz.setQuizRank(quizRank);
                quiz.setOutput(output);
                quiz.setCreateDate(LocalDateTime.now());
                quiz.setCount(0);

                return quizRepository.save(quiz);
        }

        //퀴즈 삭제
        public void delete(Quiz quiz){
                this.quizRepository.delete(quiz);
        }

        //퀴즈 수정

        public Quiz updateQuiz(Quiz quiz){
                
                if(quiz == null || quiz.getQuizId() == 0L ) {
                throw  new IllegalArgumentException("존재하지 않는 퀴즈 정보입니다.");
                }

                //기존레슨 정보 DB에서 조회
                Quiz existingQuiz = quizRepository.findById(quiz.getQuizId()).orElseThrow(() -> new EntityNotFoundException("해당 퀴즈를 찾을 수 없습니다. ID :" + quiz.getQuizId()));

                existingQuiz.setTitle(quiz.getTitle()); //제목 수정
                existingQuiz.setContent(quiz.getContent()); //내용 수정
                existingQuiz.setCorrect(quiz.getCorrect()); //정답 수정
                existingQuiz.setQuizRank(quiz.getQuizRank()); //랭크 수정
                existingQuiz.setOutput(quiz.getOutput());
                
                return quizRepository.save(existingQuiz);
        }

        //페이징 처리
        public Page<Quiz> getList(int page){
                Pageable pageable = PageRequest.of(page,10);
                return this.quizRepository.findAll(pageable);
        }

        
        //퀴즈 정답
        public boolean submitQuizAnswer(Long quizId, String answer, String output, boolean isPublic, String userName) {
            log.debug("Received answer submission - quizId: {}, answer: {}, isPublic: {}, userName: {}", quizId, answer, isPublic, userName);

            // quiz ID로 퀴즈 정보 가져오기
            Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new EntityNotFoundException("퀴즈를 찾을 수 없습니다."));

            // 사용자 ID로 멤버 정보 가져오기
            Member member = memberRepository.findByUsername(userName)
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));

            // 제출된 답과 퀴즈의 정답을 비교
            String trimmedOutput = output.trim();
            String trimmedCorrect = quiz.getOutput().trim();
            boolean isCorrect = trimmedCorrect.equalsIgnoreCase(trimmedOutput);

            // 퀴즈 제출 카운트 증가
            quiz.setCount(quiz.getCount() + 1);
            quizRepository.save(quiz); // 변경 사항 저장

            // 기존 답변을 찾거나 새로운 답변 생성
            QuizAnswer quizAnswer = quizAnswerRepository.findByQuizAndMember(quiz, member)
                .orElse(new QuizAnswer());

            boolean wasIncorrectBefore = quizAnswer.getId() != null && !quizAnswer.isCorrect();

            // 답변 정보 설정
            quizAnswer.setAnswer(answer);
            quizAnswer.setOutput(trimmedOutput);
            quizAnswer.setPublic(isPublic);
            quizAnswer.setMember(member);
            quizAnswer.setQuiz(quiz);
            quizAnswer.setSolvedQuizTime(LocalDateTime.now());
            quizAnswer.setCorrect(isCorrect);

            int expPoints = 0;

            if (isCorrect) {
                // quizRank에 따라 경험치 결정
                switch (quiz.getQuizRank()) {
                    case "D":
                        expPoints = 5;
                        break;
                    case "C":
                        expPoints = 10;
                        break;
                    case "B":
                        expPoints = 20;
                        break;
                    case "A":
                        expPoints = 30;
                        break;
                    default:
                        log.warn("존재하지 않는 랭크입니다.: {}", quiz.getQuizRank());
                        break;
                }

                if (quizAnswer.getId() == null) {
                    log.debug("New correct answer submitted");
                } else if (wasIncorrectBefore) {
                    log.debug("Answer updated from incorrect to correct");
                    // 오답에서 정답으로 바뀐 경우에도 동일한 경험치 부여
                } else {
                    log.debug("Correct answer resubmitted");
                    expPoints = 0; // 이미 정답이었던 경우 추가 경험치 없음
                }

                if (expPoints > 0) {
                    memberService.addExpPoints(member.getMemberNum(), expPoints);
                    log.debug("Added {} experience points to user {}", expPoints, member.getUsername());
                }
            } else {
                log.debug("Answer is incorrect, updating existing record or creating new one");
            }

            quizAnswerRepository.save(quizAnswer);
            return isCorrect;
        }
        
        // 퀴즈 카운트가 많은 5개 퀴즈
        public List<Quiz> getTop5QuizzesByCount() {
            Pageable topFive = PageRequest.of(0, 5);
            return quizRepository.findTop5QuizzesByCountDesc(topFive);
        }

        //퀴즈 검색하기

        //제목 또는 내용으로 검색
        public List<Quiz> searchByTitleOrContent(String keyword) {
                return quizRepository.findByTitleContainingOrContentContaining(keyword, keyword);
        }
        // 제목과 내용 모두에 키워드가 포함된 퀴즈 검색
        public List<Quiz> searchByTitleAndContent(String keyword) {
                return quizRepository.findByTitleContainingAndContentContaining(keyword, keyword);
        }
        
        //개발 진행에 따라 필요하면 복구 또는 삭제

        // public Quiz getQuizAnswer(Long quizId, Long Id){
                
        //         //퀴즈 ID로 퀴즈 찾기
        //         Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new EntityNotFoundException("퀴즈를 찾을 수 없습니다."));
        //         //사용자 ID로 멤버 정보 가죠오기
        //         Member member = memberRepository.findById(Id).orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));
        //         QuizAnswer quizAnswer = quizAnswerRepository.findById(Id).orElseThrow(() -> new EntityNotFoundException());
        //         // 정답이 비공개이면 정답을 숨겨줌
        //         if (!quizAnswer.isPublic() && !quizAnswer.getMember().getMemberNum().equals(member.getMemberNum())){
        //                 quizAnswer.setAnswer(null);
        //         }
                
        //         //퀴즈는 항상 반환, 정답은 조건에 따라 다름
        //         return quiz;
        // }
}
