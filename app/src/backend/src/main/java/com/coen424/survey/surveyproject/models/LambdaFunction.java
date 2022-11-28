package com.coen424.survey.surveyproject.models;

import java.util.Arrays;

public class LambdaFunction {

    private String funcName;

    private byte[] funcCode;

    private String funcHandler;

    public String getFuncName() {
        return funcName;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    public byte[] getFuncCode() {
        return funcCode;
    }

    public void setFuncCode(byte[] funcCode) {
        this.funcCode = funcCode;
    }

    public String getFuncHandler() {
        return funcHandler;
    }

    public void setFuncHandler(String funcHandler) {
        this.funcHandler = funcHandler;
    }

    @Override
    public String toString() {
        return "LambdaFunction{" +
                "funcName='" + funcName + '\'' +
                ", funcCode=" + Arrays.toString(funcCode) +
                ", funcHandler='" + funcHandler + '\'' +
                '}';
    }
}
