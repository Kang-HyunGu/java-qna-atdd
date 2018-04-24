package codesquad.service;

import codesquad.CannotDeleteException;
import codesquad.UnAuthenticationException;
import codesquad.domain.AnswerRepository;
import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import codesquad.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QnaServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private DeleteHistoryService deleteHistoryService;

    @InjectMocks
    private QnaService qnaService;

    @Test
    public void 질문_수정() throws UnAuthenticationException {
        User user = new User();

        Question found = new Question("원래 제목", "원래 내용");
        found.writeBy(user);

        String title = "질문 제목 수정";
        String contents = "질문 내용 수정";
        Question updatedQuestion = new Question(title, contents);

        when(questionRepository.findOne(1L)).thenReturn(found);
        when(questionRepository.save(found)).thenReturn(updatedQuestion);

        Question actual = qnaService.update(user, 1L, updatedQuestion);

        assertThat(actual.getTitle()).isEqualTo(title);
        assertThat(actual.getContents()).isEqualTo(contents);
    }

    @Test
    public void 질문_삭제() throws CannotDeleteException {
        User user = new User();
        Question found = new Question("원래 제목", "원래 내용");
        found.writeBy(user);

        when(questionRepository.findOne(1L)).thenReturn(found);

        qnaService.deleteQuestion(user, 1L);

        assertThat(found.isDeleted()).isTrue();
    }
}