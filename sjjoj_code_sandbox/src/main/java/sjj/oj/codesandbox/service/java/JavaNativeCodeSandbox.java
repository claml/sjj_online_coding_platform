package sjj.oj.codesandbox.service.java;

import sjj.oj.codesandbox.model.ExecuteCodeRequest;
import sjj.oj.codesandbox.model.ExecuteCodeResponse;
import org.springframework.stereotype.Component;

/**
 * Java 原生代码沙箱实现（直接复用模板方法）
 * @author sjj
 */
@Component
public class JavaNativeCodeSandbox extends JavaCodeSandboxTemplate {

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        return super.executeCode(executeCodeRequest);
    }
}
