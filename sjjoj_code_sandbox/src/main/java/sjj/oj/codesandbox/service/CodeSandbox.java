package sjj.oj.codesandbox.service;


import sjj.oj.codesandbox.model.ExecuteCodeRequest;
import sjj.oj.codesandbox.model.ExecuteCodeResponse;

/**
 * 代码沙箱接口定义
 *
 * @author sjj
 */
public interface CodeSandbox
{

    /**
     * 执行代码
     *
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
