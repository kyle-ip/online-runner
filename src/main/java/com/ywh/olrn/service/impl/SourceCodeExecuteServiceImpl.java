package com.ywh.olrn.service.impl;

import com.ywh.olrn.compiler.SourceCodeCompiler;
import com.ywh.olrn.service.SourceCodeExecuteService;
import org.springframework.stereotype.Service;

/**
 * @author ywh
 * @since 11/07/2020
 */
@Service
public class SourceCodeExecuteServiceImpl implements SourceCodeExecuteService {

    /**
     * 执行源码
     *
     * @param sourceCode
     * @return
     */
    public String execute(String sourceCode, String systemIn) {
        byte[] byteCode = SourceCodeCompiler.compile(sourceCode);
        return null;
    }
}
