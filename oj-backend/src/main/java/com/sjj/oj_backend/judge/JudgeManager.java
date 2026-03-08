package com.sjj.oj_backend.judge;

import com.sjj.oj_backend.judge.strategy.DefaultJudgeStrategy;
import com.sjj.oj_backend.judge.strategy.JavaLanguageJudgeStrategy;
import com.sjj.oj_backend.judge.strategy.JudgeContext;
import com.sjj.oj_backend.judge.strategy.JudgeStrategy;
import com.sjj.oj_backend.judge.codesandbox.model.JudgeInfo;
import com.sjj.oj_backend.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * 判题管理（简化调用）
 */
@Service
public class JudgeManager {

    /**
     * 执行判题
     *
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if ("java".equals(language)) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }

}
