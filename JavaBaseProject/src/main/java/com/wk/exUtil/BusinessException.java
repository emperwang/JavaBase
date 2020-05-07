package com.wk.exUtil;

/**
 *  业务异常
 */
public class BusinessException extends BaseException {
    private static final long serialVersionUID = 1L;

    /**
     * @param responseEnum
     * @param args  替换占位符的数据
     * @param message
     */
    public BusinessException(IResponseEnum responseEnum, Object[] args, String message) {
        super(responseEnum, args, message);
    }

    public BusinessException(IResponseEnum responseEnum, Object[] args, String message, Throwable cause) {
        super(responseEnum, args, message, cause);
    }
}
